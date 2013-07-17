
/*
	This is a basic Mobile Unit with varying sound input
*/

SimpleCircleRep : EntityRepresentation { 

	var >collisionFunc, >color, >collisionColor;
	var <penWidth = 1.5;
							
	*new { arg  repManager, collisionFunc, 
							color, collisionColor;  
		^super.new(repManager)
					.collisionFunc_(collisionFunc) 
					.color_(color) 
					.collisionColor_(collisionColor);
	}

	init { 

		super.init;
		collisionFunc = collisionFunc ?? {{}};

		color = color ?? {Color.green};
		collisionColor = collisionColor ?? {Color.red};
		this.play;

	}

	play {
			if (entity.active.not){entity.add};
			repManager.add(this);
	}

	color { if(this.colliding, {^collisionColor },{^color})
	}

	update {arg theChanged, message; /* entity is the changer */
					var transPosition; 

		/*first for the standard update from the superclass that gets the new 
			position and velocity paramaters*/
		super.update(theChanged, message);
		
		/* here add any additional functionality */
		switch (message[0]) 

		/* NOTE: set the speed of the NodeProxy *after* the integration to 
		account for the lag (interpolation) */
		{\preUpdate}
		{
			/* transform the position according to the camera position. */
			if (GameLoop.instance.cameraActive,
				{transPosition = Camera2D.instance.applyTransformation(theChanged)+theChanged.world.center},
				{transPosition = position}
			);
		}

		{\collision}{
			/* message should be a list with the colliding with entities*/
			collisionFunc.value(this, message);
		};

	}
	
	remove{
			repManager.remove(this);
			attached = false;
	}

	draw{arg rect; 
		Pen.strokeOval(rect)
	}
}   

