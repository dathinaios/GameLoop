
Camera2D : Vehicle { classvar <>fwd, <>back, <>rotLeft, <>rotRight, <>instance;
										 var <>arrivePosition, <>rotation, <>friction = 0.6,
										 >motionAmount = 30, >rotationAmount= 0.01pi;


	*new{ arg world, position= RealVector2D[15,15], radius = 1.0, mass = 0.05, 
				velocity = RealVector2D[0, 0], collisionType = \free, heading, 
				side, maxSpeed = 3.4, maxForce = 10, maxTurnRate = 2;

			if(instance.isNil, 
				{
					^super.new(world, 
						position, 
						radius, 
						mass
					).velocity_(velocity)
					.collisionType_(collisionType)
					.heading_(heading)
					.side_(side)
					.maxSpeed_(maxSpeed)
					.maxForce_(maxForce)
					.maxTurnRate_(maxTurnRate);
				},
				{"There is already an active instance of Camera2D".error;}
			);		
	}

	*active{
		if(instance.isNil,
			{^false},
			{^true}
		);
	}

	init{
		super.init;
		instance = this;
		arrivePosition = position;
		rotation = 0;
	}

	remove { arg confirm = false;
		if (confirm)
		{
			super.remove;
			instance = nil;
		};
	}

	/*There were issues with the transformation. I was using the heading of the camera entity in a weird manner.
	what I did here instead is define a new var for rotation. It takes a value from 0 to 2pi and works as expected.
	The only issue is that although the motion left righ is smooth the rotation is not.*/

	applyTransformation{ arg entity; 
						var entPos, newPos, theta, thetaSin, thetaCos, rad, x,y;
						var xMinusx, yMinusy;
		//translate position according to rotation and camera position	
		entPos = entity.position;
		//theta = rotation; //.norm; //.debug("theta");
		thetaSin = rotation.sin;
		thetaCos = rotation.cos;
		xMinusx = entPos[0] - position[0];
		yMinusy = entPos[1] - position[1];
		x = (xMinusx * thetaCos) - (yMinusy * thetaSin);
		y = (xMinusx * thetaSin) + (yMinusy * thetaCos);
		^RealVector2D[x,y];
	}

	moveFwd{var theta, x, y;
		theta = rotation;
		y = theta.cos;
		x = theta.sin;
		arrivePosition = arrivePosition + (motionAmount *RealVector2D[x, y]);
		this.goto(arrivePosition);
	}

	moveBack{var theta, x, y;
		theta = rotation;
		y = theta.cos;
		x = theta.sin;
		arrivePosition = arrivePosition - (motionAmount *RealVector2D[x, y]);
		this.goto(arrivePosition);
	}

	forceFwd{ var theta, x, y;
		theta = rotation;
		y = theta.cos;
		x = theta.sin;
		arrivePosition = (motionAmount *RealVector2D[x, y]);
		this.velocity = arrivePosition;
		this.force_({arg entity; entity.velocity = entity.velocity * friction; 0});
	}

	forceBack{var theta, x, y;
		theta = rotation;
		y = theta.cos;
		x = theta.sin;
		arrivePosition = -1 * (motionAmount * RealVector2D[x, y]);
		this.velocity = arrivePosition;
		this.force_({arg entity; entity.velocity = entity.velocity * friction; 0})
	}

	rotateLeft{
		rotation = (rotation - rotationAmount).wrap(0, 2pi);
	}

	rotateRight{
		rotation = (rotation + rotationAmount).wrap(0, 2pi);
	}

	goto{ arg target;
		arrivePosition = target;
		this.force_({arg ent; Arrive.calculate(ent, arrivePosition)}); 
	}

	reset{
		arrivePosition = world.center;
		rotation = 0;
		this.goto(arrivePosition);
	}

}

Camera2DRepresentation : SimpleVisual{

	position{
		^entity.world.center;
	}
}
