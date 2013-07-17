
Entity {
		var <>world, <>position, <>radius, <>mass;
		var <dt;
		var <colliding, <active;

	*new{ arg world, position = RealVector2D[15,15], radius = 1.0, mass = 1.0;
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
			colliding = false;
			active = false;
	}

	attach{arg rep;
		this.addDependant(rep);
		this.changed(\attach, this)
	}

	detach{arg rep;
		this.changed(\detach);
		this.removeDependant(rep);
	}

	detachAll{
		this.dependants.do{arg i; this.detach(i)};
	}
	
	add{ 
		world.add(this);
		active = true;
		this.changed(\add, this);
	}
	
	remove { 
		world.remove(this);
		active = false;
		this.changed(\remove);
		this.releaseDependants;
	}
	
	collision { arg entitiesArray; 
					colliding = true;
					this.changed(\collision, entitiesArray);
	}

}     

MobileEntity : Entity { var <>velocity, <>collisionType;
												var <>force = 0;

	*new{ arg world, position = RealVector2D[15,15], 
	          radius = 1.0, mass = 1.0, velocity = RealVector2D[0,0], 
	          collisionType = \free;
		  ^super.new(world, 
					 position, 
					 radius, 
					 mass
		  ).velocity_(velocity)
		   .collisionType_(collisionType);
	}

	init{
		super.init;
		velocity = velocity ?? {RealVector2D[0,0]};
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
		this.integrateEuler(force.value(this));
		/* and here we update with the future value in case we want to 
		use it for prediction as in the case of interpolation (lag) of sound 
		units */
		this.changed(\preUpdate);
	}

}

Vehicle : MobileEntity { var <>heading, <>side, <>maxSpeed, <>maxForce, <>maxTurnRate; 
	
	*new{ arg world, position= RealVector2D[15,15], radius = 1.0, mass = 1.0, 
						velocity = RealVector2D[0, 0], collisionType = \free, heading, 
						side, maxSpeed = 100, maxForce = 40, maxTurnRate = 2;
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
