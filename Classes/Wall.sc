
Wall { var <>from, <> to, <>normal;

	*new{ arg from, to;
		^super.newCopyArgs(from,  to).init;
	}

	init{
		normal = this.calculateNormal;
	}

	calculateNormal{ var dx, dy;
		/* if we define dx=x2-x1 and dy=y2-y1, then the normals are (-dy, dx) and (dy, -dx). */
		dx =  to[0] - from[0];
		dy =  to[1] - from[1];
		/* the normal to the left of the vector (considering from and to) */
		^RealVector2D[-1 * dy, dx].normalize;
	}

	/* impementation from http://doswa.com/2009/07/13/circle-segment-intersectioncollision.html */
	closestPointOnWall{ 
		arg entityPosition; 
		var segv, ptv, segvunit, proj, projv, closest;

		segv = to - from;
		ptv = entityPosition - from;

		if(segv.norm <= 0,
			{ "Invalid segment length".error;  },
			{ segvunit = segv / segv.norm }
		);

		//can I use here the proj method instead of doing it manually?
		proj = ptv <|> segvunit; //dot product

		if ( proj <= 0 ) { ^from}; 

		if ( proj >= segv.norm) { ^to};

		projv = segvunit * proj;
		closest = projv + from;
		^closest;
	}

	checkCircleWallCollision{ 
		arg entity;
		var closest, distv, circpos, circrad, offset;

		circpos = entity.position;
		circrad = entity.radius;

		closest = this.closestPointOnWall(circpos);
		distv = circpos - closest;

		if ( distv.norm > circrad) {
			^RealVector2D[0, 0]
		};

		if ( distv.norm <= 0) {
			"Circle's center is exactly on segment".error;
		};

		offset = distv / distv.norm * (circrad - distv.norm)
		^offset;
	}

}

