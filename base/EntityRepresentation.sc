EntityRepresentation { var entity, <position, <radius;

	*new { arg  entity;  
	^super.newCopyArgs(entity)
	} 

	// the dependancy passes in the changed and any additional arguments 
	update { arg entity, message; 
		//in the subclass call super.update to do the above and then add your own like:
		switch (message)//a typical use of a .changed notification (could be case for multiple)
		{\update} {position = entity.position; radius = entity.radius}
		{\add} {entity.world.repManager.add(this)}
		{\remove} {entity.world.repManager.remove(this)};
		//in subclass put: {message == \collision} {this.collision};
	} 
	
	run { // method to run the representaion without adding to the manager.
		  // Usefull for compensating for the bundle time.
		"implement run in subclass".error;
	}
	/*
	add{ RepresentationManager.add(this);
			//Main.elapsedTime.debug("I'm alive!!")
	}
	
	remove {
		RepresentationManager.remove(this);
	}
	*/

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
