EntityRepresentation { var repManager;
	var <position, <radius, entity, attached = false;

	*new { arg repManager;  
		^super.newCopyArgs(repManager)
	} 

	init{ var latency;
		/* defaults */
		position = entity.position;
		radius = entity.radius;
	}

	/* dependant notification from entity */
	update { arg ent, message;
		switch (message)
		{\update} 
		{position = entity.position; radius = entity.radius}
		{\remove} 
		{this.remove;}
		{\attach} 
		{
			if (attached.isNil or:{attached.not},
					{ attached = true; 
						entity = ent; 
						this.init; }
					/* { "You can only have one model per view (but many views per model)".error; } */
			)
		}
		{\detach}
		{
			if (attached, {attached = false; this.remove; });
		};

		/* 
		in subclass you can use: 
		super.update;
		switch (message) 
		{\remove} {this.remove}
 		{message == \collision} {this.collision};
 		*/
	} 

	remove{
		this.subclassResponsibility;
		repManager.remove(this); 
		attached = false;
	}
}   
