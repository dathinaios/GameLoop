/*
	TODO page 69 of Ai book. Event handling for communication between objects.
	TODO If I get the variables names in an array and then iterate over that and get the values wouldn't it be more efficient than iterating over the whole entityParams Dictionary?
	TODO The represtnation manager is used only for the visuals. That does not make sense in terms of the overall design.
	TODO Use RealVector
	TODO With the event based  thingy that is described in the AI book I think that an object can anticipate its collision. That could work really nicely for modulating the sound.
*/

Entity {
		var <>world, <>position, <>radius, <>mass;
		var <dt, <id; //we Do not need the param reps anymore since we are using the dependants mechanism
		var <>colliding;

	*new{ arg world, position, radius, mass;
		  ^super.newCopyArgs(world, 
		  					 position, 
		  					 radius, 
		  				     mass
		  ).initHook1.init.initHook2;		
	}

	init{ 	
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
	}

	initHook1{
		//for particular initialisations in a subclass
		//before all the other inits
	}
	
	initHook2{
		//for particular initialisations in a subclass
		//after all the other inits
	}
	
	//overide the add method in a subclass if you want to use 
	//EntityManager2
	add{ world.add(this);
	   //Main.elapsedTime.debug("I'm alive!!")
	}
	
	remove { world.remove(this);
			 this.changed(\remove);
			 this.release; //release all dependants;
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
		this.changed(\add); //this change should be dealt in the representation to activate them//reps.do{arg i; i.activate};
		//this.dependants.do{arg i; i.activate};
	}

}     

MobileEntity : Entity { var <>velocity, <>controller;

	*new{ arg world, position, radius, mass, velocity, controller;
		  ^super.newCopyArgs(world, 
		  					 position, 
		  					 radius, 
		  				     mass,
		  				     velocity,
		  				     controller
		  ).initHook1.init.initHook2;		
	}

	init{
		super.init;
		velocity = velocity ?? {RealVector[0,0]};
		//choose the right controller by adding the "controller" to the base name of the class
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
		  				     maxTurnRate
		  ).initHook1.init.initHook2;		
	}
	
	init{
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
			heading = velocity.normalize; //.debug('heading');
			side = heading.perp;
			};
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
