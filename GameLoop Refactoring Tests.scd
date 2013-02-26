
(
AmbiDecoderCentre.startDecoder2('stereo');
//~gameloop = EntityManager(SpatialHashing(40, 40, 1)).play(0.05);
~gameloop = GameLoop(40, 40, 1).play(0.05);
~gameloop.addCamera;
);

(
	var width, position, radius;
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
);

Server.default.queryAllNodes;
GroupManager.inGroup;
GroupManager.tailGroup; 

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
