/*
	TODO page 69 of Ai book. Event handling for communication between objects.
	TODO With the event based  thingy that is described in the AI book I think that an object can anticipate its collision. That could work really nicely for modulating the sound. ??I do not remember what I mean??
*/

Entity {
		var <>world, <>position, <>radius, <>mass;
		var <dt, <id; //we Do not need the param reps anymore since we are using the dependants mechanism
		var <>colliding;
		var <>collisionType;

	*new{ arg world, position, radius, mass;
		  ^super.newCopyArgs(world, 
		  					 position, 
		  					 radius, 
		  				     mass
		  );
	}

	//The init method is called in the subclass by using super.init. Using super.init
	//in all the init methods assures that everything will be called
	init{ 	
		 	//"enity init".postln;
			position = position ?? {world.center};
			radius = radius ?? {1.0}; 
			mass = mass ?? {1.0};
			//Now intialise some extra variables
			dt = world.dt;
			//define a unique id for the entity
			id = UniqueID.next;
			//set colliding to false for start
			colliding = false;
			//Main.elapsedTime.debug("I'm alive!!")
			collisionType = \free;
	}
	
	add{ world.add(this);
	   //Main.elapsedTime.debug("I'm alive!!")
	}
	
	remove { world.remove(this);
			 this.changed(\remove);
			 //this.release; //release all dependants; I do not need that becasue the depndants release themselves
	}
	
	attach { arg dependant;
			this.addDependant(dependant);
	}
	
	//the message that is send once a collision is detected
	
	collision { arg entitiesArray; 
			this.subclassResponsibility(thisMethod);
	}
	
	activate { //this method is the last to be called. It will add the entity and the representations to their managers
		this.add;
		//this.changed(\add); //this change should be dealt in the representation to activate them//reps.do{arg i; i.activate};
		//this.dependants.do{arg i; i.activate};
	}

}     

MobileEntity : Entity { var <>velocity, <>controller;

	*new{ arg world, position, radius, mass, velocity, controller;
		  ^super.new(world, 
					 position, 
					 radius, 
					 mass
		  ).velocity_(velocity)
		   .controller_(controller);
	}

	init{
		//"mobile init".postln;
		super.init;
		velocity = velocity ?? {RealVector2D[0,0]};
		//choose the right controller by adding "controller" to the base name of the class
		controller = controller ?? {(this.class.asString++"Controller").asSymbol.asClass.new(this)};
	}

	integrateEuler{ arg force = 0;
		velocity = velocity + ((force/mass) * dt);
			//Main.elapsedTime.postln;
		position = position + (velocity *dt);
			//position.debug("position");
		//t = t + dt;
	}
	
	//implement update in subclass if needed
	//Typical:
	
	update {
		this.integrateEuler(controller.getForce(this));
		this.changed(\update);
		//this.updateReps; //update all active views
	}
	
	//updateReps{ //update all views (notify them to collect the data from the entity)
	//	reps.do{arg  i; i.update};
	//}
	
//	addForce { arg force; 
//			controller.force_(force)
//	}

}


Vehicle : MobileEntity { var <>heading, <>side, <>maxSpeed, <>maxForce, <>maxTurnRate; 
					  	 //var steering;
	
	*new{ arg world, position, radius, mass, velocity, controller,
			  heading, side, maxSpeed, maxForce, maxTurnRate;
		  ^super.new(world, 
					 position, 
					 radius, 
					 mass
		  ).velocity_(velocity)
		   .controller_(controller)
		   .heading_(heading)
		   .side_(side)
		   .maxSpeed_(maxSpeed)
		   .maxForce_(maxForce)
		   .maxTurnRate_(maxTurnRate);
	}
	
	init{
		//"vehicle init".postln;
		super.init;
		maxSpeed = maxSpeed ?? {100};
		maxForce = maxForce ?? {40};
		maxTurnRate = maxTurnRate ?? {2};
	}

	integrateEuler{ arg force = 0; //TODO: Do I need an accelaration variable?
		//calculate velocity
		velocity = velocity + ((force/mass) * dt);
		//trancate the velocity to the maximum allowed
		velocity = velocity.limit(maxSpeed);
		//t = t + dt;
		//update the position
		position = position + (velocity * dt);
		// update the heading and side (only if velocity is greater than *from AI by example book*)
		if (velocity.magSq > 0.00000001)
			{
			heading = velocity.normalize;//.debug('heading');
			side = heading.perp;
			};
		this.changed(\update)
	}

	/*
	update { //arg force;
		//force = steering.calculate;
		this.integrateEuler(controller.getForce(this));
		this.changed(\update);
		//"update inside vehicle".postln;
		//this.position.postln;
	}
	*/

}