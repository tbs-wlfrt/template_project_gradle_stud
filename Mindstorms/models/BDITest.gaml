/**
* Name: BDITest
* Based on the internal empty template. 
* Author: peter
* Tags: 
*/


//DESIRES/INTENTIONS
// reach location
//    move forwards
//    rotate towards location
//       rotate left
//       rotate right

//BELIEFS
// current location
// previous location
// crate location
// orientation					(computed using the line that the current and previous location both lie on)
//   facing crate
//   not facing crate
//       facing left of crate
//       facing right of crate
	
//RULES (in priority order)
// if there is an obstacle in the way, avoid obstacle
// if the agent is currently carrying a crate, go to drop off point
// if there is a crate to collect, go to crate

//PLANS
// avoid obstacle
// go to location


model BDITest

/* Insert your model definition here */
global{
	string crate_at_location <- "crate_at_location";
	
	predicate crate_location <- new_predicate(crate_at_location);
	predicate has_crate <- new_predicate("has crate");
	predicate dropped_off_crate <- new_predicate("dropped off crate");
	predicate select_crate <- new_predicate("select crate");
	predicate rotate_towards <- new_predicate("rotating towards");
	
	int instance_count <- 1;
	int obstacle_count <- 10;
	int crate_count <- 10;
	
	
	int rotation <- 120;
	geometry cratesgeom;
	geometry obstaclesgeom;
	
	init {
		create robot number: instance_count;
		create crate number: crate_count;
		create obstacle number: obstacle_count;
		create drop_off_point number: 1;
		cratesgeom <- union(crate collect each.geo);
		obstaclesgeom <- union(obstacle collect each.geo);
	}
}

species robot skills: [moving] control: simple_bdi{
	point target;
	float view_dist <- 1000.0;
	float speed <- 2.0;
	int crates_visited <- 0;
	
	float size <- 1.0;
	rgb color <- #black;
	geometry rect;
	geometry avoidrect;
	
	init {
		location <- point(50,50);
		heading <- rotation;
		do add_desire(has_crate);
	}

	aspect base {
		point r <- point(sin(heading+90), -cos(heading+90));
		int rectlen <- 80;
		int avoidrectlen <- 20;
		rect <- (rectangle(2,rectlen) translated_by point(r*rectlen/2)) rotated_by (heading+90);
		draw rect  color:#yellow;
		avoidrect <- rectangle(2,avoidrectlen) translated_by point(r*avoidrectlen/2) rotated_by (heading+90);
		draw avoidrect  color:#orange;
		draw triangle(10) rotated_by (heading+90) color: color;
	}
	

	rule belief: crate_location new_desire: has_crate strength: 2.0;
	rule belief: has_crate new_desire: dropped_off_crate strength: 3.0;
	rule belief: dropped_off_crate new_desire: has_crate strength: 2.0;
	
	action avoid{
		if(obstaclesgeom = nil){
			return; // TODO why is this not nil for collect_crates experiment?
		}
		// TODO maybe make this a plan as well; haven't found async plan execution though :/
		write "avoiding obstacle";
		if(avoidrect overlaps obstaclesgeom){
			write "> Obstacle detected!";
		}
	}
	
	plan go_to_crate intention: has_crate {
		//select crate if no target, go to target otherwise
		if (target = nil) {
			do add_subintention(get_current_intention(), select_crate, true);
			do current_intention_on_hold();
		}
		else {
			do move speed: speed heading: heading;
			do avoid;
			float dist <- check_distance(target, location);
			if (dist < 5) {
				do add_belief(has_crate);
				target <- nil;
				
			}
		}
	}
		
	action rotate{
		if (target != nil){
			point d <- target - location;
			float angle <- atan2(d.y, d.x);
			heading <- angle; 
		}
	}
	
	plan choose_crate intention: select_crate instantaneous: true {
		list<point> crate_locations <- crate collect each.location;
		target <- crate_locations at crates_visited;
		write "target was set to: " + target;
		do rotate;
		do remove_intention(select_crate, true);
	}
	
	
	plan deliver_crate intention: dropped_off_crate {
		if (target = nil) {
			target <- drop_off_point collect each.location at 0;
			do rotate;
//			do add_subintention(get_current_intention(), rotate_towards, true);
//			do current_intention_on_hold();
		}
		else {
			do move speed: speed heading: heading;
			do avoid;
			float dist <- check_distance(target, location);
			if (dist < 5) {
				do remove_belief(has_crate);
				crates_visited <- crates_visited + 1;
				target <- nil;
				do add_belief(dropped_off_crate);
				
			}
		}
	}	
	
	float check_distance(point p0, point p1){
		float diff <- abs(p0.x - p1.x);
		diff <- diff + abs(p0.y - p1.y);
		return diff;
	}

}

species crate {
	float size <- 3.0;
	rgb color <- #brown;
	geometry geo <- square(size);
	bool select;
	
	init {
		//location <- point(50,35);
		select <- true;
	}
	
	aspect base {
		draw geo color: color;
		
	}
}

species obstacle {
	float width <- 2.0;
	float height <- 7.0;
	rgb color <- rgb (255,0,0);
	geometry geo <- rectangle(width , height) rotated_by rnd(0, 360);
		
	aspect base {
		draw geo color: color;
	}
}

species drop_off_point {
	float size <- 10.0;
	rgb color <- #green;
	geometry geo <- square(size);
	bool select;
	
	init {
		//location <- point(50,35);
	}
	
	aspect base {
		draw geo color: color;
		
	}
}


experiment collect_crates type: gui {
	parameter "rotation" var: rotation min:0 max:359 ;
	parameter "obstacle_count" var: obstacle_count;
	parameter "crate count" var: crate_count;
	output {
		display main_display type: 2d background: #grey{
			species robot aspect: base;
			species crate aspect: base;
			species drop_off_point aspect: base;
			
		}
	}
}

experiment avoid_obstacle type: gui{
		parameter "rotation" var: rotation min:0 max:359 ;
		parameter "obstacle_count" var: obstacle_count;
		parameter "crate count" var: crate_count;
		output {
			display main_display type: 2d background: #grey{
			species robot aspect: base;
			species crate aspect: base;
			species obstacle aspect: base;
			species drop_off_point aspect: base;
		}
	}
}
