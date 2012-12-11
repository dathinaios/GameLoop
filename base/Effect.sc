
Effect { var entity, params, mainOut;
	
	*new { arg entity, params;
		^super.newCopyArgs(entity, params).init;
	}

	init { 
		this.mainOut;
	}
	
	//A synth for the sound
	
	mainOut { arg dur; var x , y;
			mainOut = SingleSynth.new;
			x = entity.position[0];
		   	y = entity.position[1];
			mainOut.play({ arg gate = 1;
						   var rad, azim, in;
						   	in = params[0].value; 
							x = DC.kr(x);
							y = DC.kr(y);
							azim = atan2(y,x);
							rad = hypot(x,y);
							in = SpacePolarB2.ar(in, azim, rad,  ampCenter: SeasonsGain.gain1 *  1, speakerRho: 1.5);
							//MainOut
							Out.ar(AmbiDecoderCentre.ambiBus2.index, in);
						},
						GroupManager.between3,
						Server.default.latency
			).onEnd({mainOut.free});

	}
}
