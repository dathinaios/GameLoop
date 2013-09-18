
(
	//GameLoopDecoder('ATK', 'newStereo');
	//GameLoopDecoder('ATK', 'newListen');
	GameLoopDecoder('AmbIEM', 'binaural');
	//Allow time for the decoder to be created....
	~gameloop = GameLoop(40, 40, 1).play(0.05);
	~gameloop.gui; 
)
(
	~camera = Camera2D(
		~gameloop.world,
		position: ~gameloop.world.center,
		radius: 0.8
	);

	~cameraForcePath = {var path;
	/* steering bahavior */
	path = Path(Array.fill(rrand(2.0, 38.0), {RealVector2D[rrand(15, 25.0), rrand(15.0, 25.0)]}), true);
	~camera.force_(
		{ arg entity;
			PathFollowing.calculate(entity,path, 0.5);
		});
	};
	~camrep =Camera2DRepresentation(~gameloop.repManager, Color.white).shape_(1);
	~camera.attach(~camrep);
);

(
	~entity = Vehicle(
		~gameloop.world,
		position: RealVector2D[18, 22],
		radius: 0.4,
		maxSpeed: 10.6
	);

	~rep = SoundRepresentation(
		~gameloop.repManager,
		input:{BPF.ar(in:WhiteNoise.ar(0.2), freq: rrand(100, 19000.0), rq:0.3)} 
		//input: {SoundIn.ar(0)} 
		//collisionFunc: {arg entity, entList; entity.remove; entList.do{arg i; i.remove}}
	 ); 
	//~entity.collisionType_(\mobile);
	~entity.attach(~rep);
	//and a visual representation
	~visrep = SimpleVisual(~gameloop.repManager);
	~entity.attach(~visrep);

	~randForcePath = {var path;
	/* steerinl bahavior */
	path = Path(Array.fill(rrand(2.0, 38.0), {RealVector2D[rrand(15, 25.0), rrand(15.0, 25.0)]}), true);
	~entity.force_(
		{ arg entity;
			PathFollowing.calculate(entity,path, 0.5);
		});
	};
  ~entity.force_({ arg entity; Arrive.calculate(entity, RealVector2D[19,20.2]);});
	~randForcePath.value;
);

~gameloop.clear;
GameLoopDecoder.clear
~gameloop.clearEntities;

~camera.force_({arg ent; Arrive.calculate(ent, RealVector2D[24,20])}); 
~entity.force_({ arg entity; Arrive.calculate(entity, RealVector2D[19,20.2]);});
~cameraForcePath.value;
~randForcePath.value;

~entity.detachAll;
~entity.detach(~rep);
~entity.remove;
~gameloop.repManager.repList;
~camera.moveFwd(2);
~camera.moveBack(2);
~camera.reset;
~camera.rotateRight(0.4pi);
~camera.rotateLeft(0.4pi);
~camera.remove
~gameloop.guiClose; 


~entity.velocity = RealVector2D[0, 0]
~entity.force = {arg entity; Arrive.calculate(entity, RealVector2D[22, 25])};
~entity.force = {arg entity; Arrive.calculate(entity, RealVector2D[16, 22])};
~entity.force = {arg entity; Arrive.calculate(entity, RealVector2D[21, 21])};
~entity.force = {arg entity; Arrive.calculate(entity, RealVector2D[19, 20.5])};

(
	var width, position, radius;
	width = ~gameloop.world.center[0]*2;
	position = RealVector2D[rrand(2.0, width), rrand(2.0, width)];
	radius = 0.4;
	~entity.force_(
		{ arg entity; var position, path;
			position = RealVector2D[rrand(2.0, width), rrand(2.0, width)];
			path = Path(Array.fill(rrand(2.0, 38.0),
			{RealVector2D[position[0] + rrand(-33, 33.0), position[1] + rrand(-33.0, 33.0)]}),true);
			PathFollowing.calculate(entity,path, 0.5);
		});
)

~gameloop.resetCamera;
//clear the entities
~gameloop.clearEntities;
//clear and close everything
~gameloop.clear;
~gameloop.removeCamera;

~gameloop.repManager.repList
GameLoopDecoder.decoderBus.sources;
GameLoopDecoder.decoderProxy.sources

Server.default.queryAllNodes;
GroupManager.inGroup;
GroupManager.tailGroup;
GameLoop.instance.dt
~gameloop.entManager.clear;
~entity.do{arg i; i.remove;};
~entity[1].remove

~gameloop.cameraEntity.collisionType
~gameloop.cameraEntity.moveFwd(4);
~gameloop.cameraEntity.moveBack(4);
~gameloop.cameraEntity.goto(RealVector2D[rrand(5.0, 35.0),rrand(6.0, 30.0)]);
~gameloop.cameraEntity.rotateLeft(0.1pi);
~gameloop.cameraEntity.rotateRight(0.1pi);
~gameloop.cameraEntity.reset;

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
~gameloop.staticList;
~routiniaris.stop

~gameloop.mainRoutine.play
~gameloop.mainRoutine.stop
CmdPeriod.run


//An input and steering behavior to play around with
(
	var width, position, radius;
	width = ~gameloop.center[0]*2;
	position = RealVector2D[rrand(2.0, width), rrand(2.0, width)];
	radius = 0.4;
	~entity = SoundEntity(
		~gameloop.world,
		position,
		radius,
		maxSpeed: 40.0,
		/*
		input:
		{ arg vel;  var in;
			in =0.3*Saw.ar(rrand(500, 20000.0))*
				 LFPulse.ar(linlin(0,10, 5, rrand(10, 20.0)), mul: 0.5, add: 0.5);
			in = BPF.ar(in, rrand(200, 18000.0)*MouseX.kr(0.3, 2, lag: rrand(2.0, 6.0)), 0.4);
		},*/
		//collisionFunc: {arg entity, entList; entity.remove; entList.do{arg i; i.remove}},
		//collisionType: \mobile
	).force_(
		{ arg entity; var position, path;
			position = RealVector2D[rrand(2.0, width), rrand(2.0, width)];
			path = Path(Array.fill(rrand(2.0, 38.0),
			{RealVector2D[position[0] + rrand(-33, 33.0), position[1] + rrand(-33.0, 33.0)]}),true);
			PathFollowing.calculate(entity,path, 0.5);
		});
);

//some test (delete)
~entity.remove;
~entity.collisionType;
~gameloop.clearEntities;
~gameloop.entManager.freeList;
~gameloop.entManager.freeList.choose.remove;
GameLoopDecoder.decoderBus.sources;
//GameLoopDecoder.decoderBus.removeAll;
Server.default.latency = 3;
Server.default.latency;
TempoClock.default.latency;

"This is a test".postln
"This is a test".postln;
"This is a test".postln;
"This is a test".postln;
"This is a test".postln;
"This is a test".postln;

Quarks.gui
Help.methodArgs("LPF.ar")
SCVim.generateTagsFile()
GameLoop(
a = String.new(
String.find()

//TESTS
SinOsc.ar(
GameLoop(
testing.add(
testing.split(
MobileEntity(
ResponseDefs.add(
TGrains.ar(bufnum:0, numChannels)
a = "test string"
a.first()

Server




