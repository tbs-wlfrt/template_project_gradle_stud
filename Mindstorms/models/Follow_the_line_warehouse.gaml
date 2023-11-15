model follow_the_line_warehouse

global {
	// - Parameters:
	int obstacle_count <- 10;
	int robot_count <- 1;
	int sensor_range <- 20;
	float obstacle_height <- 4.0;
	float obstacle_width <- 4.0;
	
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
	
	// Obstacles
	geometry obstaclesgeom;
	
    init {
    	// TOOD: Find a better way to load this maybe via draw.io or find a way to hack the shp file?? 
    	// 	Else we could try to use the shp of downtown NYC? since it is very rectangular
    	// Add the nodes:
		add point(10,10) to: drop_nodes;
		add point(10,90) to: drop_nodes;
		add point(90,90) to: drop_nodes;
		add point(90,10) to: drop_nodes;
		
		add point(10, 50) to: nodes;
		add point(90, 50) to: nodes;
		
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
		movement_graph <- movement_graph add_edge(drop_nodes at 0::nodes at 0);
		movement_graph <- movement_graph add_edge(drop_nodes at 1::nodes at 0);
		movement_graph <- movement_graph add_edge(drop_nodes at 2::nodes at 1);
		movement_graph <- movement_graph add_edge(drop_nodes at 3::nodes at 1);
		
		movement_graph <- movement_graph add_edge(nodes at 0::triage_nodes at 0);
		movement_graph <- movement_graph add_edge(nodes at 1::triage_nodes at 0);
	
		// Calculate the weight based on the length of the edges so that the robot moves the same speed everywhere.
		// graph_weights <- movement_graph.edges as_map (each:: geometry(each).perimeter);
		movement_graph <- movement_graph with_weights (movement_graph.edges as_map (each:: geometry(each).perimeter));
	
		// Create the robots
		create robot number: robot_count;
		
		// Create the obstacels
		create obstacle number: obstacle_count;
		obstaclesgeom <- union(obstacle collect each.geo);
    }
}

// TODO: Change this to take a list of drop of points and also integrate the more advanced behaviour.
species robot skills: [moving]{
	point target;
	path route;
	
	geometry avoidrect;

	init{
		location <- any(drop_nodes);
	}
	
	aspect base{
		draw circle(2) color: #green;
		
		// Avoidance rectangle
		point r <- point(sin(heading+90), -cos(heading+90));
		avoidrect <- rectangle(2,sensor_range) translated_by point(r*sensor_range/2) rotated_by (heading+90);
		draw avoidrect  color:#orange;
	}
	
	reflex advance{
		if (route = nil or location = target){
			target <- any(drop_nodes);
			route <- path_between(movement_graph, location, target);
		}
		
		do follow path: route; // alternaively with move_weights: graph_weights;
	}
	
	reflex avoid{
		if(avoidrect overlaps obstaclesgeom){
			write "> Obstacle detected!";
			// TODO: Do the actual avoidance here
		}
	}
}


species obstacle {
	rgb color <- rgb (50,20,20);
	geometry geo <- rectangle(obstacle_width , obstacle_height) rotated_by rnd(0, 360);
		
	aspect base {
		draw geo color: color; // TODO: Place the obstacles on the crate?
	}
}

experiment MyExperiment type: gui {
	parameter "Obstacle Count" var: obstacle_count;
	parameter "Robot Count" var: robot_count;
	parameter "Sensor Range" var: sensor_range;
	parameter "Obstacle Width" var: obstacle_width;
	parameter "Obstacle Height" var: obstacle_height;
	
    output {
		display MyDisplay type: 2d {
			// TODO: Find a better way to draw this.
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
		    species obstacle aspect: base;
		}
    }
}