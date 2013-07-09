
Entity {
		var <>world, <>position, <>radius, <>mass;
		var <dt, <id; 
		var <>colliding;

	*new{ arg world, position, radius, mass;
		  ^super.newCopyArgs(world, 
												position, 
												radius, 
												mass
		  );
	}

	/* The init method is called in the subclass by using super.init. Using super.init
	in all the init methods assures that everything will be called. Of course remember 
	to call init in the subclass new method to start the domino effect */
	init{ 	
			position = position ?? {world.center};
			radius = radius ?? {1.0}; 
			mass = mass ?? {1.0};
			dt = world.dt;
			id = UniqueID.next;
			colliding = false;
			this.prepare;
	}
	
	prepare{
		world.prepare(this);
	}

	add{ world.add(this);
	}
	
	remove { world.remove(this);
			 this.changed(\remove);
	}
	
	attach { arg dependant;
			this.addDependant(dependant);
	}
	
	//the message send once a collision is detected
	collision { arg entitiesArray; 
			this.subclassResponsibility(thisMethod);
	}

}     

MobileEntity : Entity { var <>velocity, <>controller, <>collisionType;

	*new{ arg world, position, radius, mass, velocity, controller, collisionType;
		  ^super.new(world, 
					 position, 
					 radius, 
					 mass
		  ).velocity_(velocity)
		   .controller_(controller)
		   .collisionType_(collisionType);
	}

	init{
		super.init;
		velocity = velocity ?? {RealVector2D[0,0]};
		//choose the right controller by adding "controller" to the base name of the class
		controller = controller ?? {(this.class.asString++"Controller").asSymbol.asClass.new(this)};
		collisionType  = collisionType ?? {\free};
	}

	integrateEuler{ arg force = 0;
		velocity = velocity + ((force/mass) * dt);
		position = position + (velocity *dt);
	}
	
	//implement update in subclass if needed
	//Typical:
	update {
		/* calling update on the dependants ensure that we always get set
		by the integration of the last cycle */
		this.changed(\update);
		this.integrateEuler(controller.getForce(this));
		/* and here we update with the future value in case we want to 
		use it for prediction as in the case of interpolation (lag) of sound 
		units */
		this.changed(\preUpdate);
	}

}

Vehicle : MobileEntity { var <>heading, <>side, <>maxSpeed, <>maxForce, <>maxTurnRate; 
	
	*new{ arg world, position, radius, mass, velocity, controller, collisionType,
			  heading, side, maxSpeed, maxForce, maxTurnRate;
		  ^super.new(world, 
					 position, 
					 radius, 
					 mass
		  ).velocity_(velocity)
		   .controller_(controller)
		   .collisionType_(collisionType)
		   .heading_(heading)
		   .side_(side)
		   .maxSpeed_(maxSpeed)
		   .maxForce_(maxForce)
		   .maxTurnRate_(maxTurnRate);
	}
	
	init{
		super.init;
		maxSpeed = maxSpeed ?? {100};
		maxForce = maxForce ?? {40};
		maxTurnRate = maxTurnRate ?? {2};
	}

	integrateEuler{ arg force = 0; 
		velocity = velocity + ((force/mass) * dt);
		velocity = velocity.limit(maxSpeed);
		position = position + (velocity * dt);
		// update the heading and side (only if velocity is greater than *from AI by example book*)
		if (velocity.magSq > 0.00000001)
			{
			heading = velocity.normalize;
			side = heading.perp;
			};
	}

}
