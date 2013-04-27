CmdPeriod.run


(
//AmbiDecoderCentre.startDecoder2('stereo');
//At the moment the first time it is giving an error. Run again.
GameLoopDecoder('AmbIEM', 'binaural');
//Allow time for the decoder to be created....
//~gameloop = EntityManager(SpatialHashing(40, 40, 1)).play(0.05);
~gameloop = GameLoop(40, 40, 1).play(0.05);
~gameloop.addCamera;
~entity = List.new;
);

(
	var width, position, radius;
	width = ~gameloop.center[0]*2;
	position = RealVector2D[rrand(2.0, width), rrand(2.0, width)];
	radius = 0.4; 
	~entity.add(MUEnt(
		~gameloop.world, 
		position, 
		radius, 
		maxSpeed: 40.0, 
		input: 
		{ arg vel;  var in;
			in =SinOsc.ar(rrand(500, 20000.0))*
				 LFPulse.ar(linlin(0,10, 5, rrand(10, 20.0)), mul: 0.5, add: 0.5);
			in = BPF.ar(in, rrand(200, 18000.0)*MouseX.kr(0.3, 2, lag: rrand(2.0, 6.0)), 0.4);
		}
		//collisionFunc: {arg entList, entity; entity.remove; entList.do{arg i; i.remove}}
	);
	); 
	//~entity.collisionType_(\mobile);
	~entity.last.activate;
);

~gameloop.entManager.clear;
a = GameLoopDecoder.getEncoder();
a.class
Quarks.gui
GameLoop.instance.dt;

GameLoopDecoder.decoderBus.sources;
Server.default.queryAllNodes;
GroupManager.inGroup;
GroupManager.tailGroup; 

~gameloop.entManager.clear;
~entity.do{arg i; i.remove;};
~entity[1].remove

~gameloop.repManager.cameraEntity.moveFwd(4);
~gameloop.repManager.cameraEntity.moveBack(4);
~gameloop.repManager.cameraEntity.goto(RealVector2D[rrand(5.0, 35.0),rrand(6.0, 30.0)]);
~gameloop.repManager.cameraEntity.rotateLeft(0.1pi);
~gameloop.repManager.cameraEntity.rotateRight(0.1pi);
~gameloop.repManager.cameraEntity.reset;
~gameloop.repManager.repList

(
r = Routine{ //test
	loop{
	~gameloop.repManager.cameraEntity.rotateRight(0.05pi);
	0.05.wait;
	};
}.play;
)

r.stop

~gameloop.clear;
~gameloop.removeCamera;
~gameloop.stop
~gameloop.play
~gameloop.world.freeList;
~gameloop.mobList;
~gameloop.staticList;
~routiniaris.stop

~gameloop.mainRoutine.play
~gameloop.mainRoutine.stop
CmdPeriod.run

//Trying things out for using NodeProxies for SoundRepresentations

~in     = NodeProxy.audio.fadeTime_(4);
~out    = NodeProxy.audio.fadeTime_(4);
~lfo    = NodeProxy.control(Server.default, 1).fadeTime_(4); //Dictionary.new;
~filter = NodeProxy.audio.fadeTime_(4);
~effect = NodeProxy.audio.fadeTime_(4);


AmbiDecoderCentre.bus.index;
Server.default;
GroupManager.inGroup;
~in.group = GroupManager.inGroup;
~out.group = GroupManager.tailGroup;
{Out.ar(AmbiDecoderCentre.bus.index, SinOsc.ar(440)*0.2)}.play
~in.source = {Impulse.ar(27)};
~out.source = { Out.ar(AmbiDecoderCentre.bus.index, ~in.ar)};

(
~in.release(2);
~out.release(2);
)

//Could I use this structure as a backdrop again?
this.makeIn(\none);
this.makeLFO(\none);
this.makeFilter(\none);
this.makeEffect(\none);

f = { var position, radius, width;

	width = ~gameloop.center[0]*2;
	position = RealVector2D[rrand(2.0, width), rrand(2.0, width)];
	radius = 0.4; 
	~entity = MUEnt(
		~gameloop.world, 
		position, 
		radius, 
		maxSpeed: 40, 
		//collisionFunc: {arg entList, entity; entity.remove; entList.do{arg i; i.remove}}
	); 
	//~entity.collisionType_(\mobile);
	~entity.activate;
};
50.do{f.value;};


// ------------------------------------------------------------
//AMBISONIC TESTS

(
~encoder = FoaEncoderMatrix.newOmni;
~decoder = FoaDecoderKernel.newCIPIC;     // kernel decoders should be freed after use!!
                                         // free below...
)

// free kernel
~decoder.free

(
~test = {
    var sig;// audio signal
    var angle, azim;            // angle and azimuth control


    // display encoder and decoder
    "Ambisonic encoding via % encoder".format(~encoder.kind).postln;
    "Ambisonic decoding via % decoder".format(~decoder.kind).postln;

    // angle ---> top         = push to plane wave
    //            bottom        = omni-directional
    angle = MouseY.kr(pi/2, 0);

    // azimuth -> hard left     = back
    //          centre     = centre
    //          hard right     = back
    azim = MouseX.kr(pi, -pi);


    // ------------------------------------------------------------
    // test sig
    sig = PinkNoise.ar;             // mono pink noise


    // ------------------------------------------------------------
    // encode
    sig = FoaEncode.ar(sig, ~encoder);

    // ------------------------------------------------------------
    // transform
    sig = FoaTransform.ar(sig, 'push', angle, azim);


    // ------------------------------------------------------------
    // decode (to binaural)
    FoaDecode.ar(sig, ~decoder);

}.play;
)
	x = 2;
	y = 0.5;
	atan2(y,x);
	hypot(x,y);

