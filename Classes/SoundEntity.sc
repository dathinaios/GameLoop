
/*
	This is a basic Mobile Unit with varying sound input
*/

SoundEntity : Vehicle { var  <>input, <>collisionFunc, <>forceFunc, <>release;
	
	*new{ arg world, position= RealVector2D[15,15], radius = 1.0, mass = 1.0, 
						velocity = RealVector2D[0, 0], collisionType = \free, heading, 
						side, maxSpeed = 100, maxForce = 40, maxTurnRate = 2, input,
						collisionFunc, forceFunc, release = 0.2;
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
		   .forceFunc_(forceFunc)
		   .release_(release)	
		   .init;
	}

	init{
		//"SoundEntity init".postln;
		super.init;
		collisionFunc = collisionFunc ?? {{}};
		release = release ?? {0.2};
	}

	collision { arg entList; colliding = true;
				collisionFunc.value(this, entList);
	}

}

SoundEntityRepresentation : EntityRepresentation { var color, collisionColor;
										 	 var <penWidth = 1.5;
										 	 var <audioFunc, <audioFuncIndex;
							
	*new { arg  entity, repManager, color, collisionColor;  
	^super.newCopyArgs(entity, repManager, color, collisionColor).init
	}

	init { 
		var latency, decoderBus;
		position = entity.position;
		radius = entity.radius;
		color = color ?? {Color.green};
		collisionColor = collisionColor ?? {Color.red};
		//get the right encoder from the GameLoopDecoder class
		audioFunc = GameLoopDecoder.getEncoderProxy;
		audioFunc.clock = TempoClock.default;
		//plug the proxy to the decoder summing bus
		decoderBus = GameLoopDecoder.decoderBus;
		//Always put the new Node in an extra slot of the Summing nodeRpoxy
		audioFuncIndex = decoderBus.sources.size - 1;
		decoderBus.put(audioFuncIndex, audioFunc);
		//Here we need to run the representation and after the right amount of time 
		//add the representation and the entity to their respective managers
		latency = Server.default.latency;
		Routine{
			this.run;
			//wait for 'latency' before adding to managers so that everything is in sync.
			if(latency.notNil) {latency.wait};
			//Add everything at exactly the same time as the bundle
			entity.add;
			repManager.add(this);
		}.play;
	}
	
	color { if(entity.colliding, {^collisionColor },{^color})
	}

	update { arg entity, message; var transPosition; 
		//first for the standard update from the superclass that gets the new 
		//position and velocity paramaters
		super.update(entity, message);
		//here add any additional functionality
		switch (message) 
		{\remove} {this.remove}
		//set the speed of the NodeProxy *after* the integration to account for the lag (interpolation)
		{\preUpdate}
		{
 		audioFunc.set('speed', entity.velocity.norm);
		//transform the position according to the camera position.
		if (GameLoop.instance.cameraActive,
			{transPosition = Camera2D.instance.applyTransformation(entity)+ entity.world.center},
			{transPosition = position}
		);
		//set the syth with the new position values
		audioFunc.set('x', transPosition[0]-20);
		audioFunc.set('y', transPosition[1]-20);
		};

	}
	
	remove{var decoderBus, release;
		 decoderBus = GameLoopDecoder.decoderBus;
		 release = entity.release;
		 Routine{
			//clear everything with given realease time
			audioFunc.clear(release);
			//wait for the release to finish
			release.wait;
			//remove the node from the summing bus
			decoderBus.removeAt(audioFuncIndex);
	 	}.play(TempoClock.default);
	}

	draw{arg rect; 
		Pen.strokeOval(rect)
	}
	
	run {
			audioFunc.source = { arg dt;
						   var x , y;
						   var rad, azim, elev, in, speed;
						    dt = entity.world.dt;
						  	#x, y = Control.names(#[x, y]).kr([position[0], position[1]]);
						   	x = Ramp.kr(x, dt); //GameLoop.instance.dt);
						   	y = Ramp.kr(y, dt); //GameLoop.instance.dt);
						   	//debugging
						   	//x = MouseX.kr(-20, 20);
						   	//y = MouseY.kr(-20, 20);
							//To use the velocity
							speed = Control.names(\speed).kr(entity.velocity.norm);
							speed = Ramp.kr(speed, dt); //GameLoop.instance.dt);
							//input
							if(entity.input == nil,
								{ // A default sound in case there is not input provided
								in = Impulse.ar(speed.linlin(0,10, 5, rrand(50, 200.0)));
								in = BPF.ar(in, rrand(2000, 18000.0)*rrand(0.3, 2.0), 0.4);
								},
								{in = entity.input.value(speed)}
							);
							azim = atan2(y,x);
							rad = hypot(x,y);
							elev = 0;
							//get and use the relevant encoder
							GameLoopDecoder.getEncoder.ar(
								in, 
								azim, 
								rad, 
								elev: elev, 
								ampCenter: 0.9
							);
						};
	}

}   

SoundEntityController : Controller{ 

	getForce { var forceFunc; 
		//the variable antity is declared in the superclass Controller
		forceFunc = entity.forceFunc;
		// The steering is using the forceFunction supplied. Else the Entity remain static
			if(forceFunc == nil,
				{^0},
				{^forceFunc.value(entity)}
			);
	}

}

