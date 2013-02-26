
/*
	This is a basic Mobile Unit with varying sound input
*/

MUEnt : Vehicle { var  >collisionFunc, >forceFunc;
	
	*new{ arg world, position, radius, mass, velocity, controller,/*{{{*/
			  heading, side, maxSpeed, maxForce, maxTurnRate, collisionFunc, forceFunc;
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
		   .collisionFunc_(collisionFunc)
		   .forceFunc_(forceFunc)
		   .init;		
	}/*}}}*/

	init{/*{{{*/
		//"simplecircle init".postln;
		super.init;
		collisionFunc = collisionFunc ?? {{}};
	}/*}}}*/

	collision { arg entList; colliding = true;/*{{{*/
				collisionFunc.value(entList, this);
	}/*}}}*/

}

MUEntRepresentation : EntityRepresentation { var color, collisionColor;
										 	 var <penWidth = 1.5;
										 	 var in, out;
							
	*new { arg  entity, color, collisionColor;  /*{{{*/
	^super.newCopyArgs(entity, color, collisionColor).init
	}/*}}}*/

	init { /*{{{*/
		position = entity.position;
		radius = entity.radius;
		color = color ?? {Color.green};
		collisionColor = collisionColor ?? {Color.red};

		in     = NodeProxy.audio.fadeTime_(4);
		in.group = GroupManager.between1; //leave th inGroup for control signals
		out    = NodeProxy.audio.fadeTime_(4);
		out.group = GroupManager.between3;

		this.makeIn;
		this.makeOut;
	}/*}}}*/
	
	color { if(entity.colliding, {^collisionColor },{^color})/*{{{*/
	}/*}}}*/

	update { arg entity, message; /*{{{*/
		//first fo the standard update from the superclass
		super.update(entity, message);
		//here add any additional functionality
		switch (message) 
		{\remove} {this.remove};
		//set the syth with the new position values
		out.set('x', position[0]);
		out.set('y', position[1]);

	}/*}}}*/
	
	remove{/*{{{*/
		fork{
		 in.release(2);
		 2.wait;
		 out.release(2);
		};
	}/*}}}*/

	draw{arg rect; /*{{{*/
		Pen.strokeOval(rect)
	}/*}}}*/
	
	makeIn {/*{{{*/
			in.source = {  
						var in, vel;
						//To use the velocity
						//vel = Control.names(\vel).kr(entity.velocity.norm);
						//vel = Ramp.kr(vel, GameLoop.instance.dt);
						//input
						//in = entityParams.get['input'].value(vel, gate);
						Impulse.ar(30);
					  }; 
	}/*}}}*/
	
	makeOut {/*{{{*/
			out.source = { arg frameRate = 0.05;
						   var x , y;
						   var rad, azim, elev, input;
						  	#x, y = Control.names(#[x, y]).kr([position[0], position[1]]);
						   	x = Ramp.kr(x, frameRate);
						   	y = Ramp.kr(y, frameRate);
						   	input = in.ar;
						   	x.poll;
						   	//y.poll;
							//TODO: I should make a version for x,y since I am dealing with x,y.
							azim = atan2(y,x);
							rad = hypot(x,y);
							elev = 0;
							//get and use the relevant encoder
							input = AmbiDecoderCentre.encoderPolar(
								input, 
								azim, 
								rad, 
								elev: elev, 
								ampCenter: 1, 
								dp: true
							);
							//in = SpacePolarB2Dp.ar(in, azim, rad,  ampCenter: 1, speakerRho: 1.5);
							//in = SpacePolarAmbIEMDp.ar(in, azim, rad,  ampCenter: 1);
							//MainOut 
							//FMHDecode1.stereo(w, y);
							Out.ar(AmbiDecoderCentre.bus.index, input);
						}
	}/*}}}*/

}   

MUEntController : Controller{

	getForce { arg entity; var path, position, width;/*{{{*/
			width =entity.world.center[0]*2;
			position = RealVector2D[rrand(2.0, width), rrand(2.0, width)];
			path = Path(Array.fill(rrand(8.0, 20.0),
			{RealVector2D[position[0] + rrand(-33, 33.0), position[1] + rrand(-33.0, 33.0)]}),true);
			^PathFollowing.calculate(entity,path, 0.5);
	}/*}}}*/

}

