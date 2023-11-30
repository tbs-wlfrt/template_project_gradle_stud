model follow_the_line_warehouse

global {
	geometry shape <- rectangle(200#m,200#m);
	
	int sensor_range <- 20;
	int rotting_chance <- 15;
	
	// The graph that the robots move on.
	graph movement_graph<-spatial_graph([]);
	// The nodes that make up the graph.
	list<point> nodes;
	// The drop on and drop off points.
	list<point> drop_nodes;
	// The triage point.
	list<point> triage_nodes;
	// The recharge points.
	list<point> recharge_nodes;
	// The weights of the graph.
	list<float> edge_weights <- [5, 10, 10, 5];
	
	list<list<int>> tasks <- [[3,0], [0,3]];
	
    init {
    	// Add the points to the list and graph:
		add point(50,50) to: drop_nodes;
		add point(150,50) to: drop_nodes;
		add point(50,150) to: drop_nodes;
		add point(150,150) to: drop_nodes;
		
		loop nod over: drop_nodes {
	    	movement_graph <- movement_graph add_node(nod);
		}	
		
		// Add triage point
		add point(100,100) to: triage_nodes;
		
		loop nod over: triage_nodes {
	    	movement_graph <- movement_graph add_node(nod);
		}
		
		// Add recharge points (one per robot!)
		add point(25,25) to: recharge_nodes;
		add point(175,175) to: recharge_nodes;
		
		// Add the connections between the nodes
		// drop nodes to each other
		movement_graph <- movement_graph add_edge(drop_nodes at 0::drop_nodes at 1);
		movement_graph <- movement_graph add_edge(drop_nodes at 0::drop_nodes at 2);
		movement_graph <- movement_graph add_edge(drop_nodes at 2::drop_nodes at 3);
		movement_graph <- movement_graph add_edge(drop_nodes at 1::drop_nodes at 3);
		movement_graph <- movement_graph add_edge(drop_nodes at 1::drop_nodes at 3);
		// drop nodes to triage point
		movement_graph <- movement_graph add_edge(drop_nodes at 0::triage_nodes at 0);
		movement_graph <- movement_graph add_edge(drop_nodes at 1::triage_nodes at 0);
		movement_graph <- movement_graph add_edge(drop_nodes at 2::triage_nodes at 0);
		movement_graph <- movement_graph add_edge(drop_nodes at 3::triage_nodes at 0);
		// drop nodes to recharge points
		movement_graph <- movement_graph add_edge(drop_nodes at 0::recharge_nodes at 0);
		movement_graph <- movement_graph add_edge(drop_nodes at 3::recharge_nodes at 1);
		
		// Calculate the weight based on the length of the edges so that the robot moves the same speed everywhere.
		// movement_graph <- movement_graph with_weights (movement_graph.edges as_map (each:: geometry(each).perimeter));
		edge_weights <- [
			geometry(movement_graph.edges at 0).perimeter*0.5, // To make one robot faster than the other
			geometry(movement_graph.edges at 1).perimeter*2,
			geometry(movement_graph.edges at 2).perimeter*2,
			geometry(movement_graph.edges at 3).perimeter,
			geometry(movement_graph.edges at 4).perimeter*1.5, // TODO to avoid going to the triage point for now
			geometry(movement_graph.edges at 5).perimeter*1.5,
			geometry(movement_graph.edges at 6).perimeter*1.5,
			geometry(movement_graph.edges at 7).perimeter*1.5,
			geometry(movement_graph.edges at 8).perimeter,
			geometry(movement_graph.edges at 9).perimeter
		];
		movement_graph <-  with_weights (movement_graph, edge_weights);
	
		// Create the robots
		create robot number: 1 with: (
			location: recharge_nodes at 0,
			aspect_color: #green,
			mg: copy(movement_graph),
			name: "green"
			// pickup_list: [3,2],
			// dropoff_list: [0,1]
		);
		create robot number: 1 with: (
			location: recharge_nodes at 1,
			aspect_color: #red,
			mg: copy(movement_graph),
			name: "red"
			// pickup_list: [0,1],
			// dropoff_list: [3,2]
		);
		
		create controller with: (tasks: tasks);
    }
}

// TODO: Change this to take a list of drop of points and also integrate the more advanced behaviour.
species robot skills: [moving]{
	graph mg; // The local copy of the graph that the robot has TODO: Think about this is still needed
	
	point target; // The next point the robot will move to.
	path route; // The route that the robot will take to the target.
	
	rgb aspect_color; // The color of the robot.
	string name; // The name of the robot.
	
	// TODO: Rework this to be nicer.
	bool flag <- false;
	int last_edge; // Pointer to the last edge the robot has traversed in the graph
	point last; // The last node the robot has passed.
	
	// TODO robots need to 'order' the pickups given the earliest pickup/latest dropoff times
	list<int> pickup_list;  // lists the remaining pickup locations
	list<int> dropoff_list;  // lists the remaining dropoff locations 
	bool has_crate <- true; // whether robot has crate (= is moving towards dropoff); init at true to get first pickup location
	bool finished_deliveries <- true;
	
	geometry avoidrect;
	
	point recharge_point;
	bool battery_low <- false;
	bool going_charging <- false;
	bool is_charging <- false;
	int charge_timer <- 100; // how long to charge for
	int charge_timer_default <- 100;
	 
	init{
		last <- location;
		recharge_point <- location; // TODO we now assume all robots start at their recharge points
	}
	
	aspect base{
		draw circle(2) color: aspect_color;
		// Draw the rect
		// Avoidance rectangle
		point r <- point(sin(heading+90), -cos(heading+90));
		avoidrect <- rectangle(2,sensor_range) translated_by point(r*sensor_range/2) rotated_by (heading+90);
		draw avoidrect  color:#orange;
		
		draw image("robot.png") size: {10, 15} rotate: (heading-90);
	}
	
	reflex when: going_charging and location=target{
		write name + " arrived at the charging station. Initiating charge sequence";
		going_charging <- false;
		is_charging<- true;
	}
	
	reflex when: is_charging{
		charge_timer <- charge_timer - 1;
		if(charge_timer = 10){
			write name + " almost finished charging, only ten more minutes!";
			// TODO notify
		}
		if(charge_timer = 0){
			write name + " finished charging! Ready for action";
			is_charging <- false;
			charge_timer <- charge_timer_default;
		}
	}
	
	// Base reflex that drives the robot to move
	reflex when: not is_charging and not finished_deliveries and not going_charging and (route = nil or location = target){
		// TODO prettify/split up this reflex -> not really possible; reflexes can run in parallel (no mutex on variables)
		if(has_crate or route = nil){
			if(route != nil){
				write name +" dropped of crate at:" + location;
			}
			// now check if another pickup exists
			has_crate <- false;
			if(battery_low){
				
				// TODO: Figure out where thid goes, this crashes ATM
				list<list<int>> redistribute_tasks;
				loop i from: 0 to: length(pickup_list){
					redistribute_tasks <- redistribute_tasks + [[pickup_list[i], dropoff_list[i]]];  
				}
				ask controller {
					do add_tasks(redistribute_tasks);
				}
				
				// TODO send back to controller
				dropoff_list <- ([]);
				pickup_list<- ([]);
				
				target <- recharge_point; // when done, go to personal recharge point
				route <- path_between(mg, location, target);
				going_charging <- true;
				return;
			}
			if(length(pickup_list) > 0){
				int index <- pickup_list at 0;
				target <- (drop_nodes at index);  // set new target position from drop_nodes
				remove from:pickup_list index:0; // pop from list pickup list
				write name + " is moving to pickup crate at " + target + "(node " + index + ")";
			}else{
				write name + " finished delivering crates";
				finished_deliveries <- true;
				target <- recharge_point; // when done, go to personal recharge point				

				// TODO add the controller code here
			}
		}else{
			// don't have a crate yet, so we arrived at a pickup point
			write name + " pickup up crate at:" + location;
			// check if crate is potentially rotten
			has_crate <- true;
			if(length(dropoff_list) > 0){
				int index <- dropoff_list at 0;  // set new target to be dropoff point
				target <- (drop_nodes at index);
				remove from:dropoff_list index:0;
				if(rnd(100) > (100-rotting_chance)){
					target <- (triage_nodes at 0);
					write name + " detected potentially rotting crate";
				}else{
					write name + " is moving to drop off crate at " + target + "(node " + index + ")";	
				}
			}else{
				write name + " cannot find dropoff location; check if every pickup has a dropoff instruction";
				target <- recharge_point; // when done, go to personal recharge point
			}
		}
		route <- path_between(mg, location, target);
		//write name + "follows route: " + route;
		
		
		ask controller {
			do divide_tasks;
		}
	}
	
	// reflex gg when:
	
	reflex advance when: route != nil and not is_charging{
		do follow path: route; // alternaively with move_weights: graph_weights;
	}
	
	
	// Reflex to log interesting information for debugging.
	// The Setting of the last edge and location is also important
	reflex track_path {
		point int_loc_fl <- point({floor(location.x), floor(location.y)});
		point int_loc_cl <- point({ceil(location.x), ceil(location.y)});
		if (int_loc_fl in mg.vertices or int_loc_cl in mg.vertices){
			last <- location;
		}		
		if (current_edge != nil){
			last_edge <- mg.edges index_of(current_edge);
		}
	}
	
	// Reflex of the Robot to go back to the last edge it passed.
	// TODO: Make this not rely on the flag since that is a bit hacky.
	reflex go_back when: flag{
		//write name + " value of last: " + last;
		route <- path_between(mg, location, last); 
		if(location = last){
			// If we reached the last node it is time to recalculate the path.
			// Since we encountered an obstacle on last_edge we need to update its weight such that we don't take that edge.
			
			graph temp_graph <- copy(mg);
			list<int> temp_weights <- copy(edge_weights);
			temp_weights[last_edge] <- 500;
			
			temp_graph <- with_weights (temp_graph, temp_weights);
			route <- path_between(temp_graph, location, target);
			flag <- false;	
		}
	}
	
	reflex obstacle_avoidance when: avoidrect overlaps union(obstacle collect each.geo){
		flag <- true;
	}
	
	// TODO: This can and should be done better.
	reflex robot_avoidance{
		// TODO: This kind of works for now.
		if (avoidrect overlaps (union(robot collect geometry(each)) - geometry(self))){
			// which robot and 			
			loop rob over: robot{
				// check which overlaps
				if (rob = self){ continue; }
				if (avoidrect overlaps geometry(rob)){
					// Found the overlapping
					
					float rob_distance <- distance_to(rob.location, rob.last);
					float our_distance <- distance_to(self.location, self.last);
					if(rob_distance < our_distance){
						write name+": other robot is shorter";
						speed <- 0;
					}else{
						// TODO: Need to wait first before this is done.
						write name+": we are shorter";
						flag <- true; // TODO: THis is still not ideal yet.
					}
				}
			}
		}else{
			speed <- 1; // TODO: This is very hacky so not sure this is great
		}
	}
	
	user_command "Send Battery Warning" action: battery_warning;
	action battery_warning {
		write name+": Battery is low.";
		// TODO communicate remaining orders back to central location
		battery_low <- true;
	}
	
	action start{
		finished_deliveries <- false;
		has_crate <- false;
		target <- nil;
		route <- nil;
	}
		
	action tasks(list l1, list l2) {
		
		pickup_list <- l1;
		dropoff_list <- l2;
//		pickup_list <- [3,2];
//		dropoff_list <- [0,1];
		
		// Needed to get the robot back to the base bahvaiour
		finished_deliveries <- false;
		has_crate <- false;
		target <- nil;
		route <- nil;
	}
}

species obstacle {
	geometry geo <- square(4);
	
	aspect base{
		draw geo color: #brown;
	}
}

species controller {
	list<list<int>> tasks; // list of [pickup, drop_off]
	// map<string, list<list<int>>> tasks_per_robot;
	
	init{
		location <- (10,10);
		write tasks;
		do divide_tasks;
	}
	
	user_command "Add task" action: t;
	action t {
		tasks <- tasks + [[rnd(length(drop_nodes)-1), rnd(length(drop_nodes)-1)]];
		write "Task added: " + tasks;
		
		// could be that this doesn't work
		do divide_tasks;
	}
	
	user_command "Give tasks" action: divide_tasks;
	action divide_tasks {
		int i <- 0;
		list<robot> free_robots;
		
		loop rob over: robot {
			if rob.finished_deliveries {
				free_robots <- free_robots + [rob];
			}
		}
		write free_robots;
		write tasks;
		
		int l <- length(free_robots);
		if (l = 0){ return; }
		
		loop task over: tasks {
			robot cur <- free_robots[i];
			
			// TODO: Be smarter about this
			if cur.finished_deliveries {
				ask cur {
					pickup_list <- pickup_list + task[0];
					dropoff_list <- dropoff_list + task[1];
				}
			}	
			i <- (i + 1) mod l;
		}
		
		if(length(tasks) = 0){ return; } // This is necessary so that the robot returns to the charging station if there is nothig new.
		ask free_robots{
			do start;
		}
		
		// Clear all the distriubted tasks
		
		tasks <- [];
	}
	
	action add_tasks(list<list<int>> new_tasks){
		tasks <- tasks + new_tasks;
		do divide_tasks;
	}
	
	
	aspect base 
	{
		draw rectangle(40,5) color: #black;
		draw "Controller" color: #white;
	}
}

experiment MyExperiment type: gui {
	
	action place_obstacle {
		// Create a new obstacle on the location the user clicked.	
		if (not (#user_location overlaps union(obstacle collect each.geo))){
			create obstacle number: 1 with: (location: #user_location);
		}
	}
	
	// Remove all the obstacles
	action clean_obstacles{	
		ask obstacle  {
			do die;
		}
	}
	
	user_command "Clear all Obstacles" action: clean_obstacles category:"Obstacles";
	parameter "Sensor Range" var: sensor_range;
	parameter "Rotting Chance (int%)" var: rotting_chance;
	parameter "Tasks" var: tasks;
	// TODO: Add parameters for evrything and this should work.
	
    output {
		display MyDisplay /*type: 2d*/ {
//			event mouse_down action: my_action;
			event 'o' action: place_obstacle; // place an obstacle if the o key is pressed.
			
			// TODO: Find a better way to draw this
		    graphics "shortest path" {
		    	loop nod over: nodes {
			    	draw circle(1) at: nod color: #black;
				}
				int i <- 0;
				loop nod over: drop_nodes {
			    	draw circle(3) at: nod color: #yellow;
			    	draw "Drop point: " + i at: nod color: #black;
			    	i <- i + 1;
				}
				loop nod over: triage_nodes {
			    	draw circle(3) at: nod color: #red;
			    	draw "Triage point" at: nod color: #black;
				}
				loop nod over: recharge_nodes {
					draw circle(3) at: nod color: #turquoise;
			    	draw "recharge point" at: nod color: #black;
				}
		    	
				loop edges over: movement_graph.edges {
				    draw geometry(edges) color: #black;
				}  
		    }
		    species obstacle aspect: base;
		    species robot aspect: base;
		   	species controller aspect: base;
		}
    }
}