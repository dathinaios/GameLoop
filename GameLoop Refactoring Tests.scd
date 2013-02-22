
(
AmbiDecoderCentre.startDecoder2('stereo');
//create the manger and start the game loop at 20 frames per second
//~gameloop = EntityManager(SpatialHashing(40, 40, 1)).play(0.05);
~gameloop = GameLoop(40, 40, 1).play(0.05);
~gameloop.addCamera;

f = { var position, radius, width;

	width = ~gameloop.center[0]*2;
	position = RealVector2D[rrand(2.0, width), rrand(2.0, width)];
	radius = 0.4; 

	~entity = MUEnt(
		//~gameloop, 
		~gameloop.world, 
		position, 
		radius, 
		maxSpeed: 40, 
		//collisionFunc: {arg entList, entity; entity.remove; entList.do{arg i; i.remove}}
	); 
	//~entity.collisionType_(\mobile);
	~entity.activate;
};
);

50.do{f.value;};
f.value;
~gameloop.entManager.clear;
~entity.remove

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

//Could I use this structure as a backdrop again?
this.makeIn(\none);
this.makeLFO(\none);
this.makeFilter(\none);
this.makeEffect(\none);

mainOut = NodeProxy.audio.fadeTime_(4);
mainOut[0] = {out.ar};	

~in.source = {SinOsc.ar(440)};
~in.source = {0};
~out.source = {Out.ar(0, ~in.ar)};

out.source = { arg globalGain = 0.5;
	var input, rate, mul = 1;
	input = effect.ar;
	input = input * globalGain;
	rate = Impulse.kr(15);
	// measure Peak
	SendReply.kr(rate, ("/levels"+ instanceNum).asSymbol, Peak.ar(input, rate)); //[Amplitude.kr(input, 0.05, 0.05)], K2A.ar(Peak.ar(input, delrate).lag(0, 3))]);	
	input;
	//Out.ar(0, input)
};

in[0] = {
	arg freq = 0.5, amp = 0.5, pan = 0.5, detune = 0.5, detune2 = 0.5, trunc = 0; 
	var in;
	freq = \freq.asSpec.map(freq);
	trunc = \trunc.asSpec.map(trunc);
	pan = \pan.asSpec.map(pan);
	detune= \detune.asSpec.map(detune);
	detune2= \detune2.asSpec.map(detune2);
	amp = amp*0.5;
	in = Saw.ar(freq, amp);
	in = in + Saw.ar(freq + detune + detune2, amp);
	in = in.trunc(trunc);
	Pan2.ar(in, pan);
};
