

SimpleCircleEnt : Vehicle { var  >collisionFunc, forceFunc;
	

	*new{ arg world, position, radius, mass, velocity, controller,
			  heading, side, maxSpeed, maxForce, maxTurnRate, collisionFunc, forceFunc;
		  ^super.newCopyArgs(world, 
		  					 position, 
		  					 radius, 
		  				     mass,
		  				     velocity,
		  				     controller,
		  				     heading,
		  				     side,
		  				     maxSpeed,
		  				     maxForce,
		  				     maxTurnRate,
		  				     collisionFunc,
		  				     forceFunc
		  ).initHook1.init.initHook2;		

	}

	init{ var path;
		super.init;
		collisionFunc = collisionFunc ?? {{}};
		path = Path(Array.fill(rrand(8.0, 20.0),{RealVector[position[0] + rrand(-1, 1.0), position[1] + rrand(-1, 1.0)]}),true);
		controller = Controller(this, 
				{ arg entity;
				  PathFollowing.calculate(entity,path, 0.5)}
		);
	}

	collision { arg entList; colliding = true;
				collisionFunc.value(entList, this);
	}
}

SimpleCircleRep : EntityRepresentation { var color, collisionColor;
										 var <penWidth = 1.5;
							   	   		 var <position, <radius;
								   
							
	*new { arg  entity, color, collisionColor;  
	^super.newCopyArgs(entity, color, collisionColor).init
	}

	init { 
		position = entity.position;
		radius = entity.radius;
		color = color ?? {Color.green};
		collisionColor = collisionColor ?? {Color.red};
	}
	
	color { if(entity.colliding, {^collisionColor },{^color})
	}

	update { arg entity, message; 
		//first fo the standard update from the superclass
		super.update(entity, message);
		//here add anyadditional functionality
		message.debug("update method in simple circle - message = ");
		/*
		switch 
		{message == \remove} {"removal update".postln; this.remove};
		*/
	}
	
	draw{arg rect; Pen.strokeOval(rect)}
	
	setFR{}
	
}   
