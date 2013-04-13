
// ATK SynthDef / Synth examples
Server.default = s = Server.internal.boot;

(
~in     = NodeProxy(Server.default, \audio, 16);
~in.group = GroupManager.between1; //leave th inGroup for control signals
~out    = NodeProxy(Server.default, \audio, 2);
~out.group = GroupManager.between3;
);

(
//ATK ENCODING DECODING /*{{{*/
//SpacePolarATKDp
~in.source =  {
	var x,y,in, foa, azimuth, radius;
	in = Impulse.ar(LFDNoise1.ar(0.3,50, 200));
    // ------------------------------------------------------------
	y = MouseX.kr(10,-10);
	x = MouseY.kr(10,-10);
	azimuth = atan2(y,x);
	radius = hypot(x,y);
	SpacePolarATKDp.ar(in, azimuth, radius);
};
)

~decoder = FoaDecoderKernel.newListen(1013);
//~decoder = FoaDecoderKernel.newSpherical;
//~decoder = FoaDecoderKernel.newCIPIC(165);
//~decoder = FoaDecoderKernel.newUHJ;

(
~out.source = {arg inBus;
        var foa, out;
        foa = \in.ar(0!16);
        out = FoaDecode.ar(foa, ~decoder);
        Out.ar(0, out);
};
)/*}}}*/

~out <<> ~in;

//AMBIEM ENCODING DECODING /*{{{*/
(
~in.source =  {
	var x,y,in, foa, azimuth, radius;
	in = Impulse.ar(LFDNoise1.ar(0.3,50, 200));
    // ------------------------------------------------------------
	x = MouseX.kr(-10,10);
	y = MouseY.kr(10,-10);
	azimuth = atan2(y,x);
	radius = hypot(x,y);
	SpacePolarAmbIEMDp.ar(in, azimuth, radius);
};
)

//initialise the HRTF's
(
BinAmbi3O.kemarPath_("/Applications/SuperCollider/KemarHRTF");
BinAmbi3O.init('1_4_7_4');
)

(
~out.source = {arg inBus;
        var in, out;
        in = \in.ar(0!16);
		out = BinAmbi3O.ar(in);
        Out.ar(0, out);
};
)/*}}}*/

//Free everything

(
~in.free; // free the encoder
~out.free; // free the decoder
~kernelDecoder.free; // free kernel decoder, if used
)

////////////
////////////
////////////
//using the GameLoopDecoder

GameLoopDecoder('AmbIEM', 'binaural')
GameLoopDecoder.getDecoder <<> ~in;
//AMBIEM ENCODING DECODING
(
~in.source =  {
	var x,y,in, foa, azimuth, radius;
	in = Impulse.ar(LFDNoise1.ar(0.3,50, 200));
    // ------------------------------------------------------------
	x = MouseX.kr(-10,10);
	y = MouseY.kr(10,-10);
	azimuth = atan2(y,x);
	radius = hypot(x,y);
	GameLoopDecoder.getEncoder.ar(in, azimuth, radius);
};
);

GameLoopDecoder.getDecoder.free

(
~in     = NodeProxy(Server.default, \audio, 2);
~in2     = NodeProxy(Server.default, \audio, 2);
~out    = NodeProxy(Server.default, \audio, 2);
);

~in.source = {Out.ar(65, Pan2.ar(Impulse.ar(MouseX.kr(20, 100)), -1))};
~in2.source = {Out.ar(65, Pan2.ar(Saw.ar(MouseX.kr(20, 100)) * 0.5, 1))};
~out.source = {Out.ar(0, In.ar(65, 2))}
~out <<> ~in;
~out <<> ~in2;
