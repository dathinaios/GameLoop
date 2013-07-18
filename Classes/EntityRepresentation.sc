EntityRepresentation { var repManager, >collisionFunc;
	var <position, <radius, entity, attached = false;

	*new { arg repManager, collisionFunc;  
		^super.newCopyArgs(repManager, collisionFunc)
	} 

	init{ var latency;
		/* defaults */
		position = entity.position;
		radius = entity.radius;
		collisionFunc = collisionFunc ?? {{}};
	}

	/* dependant notification from entity */
	update { arg theChanged, message;
		switch (message[0])
		{\preUpdate}
		{ var transPosition;
			transPosition = this.cameraTransform(theChanged); 
		  this.preUpdate(theChanged, transPosition);
		}
		{\update} 
		{position = entity.position; radius = entity.radius}
		{\attach} 
		{this.attach(theChanged) }
		{\remove} 
		{this.remove;}
		{\detach}
		{this.detach(message)}
		{\collision}
		{this.collision(message)};
	} 

	attach{ arg theChanged;
		if (attached.isNil or:{attached.not},
		{ attached = true; 
			this.storeEntity(theChanged);
			this.init; }
		);
	}

	detach{ arg message;
			if (attached and:{message[1] == this}, {attached = false; this.remove;});
	}

	cameraTransform{arg theChanged; 
		if (false, //GameLoop.instance.cameraActive,
			{^(Camera2D.instance.applyTransformation(theChanged)+theChanged.world.center)},
			{^position}
		);
	}

	preUpdate{ arg theChanged, transposition;
		this.subclassResponsibility;
	}

	storeEntity{ arg item;
		entity = item;
	}

	dt{
		^entity.dt;
	}

	remove{
		this.subclassResponsibility;
		repManager.remove(this); 
		attached = false;
	}

}   
