
(
//create the manger and start the game loop at 20 frames per second
~entityManager = EntityManager(SpatialHashing(40, 40, 1)).play(0.05);
~entityManager.addCamera;

f = { var position, radius, width;

	width = ~entityManager.center[0]*2;
	position = RealVector2D[rrand(2.0, width), rrand(2.0, width)];
	radius = 0.4; 

	~entity = SimpleCircle(
		~entityManager, 
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

~entityManager.repManager.cameraEntity.moveFwd(4);
~entityManager.repManager.cameraEntity.moveBack(4);
~entityManager.repManager.cameraEntity.goto(RealVector2D[rrand(5.0, 35.0),rrand(6.0, 30.0)]);
~entityManager.repManager.cameraEntity.rotateLeft(0.1pi);
~entityManager.repManager.cameraEntity.rotateRight(0.1pi);
~entityManager.repManager.cameraEntity.reset;

(
r = Routine{ //test
	loop{
	~entityManager.repManager.cameraEntity.rotateRight(0.05pi);
	0.05.wait;
	};
}.play;
)

r.stop

~entityManager.clear;
~entityManager.removeCamera;
~entityManager.stop
~entityManager.play
~entityManager.freeList;
~entityManager.mobList;
~entityManager.staticList;
RepresentationManager.repList
~routiniaris.stop

~entityManager.mainRoutine.play
~entityManager.mainRoutine.stop

1.class.asString
a = SimpleCircleEnt.new;
(~entity.class.asString++"Controller").postln.asSymbol.asClass.new;
SimpleCircle(~entityManager, RealVector2D[2.0,2.0], 0.9, maxSpeed: 5).activate;
Camera2D(~entityManager, RealVector2D[2.0,2.0], 0.9, maxSpeed: 5).activate;

MyVector2D[-24,-2].normalize.mag
RealVector2D[-24,-2].normalize.norm

(
{
	var entPos, newPos, theta, thetaSin, thetaCos, rad, x,y;
	var xMinusx, yMinusy;
	var position;

	position = RealVector2D[25, 23];
	entPos = RealVector2D[24,20];
	theta = RealVector2D[23, 10].norm.debug("theta");
	thetaSin = theta.sin;
	thetaCos = theta.cos;
	xMinusx = entPos[0] - position[0];
	yMinusy = entPos[1] - position[1];
	x = (xMinusx * thetaCos) - (yMinusy * thetaSin);
	y = (xMinusx * thetaSin) + (yMinusy * thetaCos);
	RealVector2D[x,y];
}.value
)

CmdPeriod.run
