
GameLoopDecoder { classvar <encoderProxy, <decoderProxy, 
				 		  encoderChannels, decoderChannels,
				 		  <>library, <>type, <>dp, order;

	*new{ arg library = 'AmbIEM', type = 'binaural', dp = true; /*{{{*/
		

		this.library_(library);
		this.type_(type);
		this.dp_(dp);

		//First let's get some information that we need
		case 
		//binaural
		{library =='AmbIEM' && (type == 'binaural')} {decoderChannels = 2; order = 3}
		{library =='ATK' && (type == 'newListen')} {decoderChannels = 2; order = 1};

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
			BinAmbi3O.kemarPath_("/Applications/SuperCollider/KemarHRTF");
			BinAmbi3O.init('1_4_7_4');
			decoderProxy.source = {
				var in, out;
				in = \in.ar(0!encoderChannels);
				out = BinAmbi3O.ar(in);
				Out.ar(0, out);
			};
		}
		{library =='ATK' && (type == 'newListen')} {
			decoderProxy.source = {
			var in, out;
			in = \in.ar(0!encoderChannels);
			out = FoaDecode.ar(in, 'newListen');
			Out.ar(0, out);
			};
		};

		"A decoder was created through GameLoopDecoder".postln

	}/*}}}*/

	*getEncoderProxy{/*{{{*/
		//return proxies with the channels needed for the given order
		^NodeProxy(Server.default, 'audio', encoderChannels);
	}/*}}}*/

	*getEncoder{/*{{{*/
		//return the right encoder class
		case 
		//binaural
		{library =='AmbIEM' && (type == 'binaural')} {
			if (dp == true,
				{^SpacePolarAmbIEMDp},
				{^SpacePolarAmbIEM}
			);
		}
		{library =='ATK' && (type == 'newListen')} {
			if (dp == true,
				{^SpacePolarATKDp.class},
				{^SpacePolarATK}
			);
		};
	}/*}}}*/

	*getDecoder{/*{{{*/
		^decoderProxy;
	}/*}}}*/

}
