
Wall { var <>from, <> to, <>normal;

	*new{ arg from, to;
		^super.newCopyArgs(from,  to).init;
	}

	init{
		normal = this.calculateNormal;
		WallManager.add(this);
	}

	remove{
		WallManager.remove(this)
	}

	calculateNormal{ var dx, dy;
		/* if we define dx=x2-x1 and dy=y2-y1, then the normals are (-dy, dx) and (dy, -dx). */
		dx =  to[0] - from[0];
		dy =  to[1] - from[1];
		/* the normal to the left of the vector (considering from and to) */
		^RealVector2D[-1 * dy, dx].normalize;
	}

}

/* the manager is not currently used */
WallManager{ classvar <walls;

	*new{ "You can not have an instance of this".error;
	}

	*initClass{
		walls = List.new;
	}
	
	*add{ arg wall; 
		walls = walls.add(wall);
	}

	*remove{ arg wall;
		this.remove(wall);
	}
	
	*clear{ walls.clear;
	}

}
