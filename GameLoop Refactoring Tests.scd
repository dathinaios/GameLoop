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
	~entity.add(SoundEntity(
		~gameloop.world, 
		position: RealVector2D[18, 22], 
		radius: 0.4, 
		maxSpeed: 10.0, 
		input: nil, //input can be anything. Velocity is passed as a viariable
		forceFunc: {arg entity; Arrive.calculate(entity, RealVector2D[20, 20.5])}
		//collisionFunc: {arg entList, entity; entity.remove; entList.do{arg i; i.remove}}
	);
	); 
	//~entity.collisionType_(\mobile);
	~entity.last.activate;
);


~entity[0].forceFunc_({arg entity; Arrive.calculate(entity, RealVector2D[22, 25])});
~entity[0].forceFunc_({arg entity; Arrive.calculate(entity, RealVector2D[16, 22])});
~entity[0].forceFunc_({arg entity; Arrive.calculate(entity, RealVector2D[21, 21])});
~entity[0].forceFunc_({arg entity; Arrive.calculate(entity, RealVector2D[19, 20.5])});
// chase the entity at [1]
~entity[0].forceFunc_({arg entity; Seek.calculate(entity, ~entity[1].position)});

~gameloop.entManager.clear;
a = GameLoopDecoder.getEncoder();
a.class
Quarks.gui
GameLoop.instance.dt;

GameLoopDecoder.decoderBus.sources;
Server.default.queryAllNodes;
GroupManager.inGroup;
GroupManager.tailGroup; 
GameLoop.instance.dt
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


//An input and steering behavior to play around with
(
	var width, position, radius;
	width = ~gameloop.center[0]*2;
	position = RealVector2D[rrand(2.0, width), rrand(2.0, width)];
	radius = 0.4; 
	~entity.add(SoundEntity(
		~gameloop.world, 
		position, 
		radius, 
		maxSpeed: 40.0, 
		input: 
		{ arg vel;  var in;
			in =SinOsc.ar(rrand(500, 20000.0))*
				 LFPulse.ar(linlin(0,10, 5, rrand(10, 20.0)), mul: 0.5, add: 0.5);
			in = BPF.ar(in, rrand(200, 18000.0)*MouseX.kr(0.3, 2, lag: rrand(2.0, 6.0)), 0.4);
		},
		forceFunc:
		{ arg entity; var position, path;
			position = RealVector2D[rrand(2.0, width), rrand(2.0, width)];
			path = Path(Array.fill(rrand(2.0, 38.0),
			{RealVector2D[position[0] + rrand(-33, 33.0), position[1] + rrand(-33.0, 33.0)]}),true);
			PathFollowing.calculate(entity,path, 0.5);
		}
		//collisionFunc: {arg entList, entity; entity.remove; entList.do{arg i; i.remove}}
	);
	); 
	//~entity.collisionType_(\mobile);
	~entity.last.activate;
);
