
/*
	This is a basic Mobile Unit with varying sound input
*/

SoundRepresentation : EntityRepresentation { 

	var >input, >collisionFunc, >release = 0.2, >color, >collisionColor;
	var <penWidth = 1.5;
	var encoder, <encoderProxy, decoderBus, <encoderProxyIndex;
							
	*new { arg  repManager, input, collisionFunc, 
							release, color, collisionColor;  
		^super.new(repManager)
					.input_(input) 
					.collisionFunc_(collisionFunc) 
					.release_(release) 
					.color_(color) 
					.collisionColor_(collisionColor);
	}

	init { 

		super.init;
		collisionFunc = collisionFunc ?? {{}};
		release = release ?? {0.2};

		color = color ?? {Color.green};
		collisionColor = collisionColor ?? {Color.red};

		/* decoder init */
		this.initializeDecoder;

		/* make some sound */
		this.play;

	}

	initializeDecoder{
		encoder = GameLoopDecoder.getEncoder;
		/* get the right encoder from the GameLoopDecoder class */
		encoderProxy = GameLoopDecoder.getEncoderProxy;
		encoderProxy.clock = TempoClock.default;
		/* plug the proxy to the decoder summing bus */
		decoderBus = GameLoopDecoder.decoderBus;
		/* Always put the new Node in an extra slot of the Summing nodeRpoxy */
		encoderProxyIndex = decoderBus.sources.size - 1;
		decoderBus.put(encoderProxyIndex, encoderProxy);
	}
	
	play {
		var latency;
		latency = Server.default.latency;
		Routine{
			this.addSource;
			/* wait for 'latency' before adding to managers so that everything is in sync. */
			if(latency.notNil) {latency.wait};
			/* Add everything at exactly the same time as the bundle */
			if (entity.active.not){entity.add};
			repManager.add(this);
		}.play;
	}

	addSource{
			encoderProxy.source = { arg dt;
				var x , y;
				var rad, azim, elev, in, speed;

				dt = this.dt;

				/* Ramp is used to interpolate between updates */
				#x, y = Control.names(#[x, y]).kr([position[0], position[1]]);
				x = Ramp.kr(x, dt);
				y = Ramp.kr(y, dt);

				speed = Control.names(\speed).kr(entity.velocity.norm);
				speed = Ramp.kr(speed, dt); 

				/* play default if input is not supplied */
				if(input == nil,
					{
						in = Impulse.ar(speed.linlin(0,10, 5, rrand(50, 200.0)));
						in = BPF.ar(in, rrand(2000, 18000.0)*rrand(0.3, 2.0), 0.4);
					},
					{in = input.value(speed)}
				);

				/* calculate azimuth and radius */
				azim = atan2(y,x);
				rad = hypot(x,y);
				elev = 0;

				/* get and use the relevant encoder */
				encoder.ar(
					in, 
					azim, 
					rad, 
					elev: elev, 
					ampCenter: 0.9
				);
			};
	}

	color { if(entity.colliding, {^collisionColor },{^color})
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
			encoderProxy.set('speed',entity.velocity.norm);
			/* transform the position according to the camera position. */
			if (GameLoop.instance.cameraActive,
				{transPosition = Camera2D.instance.applyTransformation(theChanged)+theChanged.world.center},
				{transPosition = position}
			);
			/* set the syth with the new position values */
			encoderProxy.set('x', transPosition[0]-20);
			encoderProxy.set('y', transPosition[1]-20);
		}

		{\collision}{
			/* message should be a list with the colliding with entities*/
			collisionFunc.value(this, message);
		};

	}
	
	remove{
		 Routine{
			//clear everything with given realease time
			encoderProxy.clear(release);
			//wait for the release to finish
			release.wait;
			//remove the node from the summing bus
			decoderBus.removeAt(encoderProxyIndex);
			repManager.remove(this);
			attached = false;
	 	}.play(TempoClock.default);
	}

	draw{arg rect; 
		Pen.strokeOval(rect)
	}
}   
 
/* SoundEntity : Vehicle { var  <>input, <>collisionFunc, <>release; 
	
	*new{ arg world, position= RealVector2D[15,15], radius = 1.0, mass = 1.0, 
						velocity = RealVector2D[0, 0], collisionType = \free, heading, 
						side, maxSpeed = 100, maxForce = 40, maxTurnRate = 2, input, collisionFunc, release = 0.2;
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
		   .maxTurnRate_(maxTurnRate)
		   .input_(input)
		   .collisionFunc_(collisionFunc)
		   .release_(release)	
		   .init;
	}

	init{
		super.init;
		collisionFunc = collisionFunc ?? {{}};
		release = release ?? {0.2};
	}
}
*/
