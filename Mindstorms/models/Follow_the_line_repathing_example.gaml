model follow_the_line_warehouse

global {
	int sensor_range <- 20;
	
	// The graph that the robots move on.
	graph movement_graph<-spatial_graph([]);
	// The nodes that make up the graph.
	list<point> nodes;
	// The drop on and drop off points.
	list<point> drop_nodes;
	// The triage point.
	list<point> triage_nodes;
	// The weights of the graph.
	list<float> edge_weights <- [5, 10, 10, 5];
	
    init {
    	// Add the points to the list and graph:
		add point(10,10) to: drop_nodes;
		add point(90,10) to: drop_nodes;
		add point(10,90) to: drop_nodes;
		add point(90,90) to: drop_nodes;
		loop nod over: drop_nodes {
	    	movement_graph <- movement_graph add_node(nod);
		}	
		
		// Add the connections between the nodes
		movement_graph <- movement_graph add_edge(drop_nodes at 0::drop_nodes at 1);
		movement_graph <- movement_graph add_edge(drop_nodes at 0::drop_nodes at 2);
		movement_graph <- movement_graph add_edge(drop_nodes at 2::drop_nodes at 3);
		movement_graph <- movement_graph add_edge(drop_nodes at 1::drop_nodes at 3);
		
		// Calculate the weight based on the length of the edges so that the robot moves the same speed everywhere.
		// movement_graph <- movement_graph with_weights (movement_graph.edges as_map (each:: geometry(each).perimeter));
		edge_weights <- [
			geometry(movement_graph.edges at 0).perimeter,
			geometry(movement_graph.edges at 1).perimeter*2,
			geometry(movement_graph.edges at 2).perimeter*2,
			geometry(movement_graph.edges at 3).perimeter
		];
		movement_graph <-  with_weights (movement_graph, edge_weights);
	
		// Create the robots
		create robot number: 1 with: (location: drop_nodes at 0, aspect_color: #green, target: drop_nodes at 3, mg: copy(movement_graph), name: "green");
		create robot number: 1 with: (location: drop_nodes at 3, aspect_color: #red, target: drop_nodes at 0, mg: copy(movement_graph), name: "red");
    }
}

// TODO: Change this to take a list of drop of points and also integrate the more advanced behaviour.
species robot skills: [moving]{
	graph mg; // The local copy of the graph that the robot has TODO: Think about this is still needed
	
	point target; // The next drop_of point the robot will move to.
	path route; // The route that the robot will take to the target.
	rgb aspect_color; // The color of the robot.
	string name; // The name of the robot.
	
	// TODO: Rework this to be nicer.
	bool flag <- false;
	int last_edge; // Pointer to the last egde the robot has traveresd in the graph
	point last; // The last node the robot has passed.
	
	geometry avoidrect;
	
	init{}
	
	aspect base{
		draw circle(2) color: aspect_color;
		// Draw the rect
		// Avoidance rectangle
		point r <- point(sin(heading+90), -cos(heading+90));
		avoidrect <- rectangle(2,sensor_range) translated_by point(r*sensor_range/2) rotated_by (heading+90);
		draw avoidrect  color:#orange;
	}
	
	// Base reflex that drives the robot to move
	reflex {
		// For now only move between 0 and 3
		if (route = nil or location = target){
			if (target = (drop_nodes at 3)){
				target <- (drop_nodes at 0);
			}else{
				target <- (drop_nodes at 3);
			}
			route <- path_between(mg, location, target);
		}
		
		do follow path: route; // alternaively with move_weights: graph_weights;
	}
	
	
	// Reflex to log interesting information for debugging.
	// The Setting of the last edge and location is also important
	reflex log {
		if (location in drop_nodes){
			// write name + " at:" + location;
			last <- location;
		}
		
		if (current_edge != nil){
			last_edge <- mg.edges index_of(current_edge);
		}
	}
	
	// Reflex of the Robot to go back to the last edge it passed.
	// TODO: Make this not rely on the flag since that is a bit hacky.
	reflex go_back{
		if (flag){
			route <- path_between(mg, location, last); 
		}
		if(location = last and flag){
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
	
	// TODO: This can and should be done better.
	reflex collision{
		if (avoidrect overlaps union(obstacle collect each.geo)){
			flag <- true;
		}
		// TODO: This kind of works for now.
		if (avoidrect overlaps (union(robot collect geometry(each)) - geometry(self))){
			flag <- true;
		}
		// Also cehck for the
	}
	
	user_command "Send Battery Warning" action: battery_warning;
	action battery_warning {
		write name+": Battery is low."; // TODO: Put some logic behind this
	}
}


species obstacle {
	geometry geo <- square(4);
	bool should_die <- false;
	
	aspect base{
		draw geo color: #brown;
	}
	
	reflex{ if should_die { do die; }}
}

experiment MyExperiment type: gui {
	action my_action{
		write #user_location; // Use this to get the mouse location
		
		// Currently a POC this will be redone eventually.
		// movement_graph <- with_weights (movement_graph, [500, 10, 10, 500]);
		loop rob over: robot{
			rob.flag <- true;
			// rob.route  <- path_between(rob.mg, rob.location, rob.target);
		}
	}
	
	action place_obstacle {
		// Create a new obstacle on the location the user clicked.	
		if (not (#user_location overlaps union(obstacle collect each.geo))){
			create obstacle number: 1 with: (location: #user_location);
		}
	}
	
	// Remove all the obstacles
	action clean_obstacles{	
		loop ob over: obstacle {
			ob.should_die <- true;
		}
	}
	
	user_command "Clear all Obstacles" action: clean_obstacles category:"Obstacles";
	parameter "Sensor Range" var: sensor_range;
	
    output {
		display MyDisplay /*type: 2d*/ {
			event mouse_down action: my_action;
			event 'o' action: place_obstacle; // place an obstacle if the o key is pressed.
			
			// TODO: Find a better way to draw this
		    graphics "shortest path" {
		    	loop nod over: nodes {
			    	draw circle(1) at: nod color: #black;
				}
				loop nod over: drop_nodes {
			    	draw circle(3) at: nod color: #yellow;
			    	draw "Drop point" at: nod color: #black;
				}
				loop nod over: triage_nodes {
			    	draw circle(3) at: nod color: #red;
			    	draw "Triage point" at: nod color: #black;
				}
		    	
				loop edges over: movement_graph.edges {
				    draw geometry(edges) color: #black;
				}  
		    }
		    species obstacle aspect: base;
		    species robot aspect: base;
		}
    }
}