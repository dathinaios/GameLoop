
/*
	This is a basic Mobile Unit with varying sound input
*/

SoundEntity : Vehicle { var  <>input, <>collisionFunc, <>forceFunc, <>release;
	
	*new{ arg world, position, radius, mass, velocity, controller,/*{{{*/
			  heading, side, maxSpeed, maxForce, maxTurnRate, input,
			  collisionFunc, forceFunc, release;
		  ^super.new(world, 
					 position, 
					 radius, 
					 mass
		  ).velocity_(velocity)
		   .controller_(controller)
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
	}/*}}}*/

	init{/*{{{*/
		//"simplecircle init".postln;
		super.init;
		collisionFunc = collisionFunc ?? {{}};
		release = release ?? {0.2};
	}/*}}}*/

	collision { arg entList; colliding = true;/*{{{*/
				collisionFunc.value(entList, this);
	}/*}}}*/

}

SoundEntityRepresentation : EntityRepresentation { var color, collisionColor;
										 	 var <penWidth = 1.5;
										 	 var out;
							
	*new { arg  entity, color, collisionColor;  /*{{{*/
	^super.newCopyArgs(entity, color, collisionColor).init
	}/*}}}*/

	init { /*{{{*/
		position = entity.position;
		radius = entity.radius;
		color = color ?? {Color.green};
		collisionColor = collisionColor ?? {Color.red};
		//get the right encoder from the GameLoopDecoder class
		out = GameLoopDecoder.getEncoderProxy;
		//plug the proxy to the decoder summing bus
		GameLoopDecoder.decoderBus.add(out);
		//this.makeIn;
		this.inputSourse;
	}/*}}}*/
	
	color { if(entity.colliding, {^collisionColor },{^color})/*{{{*/
	}/*}}}*/

	update { arg entity, message; /*{{{*/
		//first fo the standard update from the superclass
		super.update(entity, message);
		//here add any additional functionality
		switch (message) 
		{\remove} {this.remove};
		out.set('vel',entity.velocity.norm);
		//set the syth with the new position values
		out.set('x', position[0]-20);
		out.set('y', position[1]-20);

	}/*}}}*/
	
	remove{/*{{{*/
		//clear everything with give realease time
		 out.clear(entity.release);
		 //remove the node from the summing bus
		 GameLoopDecoder.decoderBus.removeAt(out.sources.find([out]));
	}/*}}}*/

	draw{arg rect; /*{{{*/
		Pen.strokeOval(rect)
	}/*}}}*/
	
	inputSourse {/*{{{*/
			out.source = { arg frameRate = 0.05;
						   var x , y;
						   var rad, azim, elev, in, vel;
						  	#x, y = Control.names(#[x, y]).kr([position[0], position[1]]);
						   	x = Ramp.kr(x, 0.05); //GameLoop.instance.dt);
						   	y = Ramp.kr(y, 0.05); //GameLoop.instance.dt);
							//To use the velocity
							vel = Control.names(\vel).kr(entity.velocity.norm);
							vel = Ramp.kr(vel, 0.05); //GameLoop.instance.dt);
							//input
							//in = entityParams.get['input'].value(vel, gate);
							if(entity.input == nil,
								{ // A default sound in case there is not input provided
								in = Impulse.ar(vel.linlin(0,10, 5, rrand(50, 200.0)));
								in = BPF.ar(in, rrand(200, 18000.0)*MouseX.kr(0.3, 2, lag: rrand(2.0, 6.0)), 0.4);
								},
								{in = entity.input.value(vel)}
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
	}/*}}}*/

}   

SoundEntityController : Controller{ 

	getForce { var forceFunc; /*{{{*/
		//the variable antity is declared in the superclass Controller
		forceFunc = entity.forceFunc;
		// The steering is using the forceFunction supplied. Else the Entity remain static
			if(forceFunc == nil,
				{^0},
				{^forceFunc.value(entity)}
			);
	}/*}}}*/

}

