//TODO: Space Classes: In the stereo version sthere is something weird with the mappin in the pan2 UgenÙÙ
//No reason really for the stereo ones. Use the ambisonic ones and decode to stereo


SpacePolar2 : UGen{ 

	*ar {arg in, azimuth, radius,  ampCenter, xSize = [-1, 1]; //DELETE: dont think I need the room dimensions
	    var amp;
		// ** VSpace ** style
		//x = radius*(azimuth.cos); //find the x coordinate
      	amp = (1/radius.squared).clip(0, 1);
		//here we scale and filter like in ** VSpace **
      	in = Pan2.ar(in*ampCenter, azimuth.linlin(3*(pi/4), pi/4, -0.95, 0.95));
      	in = amp*LPF.ar(in, (100000/radius.clip(0.01, inf)).clip(20, (SampleRate.ir/2) - 100));

		^in; 
	}

}

SpacePolarDp2 : UGen { 

	*ar {arg in, azimuth, radius,  ampCenter, xSize = [-1, 1]; //DELETE: dont think I need the room dimensions
	    var amp;
		// ** VSpace ** style
		//x = radius*(azimuth.cos); //find the x coordinate
      	amp = (1/radius.squared).clip(0, 1);
		//here we scale and filter like in ** VSpace **
      	in = DelayL.ar(in, 1, radius/343); //for doppler
      	in = Pan2.ar(in*ampCenter, azimuth.linlin(3*(pi/4), pi/4, -0.95, 0.95)); 
      	in = amp*LPF.ar(in, (100000/radius.clip(0.01, inf)).clip(20, (SampleRate.ir/2) - 100));

		^in;
	}

}

//****************\\

SpaceCart2 : UGen{ 

	*ar {arg in, x, y, ampCenter;
	    var amp, radius, azimuth;
	    
	      radius = hypot(x,y); //finding the radius, 
	      azimuth = atan2(y,x);// azimuth in radians //.linlin(pi, -pi, 0, 2pi); 
		// ** VSpace ** style
      	amp = (1/radius.squared).clip(0, 1);
		//here we scale and filter with the ** VSpace ** formula
      	in = Pan2.ar(in*ampCenter, azimuth.linlin(3*(pi/4), pi/4, -0.95, 0.95)); //pi/4 = 45 degrees,3*(pi/4)  = 135
      	in = amp*LPF.ar(in, (100000/radius.clip(0.01, inf)).clip(20, (SampleRate.ir/2) - 100));

		^in
	}

}

SpaceCartDp2 : UGen{

	*ar {arg in, x, y, ampCenter;
	    var amp, radius, azimuth;
	    	radius = hypot(x,y); //finding the radius, 
	    	azimuth = atan2(y,x); // azimuth in radians			// ** VSpace ** style
      	amp = (1/radius.squared).clip(0, 1);
		//here we scale and filter like in ** VSpace **
      	in = DelayL.ar(in, 1, radius/343); //for doppler
      	in = Pan2.ar(in*ampCenter, azimuth.linlin(3*(pi/4), pi/4, -0.95, 0.95)); 
      	in = amp*LPF.ar(in, (100000/radius.clip(0.01, inf)).clip(20, (SampleRate.ir/2) - 100));

		^in
	}

}

//****************\\
//----Ambisonic---\\
//****************\\


//azimuth -  in radians, -pi to pi
//elevation -  in radians, -0.5pi to +0.5pi

SpacePolarB1 : UGen{

	*ar {arg in, azimuth, radius,  ampCenter, speakerRho = 2;
	    var amp;
	    //var w,x,y,z;
		// ** VSpace ** style
      	amp = (1/radius.squared).clip(0, 1);
		//here we scale and filter like in ** VSpace **
      	in = amp*MoogVCF.ar(in*ampCenter, (100000/radius.clip(0.01, inf)).clip(20, (SampleRate.ir/2) - 100), 0);
		//encode the signal (the radius code is used in order to take advantage of the rho argument of BFEncode1)
		^BFEncode1.ar(in,  azimuth - 0.5pi, 0, radius.clip(0, speakerRho).linlin(0, speakerRho, 0, 1)); //
 	}

}

SpacePolarB1Dp : UGen {
	*ar {arg in, azimuth, radius,  ampCenter, speakerRho = 2;
	    var amp;
	    //var w,x,y,z;
		// ** VSpace ** style
      	amp = (1/radius.squared).clip(0, 1);
		//here we scale and filter like in ** VSpace **
		in = DelayL.ar(in, 1, radius/343); //for doppler
      	in = amp*MoogVCF.ar(in*ampCenter, (100000/radius.clip(0.01, inf)).clip(20, (SampleRate.ir/2) - 100), 0);
		//encode the signal (the radius code is used in order to take advantage of the rho argument of BFEncode1)
		^BFEncode1.ar(in,  azimuth - 0.5pi, 0, radius.clip(0, speakerRho).linlin(0, speakerRho, 0, 1)); //
 	}

}

SpacePolarB2 : UGen{

	*ar {arg in, azimuth, radius,  ampCenter, speakerRho = 2;
	    var amp;
		// ** VSpace ** style
      	amp = (1/radius.squared).clip(0, 1);
		//here we scale and filter like in ** VSpace **
      	in = amp*MoogVCF.ar(in*ampCenter, (100000/radius.clip(0.01, inf)).clip(20, (SampleRate.ir*0.5) - 100), 0);
		//encode the signal (the radius code is used in order to take advantage of the rho argument of BFEncode1)
		^FMHEncode1.ar(in,  azimuth - 0.5pi, 0, radius.clip(0, speakerRho).linlin(0, speakerRho, 0, 1)); //
 	}
		//azimuth.linlin(-pi, pi, pi, -pi) + 0.5pi
}

SpacePolarB2Dp : UGen {
	*ar {arg in, azimuth, radius,  ampCenter, speakerRho = 2;
	    var amp;
		// ** VSpace ** style
      	amp = (1/radius.squared).clip(0, 1);
		//here we scale and filter like in ** VSpace **
		in = DelayL.ar(in, 1, radius/343); //for doppler
      	in = amp*MoogVCF.ar(in*ampCenter, (100000/radius.clip(0.01, inf)).clip(20, (SampleRate.ir*0.5) - 100), 0);
		//encode the signal (the radius code is used in order to take advantage of the rho argument of BFEncode1)
		^FMHEncode1.ar(in,  azimuth - 0.5pi, 0, radius.clip(0, speakerRho).linlin(0, speakerRho, 0, 1)); //
 	}

}

SpacePolarAmbIEM : UGen{

	*ar {arg in, azimuth, radius,  elev = 0, ampCenter = 1, speakerRho = 2;
	    var amp;
		// ** VSpace ** style
      	amp = (1/radius.squared).clip(0, 1);
		//here we scale and filter like in ** VSpace **
      	in = amp*MoogVCF.ar(in*ampCenter, (100000/radius.clip(0.01, inf)).clip(20, (SampleRate.ir*0.5) - 100), 0);
		//encode the signal
		^PanAmbi3O.ar(in, 0.5pi - azimuth, DC.kr(elev)); //
 	}
		//azimuth.linlin(-pi, pi, pi, -pi) + 0.5pi
}

SpacePolarAmbIEMDp : UGen {
	*ar {arg in, azimuth, radius, elev = 0, ampCenter = 1, speakerRho = 2;
	    var amp;
		// ** VSpace ** style
      	amp = (1/radius.squared).clip(0, 1);
		//here we scale and filter like in ** VSpace **
		in = DelayL.ar(in, 1, radius/343); //for doppler
      	in = amp*MoogVCF.ar(in*ampCenter, (100000/radius.clip(0.01, inf)).clip(20, (SampleRate.ir*0.5) - 100), 0);
		//encode the signal
		^PanAmbi3O.ar(in, 0.5pi - azimuth, DC.kr(elev))  //
 	}

}

SpacePolarATK : UGen{

	*ar {arg in, azimuth, radius,  elev = 0, ampCenter = 1;
	    var amp, foa;
		// ** VSpace ** style
      	amp = (1/radius.squared).clip(0, 1);
		//here we scale and filter like in ** VSpace **
      	in = amp*MoogVCF.ar(in*ampCenter, (100000/radius.clip(0.01, inf)).clip(20, (SampleRate.ir*0.5) - 100), 0);
		//encode the signal
		in = HPF.ar(in, 20.0);		// precondition signal for proximity
		// Encode into our foa signal
		foa = FoaPanB.ar(in, azimuth, elev);
		//foa = FoaRotate.ar(foa, azimuth);
		^FoaProximity.ar(foa, radius.clip(0.0001, 30.0));   // image
 	}
		//azimuth.linlin(-pi, pi, pi, -pi) + 0.5pi
}

SpacePolarATKDp : UGen {
	*ar {arg in, azimuth, radius, elev = 0, ampCenter = 1;
	    var amp, foa;
		// ** VSpace ** style
      	amp = (1/radius.squared).clip(0, 1);
		//here we scale and filter like in ** VSpace **
		in = DelayL.ar(in, 1, radius/343); //for doppler
      	in = amp*MoogVCF.ar(in*ampCenter, (100000/radius.clip(0.01, inf)).clip(20, (SampleRate.ir*0.5) - 100), 0);
		//encode the signal
		//encode the signal
		in = HPF.ar(in, 20.0);		// precondition signal for proximity
		// Encode into our foa signal
		foa = FoaPanB.ar(in, azimuth, elev);
		//foa = FoaRotate.ar(foa, azimuth);
		^FoaProximity.ar(foa, radius.clip(0.0001, 30.0));   // image
 	}

}
//****************\\
//----Velocity---\\
//****************\\

VelocityXY : UGen{ //controls the sound using direction and speed aka. velocity

	*ar {arg velocityX = 0, velocityY = 0, xStart =0, yStart = 0, xSize = [-10, 10], ySize = [-10, 10];
		var azim, rad, speed;
		var	x, sr;
		var	y;
		
		   sr = SampleRate.ir;
		
		speed = ( //finding the everage velocity with the pythagorean.
			  (velocityX.abs).squared + (velocityY.abs).squared
		).sqrt;
		//in order to start from any point
		xStart = Line.kr(xStart, xSize[0], 0.01);
		yStart = Line.kr(yStart, ySize[0], 0.01);
		//the phasors actually move the sound in speed meters/s
		x = RedPhasor2.ar(1, velocityX/sr, xStart, xSize[1], 0); //.poll(label: "X");
		y = RedPhasor2.ar(1, velocityY/sr, yStart, ySize[1], 0); //.poll(label: "Y");
		//calculate the azimuth and radius
		rad = hypot(x,y); //finding the radius, 
		azim = atan2(y,x); //x.linlin(xmin, ySize, -1, 1);
		//return an array with usefull variables
		//x		//y		
		^[x, y, rad, azim, speed]
	
	}
	
	*kr {arg velocityX = 0, velocityY = 0, xStart =0, yStart = 0, xSize = [-10, 10], ySize = [-10, 10];
		var azim, rad, speed;
		var	x,  sr;
		var	y;
		
		sr = ControlRate.ir;
		speed = ( //finding the everage velocity with the pythagorean.
			  (velocityX.abs).squared + (velocityY.abs).squared
		).sqrt;
		
		//in order to start from any point
		xStart = Line.kr(xStart, xSize[0], 0.01);
		yStart = Line.kr(yStart, ySize[0], 0.01);
		//the phasors actually move the sound in speed meters/s
		x = RedPhasor2.kr(1, velocityX/sr, xStart, xSize[1], 0); //.poll(label: "X");
		y = RedPhasor2.kr(1, velocityY/sr, yStart, ySize[1], 0); //.poll(label: "Y");
		//calculate the azimuth and radius
		rad = hypot(x,y); //finding the radius, 
		azim = atan2(y,x); //x.linlin(xmin, ySize, -1, 1);
		//return an array with usefull variables
		//x		//y		
		^[x, y, rad, azim, speed]
	
	}
	
		//velocityX = MouseX.kr((-1)*maxSpeed, maxSpeed);
		//velocityX = LagUD.ar(velocityX, 4, 1);
		////moving y
		//velocityY = MouseY.kr(maxSpeed, (-1)*maxSpeed);
		//velocityY = LagUD.ar(velocityY, 4, 1);
		//relativeVelocity = x/rad.squared;
		//relativeVelocity = RedPhasor2.kr(1, relativeVelocity/sr, xStart, xSize[1], 0);

}

Velocity : UGen{

	*ar {arg direction = 0, magnitude = 0, xStart =0, yStart = 0, xSize = [-10, 10], ySize = [-10, 10];
		var azim, rad;
		var	x, y, sr;
		sr = SampleRate.ir;
		x = magnitude*(direction.cos); //find the velocity x
		y = magnitude*(direction.sin); //find the velocity y		//in order to start from any point
		xStart = Line.kr(xStart, xSize[0], 0.01);
		yStart = Line.kr(yStart, ySize[0], 0.01);
		//the phasors actually move the sound in speed meters/s
		x = RedPhasor2.ar(1, x/sr, xStart, xSize[1], 0); //.poll(label: "X");
		y = RedPhasor2.ar(1, y/sr, yStart, ySize[1], 0); //.poll(label: "Y");
		//calculate the azimuth and radius
		rad = hypot(x,y);
		azim = atan2(y,x);
		//return an array with usefull variables
		//x		//y		
		^[x, y, rad, azim, magnitude]
	
	}
	
	*kr {arg direction = 0, magnitude = 0, xStart =0, yStart = 0, xSize = [-10, 10], ySize = [-10, 10];
		var azim, rad;
		var	x, y, sr;
		sr = ControlRate.ir;
		x = magnitude*(direction.cos); //find the velocity x
		y = magnitude*(direction.sin); //find the velocity y			//in order to start from any point
		xStart = Line.kr(xStart, xSize[0], 0.01);
		yStart = Line.kr(yStart, ySize[0], 0.01);
		//the phasors actually move the sound in speed meters/s
		x = RedPhasor2.kr(1, x/sr, xStart, xSize[1], 0); //.poll(label: "X");
		y = RedPhasor2.kr(1, y/sr, yStart, ySize[1], 0); //.poll(label: "Y");
		//calculate the azimuth and radius
		rad = hypot(x,y);
		azim = atan2(y,x);
		//return an array with usefull variables
		//x		//y		
		^[x, y, rad, azim, magnitude]
	
	}

}


Velocity1D : UGen{ //controls the sound using direction and speed aka. velocity

	*ar {arg velocity = 1, min = 1, max = 1, start = 0;
		var sr;
		sr = SampleRate.ir;
		//in order to start from any point
		min = Line.kr(start, min, 0.01);
		//the phasors actually move the sound in speed meters/s
		^RedPhasor2.ar(1, velocity/sr, min, max, 0); //.poll(label: "X");
	
	}
	
	*kr {arg velocity = 1, min = 1, max = 1, start = 0;
		var sr;
		sr = ControlRate.ir;
		//in order to start from any point
		min = Line.kr(start, min, 0.01);
		//the phasors actually move the sound in speed meters/s
		^RedPhasor2.kr(1, velocity/sr, min, max, 0); //.poll(label: "X");
	
	}
}

//****************\\
//------Fade------\\
//****************\\

//usage
//a = {arg gate = 1; SinOsc.ar(200, 0, 0.1)*Fade.kr(inTime: 1, outTime: 4, duration: 5)}.play;
//a.release
//a.release(8)

Fade : UGen{
	//this creates a simple fade in and fade out
	//while makes sure the whole process wont take more than duration
	*kr { arg  inTime = 0.015, outTime = 0.015, duration = 0.1, gate = 1, curve = 'exponential', tweek = 0.000001;
		 var env, trig = 1, durTrig;
		 
		env = Env.new(
			[tweek, 1, tweek],[inTime, outTime], curve,1
		);
		durTrig = SetResetFF.kr(DC.kr(1), TDelay.kr(DC.kr(1), duration-outTime));
		^EnvGen.kr(env, gate*durTrig, doneAction: 2);
	}

}

Fade2 : UGen{
	//this creates a simple fade in and fade out
	*kr { arg  inTime = 0.015, outTime = 0.015, gate = 1, curve = 'exponential', tweek = 0.000001;
		 var env;
		 
		env = Env.new(
			[tweek, 1, tweek],[inTime, outTime], curve, 1
		);
		^EnvGen.kr(env, gate, doneAction: 2);
	}

}

FadeAtt : UGen{
	//this creates a simple fade in and fade out but adding a sharp attack
	//while makes sure the whole process wont take more than duration
	*kr { arg  inTime = 0.015, decTime = 0.05, outTime = 0.015, attLevel =1, duration = 0.1, gate = 1, curve = 'exponential';
		 var env, trig = 1, durTrig;
		 
		env = Env.new(
			[0.000001, attLevel, 1, 0.000001],[inTime, decTime, outTime], curve, 2
		);
		durTrig = SetResetFF.kr(DC.kr(1), TDelay.kr(DC.kr(1), duration-outTime));
		^EnvGen.kr(env, gate*durTrig, doneAction: 2);
	}
	
	*ar { arg  inTime = 0.015, decTime = 0.05, outTime = 0.015, attLevel =1, duration = 0.1, gate = 1, curve = 'exponential';
		 var env, trig = 1, durTrig;
		 
		env = Env.new(
			[0.000001, attLevel, 1, 0.000001],[inTime, decTime, outTime], curve, 2
		);
		durTrig = SetResetFF.kr(DC.kr(1), TDelay.kr(DC.kr(1), duration-outTime));
		^EnvGen.ar(env, gate*durTrig, doneAction: 2);
	}

}

Fade2Att : UGen{
	//this creates a simple fade in and fade out
	*kr { arg  inTime = 0.015, decTime = 0.05, outTime = 0.015, attLevel =1, gate = 1, curve = 'exponential';
		 var env;
		 
		env = Env.new(
			[0.000001, attLevel, 1, 0.000001],[inTime, decTime, outTime], curve, 2
		);
		^EnvGen.kr(env, gate, doneAction: 2);
	}
	
	*ar { arg  inTime = 0.015, decTime = 0.05, outTime = 0.015, attLevel =1, gate = 1, curve = 'exponential';
		 var env;
		 
		env = Env.new(
			[0.000001, attLevel, 1, 0.000001],[inTime, decTime, outTime], curve, 2
		);
		^EnvGen.ar(env, gate, doneAction: 2);
	}

}


//****************\\
//------Fill Factor TGrains------\\
//****************\\

TGrainsFF1 : UGen{
	
	//goes through the file 
	*ar { arg numChannels = 2, tstretch = 1, density=1, ff = 0.5, bufnum=0, rate=1,
			 pan=0, amp=1.0, loop = 1, interp=4; 
		 var bufDur, bufFrames, trig;

		bufDur = BufDur.ir(bufnum);
		bufFrames = BufFrames.ir(bufnum);
		trig = Impulse.ar(density);
	
		^TGrains.ar(	2, 
					trig, 
					bufnum, 
					rate, 
					RedPhasor.ar(0, (bufDur*tstretch)/bufFrames,0, bufDur, loop, 0, bufDur), 
					density.reciprocal*ff, 
					pan, 
					amp, 
					interp);	
		}
}

TGrainsFF2 : UGen{
	
	//goes through the file 
	*ar { arg numChannels = 2, tstretch = 1, density=1, ff = 0.5, bufnum=0, rate=1,
			 pan=0, amp=1.0, att = 0.5, dec = 0.5, loop = 1, interp=4; 
		 var bufDur, bufFrames, trig;

		bufDur = BufDur.ir(bufnum);
		bufFrames = BufFrames.ir(bufnum);
		trig = Impulse.ar(density);
	
		^TGrains2.ar(	2, 
					trig, 
					bufnum, 
					rate, 
					RedPhasor.ar(0, (bufDur*tstretch)/bufFrames,0, bufDur, loop, 0, bufDur), 
					density.reciprocal*ff, 
					pan, 
					amp, 
					att,
					dec,
					interp);	
		}
}


//****************\\
//------CDP inspired------\\
//****************\\

Stack : UGen{ //stack back to back ebband flow generator
	
	//goes through the file 
	*ar { arg buf, transp, count, attTime, rate = 1, mul = 0.5, numChans = 1; //count: number of copies. attTime: time of attack
	  var bufDur, bufRateScale, in;
	  	bufRateScale = BufRateScale.ir(buf);
		bufDur = BufDur.ir(buf);
		//play the normal sample
		in = PlayBuf.ar(1, buf, rate*bufRateScale) * mul;
		//generate the stack versions
		(count).do{ arg i; var curAtt, curDur, curRate;
			//to start the count from 1
			i = i+1;
			//calculate the rate
			curRate = (transp*i).midiratio; //.debug("transp");
			//calculate the duration according to the rate
			curDur = (curRate.reciprocal)*bufDur*(rate.reciprocal);
			//calc the attack time in the new duration
			curAtt = DC.ar(attTime).linlin(0, bufDur, 0, curDur);
			in = in + DelayN.ar(PlayBuf.ar(1, buf, rate*curRate*bufRateScale) * mul, bufDur, (attTime - curAtt));
		};
		^in;
	}
}	

Stack2 : UGen{ //stack back to back ebband flow generator

	//efficient version
	//goes through the file 
	*ar { arg buf, transp, count, attTime, rate = 1, mul = 0.5, numChans = 1; //count: number of copies. attTime: time of attack
	  var bufDur, bufRateScale, in, delayBuf, timesArray = [], inArray = [], server, rateRecip;
		server = Server.default;
	  	bufRateScale = BufRateScale.ir(buf);
		rateRecip = rate.reciprocal;
		bufDur = buf.duration;
		delayBuf = {LocalBuf(server.sampleRate * (bufDur*rateRecip + 2), 1).clear};
		//delayBuf = Buffer.alloc(server, server.sampleRate * (bufDur*rateRecip), 1);
		//play the normal sample
		in = PlayBuf.ar(1, buf, rate*bufRateScale) * mul;
		//generate the stack versions
		(count).do{ arg i; var curAtt, curDur, curRate;
			//to start the count from 1
			i = i+1;
			//calculate the rate
			curRate = (transp*i).midiratio; //.debug("transp");
			//calculate the duration according to the rate
			curDur = (curRate.reciprocal)*bufDur*rateRecip;
			//calc the attack time in the new duration
			curAtt = DC.ar(attTime).linlin(0, bufDur, 0, curDur);
			//add the time as a delay
			timesArray = timesArray.add(attTime - curAtt);
			inArray = inArray.add(PlayBuf.ar(1, buf, rate*curRate*bufRateScale) * mul);
		};
		in = in + MultiInputDelay.ar(`timesArray, `inArray, bufnum: delayBuf.value);
		^in;
	}
}

/* ================== */
/* = Spectral Space = */
/* ================== */

SpecSpace : UGen{ // a UGen for intuitive contol of spectral space. The 'space' of the sound is defined by
				  // low and high limit and the movement happens in this space giving avalue from -1 to 1
				  // as well as a bandwidth in octaves.
	
	*ar{ arg in, lowLmt, highLmt, center, width;  // the width is in octaves
		 var freq;
		freq = center.linlin(-1, 1, lowLmt, highLmt); //make the -1 to 1 into the defined spectral teritorry
		freq = freq.clip(lowLmt, highLmt); //clip just in case
		^BBandPass.ar(in, freq.clip(20,20000), width);
	}
}



