



EntityRepresentation { var entity, entityParams;

	*new { arg  entity, entityParams;  
	^super.newCopyArgs(entity, entityParams)
	} 


	update { arg entity, message; // the dependancy passes in the changed and any additional arguments // (will use symbols for that and a switch statement) to respond in different ways.

		this.getData;
	} 
	
	//the method to collect relevant data from the entity
	getData{ "getData should be implemented in subclass".error;
	}
	
	
	run { // method to run the representaion without adding to the manager.
		  // Usefull for compensating for the bundle time.
		"implement run in subclass".error;
	}

	add{ RepresentationManager.add(this);
			//Main.elapsedTime.debug("I'm alive!!")
	}
	
	remove {
		RepresentationManager.remove(this);
	}

	activate { // this method adds the rep to the manager but does not
			   // run the representation.
	
		this.add;
	
	}
	activateRun{ // this method adds the rep to the manager and then calls run
			 // run should be implemented in the subclass and will contain 
			 // all the things relevenat to the particular representation
			 
		this.add;
		this.run;
	}
}   
