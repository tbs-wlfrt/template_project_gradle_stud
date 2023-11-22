model follow_the_line_warehouse

global {
	// The graph that the robots move on.
	graph movement_graph<-spatial_graph([]);
	// Keeps track of the weight of the edges in the graph so that the robots move speed is consitent.
	// map<geometry, float> graph_weights;
	
	// - TODO: Think about if it is better to make this specieses instead of points.
	// The nodes that make up the graph.
	list<point> nodes;
	// The drop on and drop off points
	list<point> drop_nodes;
	// The triage point
	list<point> triage_nodes;
	
	
	list<int> w <- [5, 10, 10, 5];
	
    init {
    	// Add the nodes:
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
		// graph_weights <- movement_graph.edges as_map (each:: geometry(each).perimeter);
		// movement_graph <- movement_graph with_weights (movement_graph.edges as_map (each:: geometry(each).perimeter));
		movement_graph <-  with_weights (movement_graph, w);
	
		// Create the robots
		create robot number: 1 with: (location: drop_nodes at 0, _color: #green, target: drop_nodes at 3, mg: copy(movement_graph), name: "green");
		create robot number: 1 with: (location: drop_nodes at 3, _color: #red, target: drop_nodes at 0, mg: copy(movement_graph), name: "red");
    }
}

// TODO: Change this to take a list of drop of points and also integrate the more advanced behaviour.
species robot skills: [moving]{
	point target;
	path route;
	rgb _color;
	graph mg;
	string name;
	
	bool flag <- false;

	int last_edge; // pointer in the edge list
	point last;

	init{
		// location <- any(drop_nodes);
		// target <- any(drop_nodes);
	}
	
	aspect base{
		draw circle(2) color: _color;
	}
	
	reflex {
		if (route = nil or location = target){
			if (target = (drop_nodes at 3)){
				target <- (drop_nodes at 0);
			}else{
				target <- (drop_nodes at 3);
			}
			// target <- any(drop_nodes);
			route <- path_between(mg, location, target); // this works
		}
		
		do follow path: route; // alternaively with move_weights: graph_weights;
	}
	
	reflex log {
		// Logs when ever a robot passes an edge
		if (location in drop_nodes){
			write name + " at:" + location;
			last <- location;
		}
		
		if current_edge = nil {return;} // Don't do the check in the case that we are on a ndoe and not an edge
		last_edge <- mg.edges index_of(current_edge);

		// int found <- mg.edges index_of(current_edge);
		
		// write name + " at:" + mg.edges index_of(current_edge);
		// write string(current_edge) + ":" + string(mg.edges at found);
		
		// Update the weight and recal the path
		// list<int> temp <- copy(w);
		// temp[found] <- 500;
		
		
		
		// set temp at found <- 10;
		
//		if (location in movement_graph.edges){
//			write name + " on edge at:" + location;
//			last <- location;
//		}
	}
	
	reflex go_back{
		if (flag){
			route <- path_between(mg, location, last); 
		}
		if(location = last and flag){
			
			graph temp_graph <- copy(mg);
			list<int> temp_weights <- copy(w);
			temp_weights[last_edge] <- 500;
			
			temp_graph <- with_weights (temp_graph, temp_weights);
			route <- path_between(temp_graph, location, target);
			flag <- false;
			
//			// TODO: This is where we recompute the path.
//			write "1."+ weight_of(mg, mg.edges at 0);
//			mg[mg.edges at 0] <- 500;
//			mg[mg.edges at 1] <- 10;
//			mg[mg.edges at 2] <- 10;
//			mg[mg.edges at 3] <- 500;
//			
//			write "2."+ weight_of(mg, mg.edges at 0);
//			mg <- with_weights (mg, [500, 10, 10, 500]);
//			write "3."+ weight_of(mg, mg.edges at 0);
//			route <- path_between(mg, location, target);
//			flag <- false;
		}
	}
	
	reflex re_call{
		
	}
}

experiment MyExperiment type: gui {
	action my_action{
		write #user_location; // Use this to get the mouse location
		
		// movement_graph <- with_weights (movement_graph, [500, 10, 10, 500]);
		
		loop rob over: robot{
			rob.flag <- true;
			// rob.route  <- path_between(rob.mg, rob.location, rob.target);
		}
		
	}
	
	
    output {
		display MyDisplay /*type: 2d*/ {
			event mouse_down action: my_action;
			
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
		    
		    species robot aspect: base;
		}
    }
}