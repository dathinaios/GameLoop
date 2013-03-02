//Just to keep the ambisonic info in one place and change it according to the setup

AmbiDecoderCentre { classvar <bus, <decoder, <type, <speakerRho ;
     
	*initClass{
		speakerRho = 1.5;
	}

     *startDecoder{ arg channels = 'octagon2'; //[0pi, -0.25pi, -0.5pi,  -0.75pi, -pi , -1.25pi, -1.5pi, -1.75pi];
		type = 'ambisonic2';
		decoder = NodeProxy(Server.default, \audio, 2);
		decoder.group = GroupManager.tailGroup;
		decoder.source = {
			var in, w, x, y, z, r, s, t, u, v;
			#w, x, y, z, r, s, t, u, v = \in.ar(0!16);
			if(channels == 'stereo')
				{in = FMHDecode1.stereo(w, y);}; 
				
			if(channels == 'quad')
				{in = FMHDecode1.quad(w, x, y, v)}; 
				
			if(channels == 'octagon1')
				{in = FMHDecode1.octagon1(w, x, y, u, v)}; 
			
			if(channels == 'octagon2')
				{in = FMHDecode1.octagon2(w, x, y, u, v)}; 
			
			if(channels.class == Array)
				{in = FMHDecode1.ar(w, x, y, z, r, s, t, u, v, channels, 0)};
				
			Out.ar(0, in*0.9)
			};
	}

     *startDecoder2{ arg channels = 'octagon2'; //[0pi, -0.25pi, -0.5pi,  -0.75pi, -pi , -1.25pi, -1.5pi, -1.75pi];
		type = 'ambisonic2';
		bus = Bus.audio(Server.default, 9);
		decoder = SingleSynth.new;
		decoder.play({
			var in, w, x, y, z, r, s, t, u, v;
			#w, x, y, z, r, s, t, u, v = In.ar(bus.index, 9); 
			if(channels == 'stereo')
				{in = FMHDecode1.stereo(w, y);}; 
				
			if(channels == 'quad')
				{in = FMHDecode1.quad(w, x, y, v)}; 
				
			if(channels == 'octagon1')
				{in = FMHDecode1.octagon1(w, x, y, u, v)}; 
			
			if(channels == 'octagon2')
				{in = FMHDecode1.octagon2(w, x, y, u, v)}; 
			
			if(channels.class == Array)
				{in = FMHDecode1.ar(w, x, y, z, r, s, t, u, v, channels, 0)};
				
			Out.ar(0, in*0.9)
			}, GroupManager.tailGroup
		);
	
	}
	
	*startBinaural{
				type = 'binaural';
				A.t.proxySpace = ProxySpace.push;
				bus = Bus.audio(Server.default, 16);
				A.t.busPlug = BusPlug.for(bus);
				BinAmbi3O.kemarPath_("/Applications/SuperCollider/KemarHRTF");
				BinAmbi3O.init('1_4_7_4');
				~out = {BinAmbi3O.ar(A.t.busPlug.ar(16))*0.9;};
				~out.play;
				
	}
	
	*encoderPolar{ arg in, azim, rad, elev, ampCenter, dp = false; //get the relevant encoder according to type
		elev = elev ?? 0; //FIXME: initialization of important values in the representation will avoid the fix here
		if (dp,
			{
			 case
			 {type == 'ambisonic2'}
			 {^SpacePolarB2Dp.ar(in, azim, rad,  ampCenter: ampCenter, speakerRho: speakerRho);}
			 {type == 'binaural'}
			 {^SpacePolarAmbIEMDp.ar(in, azim, rad, elev, ampCenter: 1);}
			},
			{
			 case
			 {type == 'ambisonic2'}
			 {^SpacePolarB2.ar(in, azim, rad,  ampCenter: ampCenter, speakerRho: speakerRho);}
			 {type == 'binaural'}
			 {^SpacePolarAmbIEM.ar(in, azim, rad, elev, ampCenter: 1);}
			})
	}

}

//(
//{
//var in, w, x,y,z; 
//#w,x,y,z = In.ar(AmbiDecoderCentre.ambiBus1.index, 4); 
//BFDecode1.ar(w,x,y,z,[0pi, 0.25pi, 0.5pi,  0.75pi, pi , 1.25pi, 1.5pi, 1.75pi],[0, 0, 0, 0]); 
//}.play
//)


/**startBinauralFMH{ 
		A.t.proxySpace = ProxySpace.push;
		BinAmbi2O.kemarPath_("KemarHRTF/");
		BinAmbi2O.init('1_4_7_4');

		~in = {var in, w, x, y, z, r, s, t, u, v;
			   #w, x, y, z, r, s, t, u, v = In.ar(ambiBus2.index, 9); 
			   in = FMHtoAmb2O.ar(w, x, y, z, r, s, t, u, v);
			   BinAmbi2O.ar(in)*0.9;
			   //Out.ar(0, BinAmbi2O.ar(in));
			  };
		~in.play;

}*/
