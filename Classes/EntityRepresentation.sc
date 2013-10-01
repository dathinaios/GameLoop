EntityRepresentation { var repManager, <>collisionFunc;
	var <position, <radius, <speed, entity, attached = false;
	var <>type = 'sound';

	*new { arg repManager, collisionFunc;  
		^super.newCopyArgs(repManager, collisionFunc)
	} 

	init{ var latency;
		/* initialize data */
		this.getData;
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
		{this.getData}
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
		if (Camera2D.active,
			{^(Camera2D.instance.applyTransformation(theChanged))},
			{^position}
		);
	}

	preUpdate{ arg theChanged, transposition;
		position = transposition + theChanged.world.center;
	}

	getData{
		position = entity.position - entity.world.center;
		radius = entity.radius;
		speed = entity.velocity.norm;
	}

	addEntity{
		if (entity.active.not){entity.add};
	}

	storeEntity{ arg item;
		entity = item;
	}

	colliding{
		^entity.colliding;
	}

	dt{
		^entity.dt;
	}

	add{
		this.subclassResponsibility(thisMethod);
	}

	remove{
		repManager.remove(this); 
		attached = false;
	}

	collision{ arg message;
		/* message should have a list at [1] with the colliding with entities*/
		collisionFunc.value(entity, message[1]);
	}

}   
