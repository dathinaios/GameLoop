
GameLoopDecoder { classvar <encoderProxy, <decoderProxy, <summingProxy, 
									encoderChannels, decoderChannels, <active = false,
									<>library, <>type, <>dp, order,
									kernel;

	*new{ arg library = 'AmbIEM', type = 'binaural', dp = true; /*{{{*/
		
		active = true;
		this.library_(library);
		this.type_(type);
		this.dp_(dp);

		//First let's get some information that we need
		case 
		//binaural
		{library =='AmbIEM' && (type == 'binaural')} {decoderChannels = 2; order = 3}
		{library =='ATK' && (type == 'newListen')} {decoderChannels = 2; order = 1}
		//stereo
		{library =='ATK' && (type == 'newStereo')} {decoderChannels = 2; order = 1};

		//encoder channels depend on the Ambisonics order (1st,2nd or 3d)
		//use by the getEncoderProxy method
		switch (order)
		{1}{ encoderChannels = 4 }
		{2}{ encoderChannels = 9 }
		{3}{ encoderChannels = 16};


		//decoded channels depend on the decoder. Create the NodeProxy if it does not exist in the right form
		if(decoderProxy.numChannels != decoderChannels)
		{decoderProxy = NodeProxy(Server.default, 'audio', decoderChannels).fadeTime_(0.5)};

		//Create the decoder according to the type given
		case 
		//binaural
		{library =='AmbIEM' && (type == 'binaural')} {
			BinAmbi3O.init('1_4_7_4');
			Routine{
				1.wait; //take the time to load the kernel
				decoderProxy.source = {
				var in, out;
				in = \in.ar(0!encoderChannels);
				out = BinAmbi3O.ar(in);
				Out.ar(0, out);
				};
				this.readyMsg;
			}.play;
		}
		{library =='ATK' && (type == 'newStereo')} {
			//get the kernel (in this case it is a matrix)
			kernel = FoaDecoderMatrix.newStereo(131/2 * pi/180, 0.5); // Cardioids at 131 deg
			Routine{
				1.wait; //take the time to load the kernel
				decoderProxy.source = {
				var in, out;
				in = \in.ar(0!encoderChannels);
				out = FoaDecode.ar(in, kernel);
				Out.ar(0, out);
				};
				this.readyMsg;
			}.play;
		}
		//check here for auto choosing correct decoder: chttp://www.ambisonictoolkit.net/Help/Guides/Intro-to-the-ATK.html
		{library =='ATK' && (type == 'newListen')} {
			//get the kernel
			kernel = FoaDecoderKernel.newListen(1013);
			Routine{
				1.wait; //take the time to load the kernel
				decoderProxy.source = {
				var in, out;
				in = \in.ar(0!encoderChannels);
				out = FoaDecode.ar(in, kernel);
				Out.ar(0, out);
				};
				this.readyMsg;
			}.play;
		};

		// create the summing NodeProxy that will act as the summation bus
		// see http://new-supercollider-mailing-lists-forums-use-these.2681727.n2.nabble.com/Many-to-One-Audio-Routing-in-Jitlib-td7594874.html

		summingProxy = NodeProxy(Server.default, 'audio', encoderChannels);
		//route the summation bus to the decoder
		summingProxy <>> decoderProxy;

	}/*}}}*/

	*readyMsg{/*{{{*/
		"A decoder was created through GameLoopDecoder".postln;
	}/*}}}*/

	*getEncoderProxy{/*{{{*/
		//return proxies with the channels needed for the given order
		^NodeProxy(Server.default, 'audio', encoderChannels);
	}/*}}}*/

	*getEncoderClass{/*{{{*/
		//return the right encoder class
		case 
		//binaural
		{library =='AmbIEM' && (type == 'binaural')} {
			if (dp == true,
				{^SpacePolarAmbIEMDp},
				{^SpacePolarAmbIEM}
			);
		}
		//Make all the ATK types into one condition as they return the same encoder
		{library =='ATK' && (type == 'newListen')} {
			if (dp == true,
				{^SpacePolarATKDp},
				{^SpacePolarATK}
			);
		}
		{library =='ATK' && (type == 'newStereo')} {
			if (dp == true,
				{^SpacePolarATKDp},
				{^SpacePolarATK}
			);
		};
	}/*}}}*/

	*clear{
		Routine{
			decoderProxy.source = nil;
			1.wait;
			kernel.free;
			active = false;
		}.play;
	}

}
