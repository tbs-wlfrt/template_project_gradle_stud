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
	
    init {
    	// Add the nodes:
		add point(10,10) to: drop_nodes;
		add point(10,90) to: drop_nodes;
		add point(90,90) to: drop_nodes;
		add point(90,10) to: drop_nodes;
		add point(40,10) to: drop_nodes;
		add point(40,90) to: drop_nodes;
		add point(60,90) to: drop_nodes;
		add point(60,10) to: drop_nodes;
		
		add point(25, 50) to: nodes;
		add point(75, 50) to: nodes;
		add point(25, 10) to: nodes;
		add point(75, 10) to: nodes;
		add point(25, 90) to: nodes;
		add point(75, 90) to: nodes;
		
		
		add point(50,50) to: triage_nodes;
		
		loop nod over: nodes {
	    	movement_graph <- movement_graph add_node(nod);
		}
		loop nod over: drop_nodes {
	    	movement_graph <- movement_graph add_node(nod);
		}
		loop nod over: triage_nodes {
	    	movement_graph <- movement_graph add_node(nod);
		}
		
		// Add the connections between the nodes
		movement_graph <- movement_graph add_edge(drop_nodes at 0::nodes at 2);
		movement_graph <- movement_graph add_edge(drop_nodes at 1::nodes at 4);
		movement_graph <- movement_graph add_edge(drop_nodes at 2::nodes at 5);
		movement_graph <- movement_graph add_edge(drop_nodes at 3::nodes at 3);
		movement_graph <- movement_graph add_edge(drop_nodes at 4::nodes at 2);
		movement_graph <- movement_graph add_edge(drop_nodes at 5::nodes at 4);
		movement_graph <- movement_graph add_edge(drop_nodes at 6::nodes at 5);
		movement_graph <- movement_graph add_edge(drop_nodes at 7::nodes at 3);
		
		movement_graph <- movement_graph add_edge(nodes at 0::nodes at 4);
		movement_graph <- movement_graph add_edge(nodes at 1::nodes at 3);
		movement_graph <- movement_graph add_edge(nodes at 0::nodes at 2);
		movement_graph <- movement_graph add_edge(nodes at 1::nodes at 5);
		
		movement_graph <- movement_graph add_edge(nodes at 0::triage_nodes at 0);
		movement_graph <- movement_graph add_edge(nodes at 1::triage_nodes at 0);
	
		// Calculate the weight based on the length of the edges so that the robot moves the same speed everywhere.
		// graph_weights <- movement_graph.edges as_map (each:: geometry(each).perimeter);
		movement_graph <- movement_graph with_weights (movement_graph.edges as_map (each:: geometry(each).perimeter));
	
		// Create the robots
		create robot number: 2;
    }
}

// TODO: Change this to take a list of drop of points and also integrate the more advanced behaviour.
species robot skills: [moving]{
	point target;
	path route;

	init{
		location <- any(drop_nodes);
		target <- any(drop_nodes);
	}
	
	aspect base{
		draw circle(2) color: #green;
	}
	
	reflex {
		if (route = nil or location = target){
			target <- any(drop_nodes);
			route <- path_between(movement_graph, location, point(30, 30)); // this works
		}
		// Experiement here to show if the updated weight is considered on the go.
		
		
		do follow path: route; // alternaively with move_weights: graph_weights;
	}
}

experiment MyExperiment type: gui {
    output {
		display MyDisplay type: 2d {
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