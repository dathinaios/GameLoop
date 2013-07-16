EntityRepresentation { var entity, repManager, <position, <radius;

	*new { arg  entity, repManager;  
		^super.newCopyArgs(entity, repManager)
	} 

	init{ var latency;
		/* defaults */
		position = entity.position;
		radius = entity.radius;
		entity.addDependant(this);
	}

	/* dependant notification from entity */
	update { arg entity, message;

		switch (message)//a typical use of a .changed notification (could be case for multiple)
		{\update} {position = entity.position; radius = entity.radius}
		{\remove} {this.remove};
		/* 
		in subclass you can use: 

		super.update;
		switch (message) 
		{\remove} {this.remove}
 		{message == \collision} {this.collision};

 		*/
	} 

	remove{
		repManager.remove(this);
	}
}   
