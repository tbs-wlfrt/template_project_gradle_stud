/**
* Name: v1
* Based on the internal empty template. 
* Author: lab25
* Tags: 
*/


model v1

global {
	int instance_count <- 1;
	int rotation <- 120;
	geometry cratesgeom;
	init {
		create robot number: instance_count;
		create crate number: 20;
		cratesgeom <- union(crate collect each.geo);
	}
}

species robot {
	float size <- 1.0;
	rgb color <- #black;
	int _rotation;
	geometry rect;
//	float rotation <- 120.0;  // storingt he angle of the sprite representing the robot.
	
	init {
		location <- point(50,50);
		_rotation <- rotation;
	}
	
	aspect base {
		point r <- point(sin(_rotation), -cos(_rotation));
		rect <- (rectangle(2,80) translated_by point(r*40)) rotated_by _rotation;
		draw rect  color:#yellow;
		draw triangle(10) rotated_by _rotation color: color;
		
	}
	
	reflex basic_move {
		// todo: do more here
//		rotation <- rotation + 10;
//		location <- location + point(1);
	}
	
//	reflex move_forward{
//		location <- location + point(sin(_rotation), -cos(_rotation));
//	}
	
	reflex object_detected when: overlaps(rect, cratesgeom){
		write string("object seen") color:#black;
	}
	
	reflex speeeeen{
		_rotation <- (_rotation + 10) mod 359;
	}
	
//	reflex move_backward{
//		location <- location - point(sin(_rotation), -cos(_rotation));
//	}
	
	
}

species crate {
	float size <- 1.0;
	rgb color <- #brown;
	geometry geo <- square(3);
	
	init {
//		location <- point(50,35);
	}
	
	aspect base {
		draw geo color: color;
		
	}
}



// something that is not a grid world

experiment move_predifened_path type: gui {
	parameter "rotation" var: rotation min:0 max:359 ;
	output {
		display main_display type: 2d background: #grey{
			species robot aspect: base;
			species crate aspect: base;
		}
	}
}

/* Insert your model definition here */

