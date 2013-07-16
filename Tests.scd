(
	//AmbiDecoderCentre.startDecoder2('stereo');
	//At the moment the first time it is giving an error. Run again.
	GameLoopDecoder('AmbIEM', 'binaural');
	//GameLoopDecoder('ATK', 'newStereo');
	//GameLoopDecoder('ATK', 'newListen');
	//Allow time for the decoder to be created....
	//~gameloop = EntityManager(SpatialHashing(40, 40, 1)).play(0.05);
	~gameloop = GameLoop(40, 40, 1).play(0.05);
	//~gameloop.addCamera;
);

(

	~entity = Vehicle(
		~gameloop.world,
		position: RealVector2D[18, 22],
		radius: 0.4,
		maxSpeed: 10.0
	).init;

	~rep = SoundRepresentation(
		~gameloop.repManager,
		input: nil //{WhiteNoise.ar(0.1)} 
		//collisionFunc: {arg entList, entity; entity.remove; entList.do{arg i; i.remove}}
	);
	//~entity.collisionType_(\mobile);
	~entity.attach(~rep);

	{var path;
	/* steering bahavior */
	path = Path(Array.fill(rrand(2.0, 38.0), {RealVector2D[rrand(3, 33.0), rrand(3.0, 33.0)]}), true);

	~entity.force_(
		{ arg entity;
			PathFollowing.calculate(entity,path, 0.5);
		});
	}.value;

);

//with entity everything is removed
~entity.remove;
~rep.remove;
~gameloop.clearEntities;

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
GameLoopDecoder.clear
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




