
(
//create the manger and start the game loop at 20 frames per second
A.t.entityManager = EntityManager(SpatialHashing(40, 40, 1)).play(0.05);
A.t.entityManager.addCamera;

f = { var position, radius, width;

	width = A.t.entityManager.center[0]*2;
	position = RealVector2D[rrand(2.0, width), rrand(2.0, width)];
	radius = 0.4; 

	A.t.entity = SimpleCircle(
		A.t.entityManager, 
		position, 
		radius, 
		maxSpeed: 40, 
		//collisionFunc: {arg entList, entity; entity.remove; entList.do{arg i; i.remove}}
	); 
	//A.t.entity.collisionType_(\mobile);
	A.t.entity.activate;
};
);

A.t.entityManager.repManager.cameraEntity.moveFwd(4);
A.t.entityManager.repManager.cameraEntity.moveBack(4);
A.t.entityManager.repManager.cameraEntity.goto(RealVector2D[rrand(5.0, 35.0),rrand(6.0, 30.0)]);
A.t.entityManager.repManager.cameraEntity.rotateLeft(0.1pi);
A.t.entityManager.repManager.cameraEntity.rotateRight(0.1pi);

50.do{f.value;};
f.value;

A.t.entityManager.clear;
A.t.entityManager.removeCamera;
A.t.entityManager.stop
A.t.entityManager.play
A.t.entityManager.freeList;
A.t.entityManager.mobList;
A.t.entityManager.staticList;
RepresentationManager.repList
A.t.routiniaris.stop

A.t.entityManager.mainRoutine.play
A.t.entityManager.mainRoutine.stop

1.class.asString
a = SimpleCircleEnt.new;
(A.t.entity.class.asString++"Controller").postln.asSymbol.asClass.new;
SimpleCircle(A.t.entityManager, RealVector2D[2.0,2.0], 0.9, maxSpeed: 5).activate;
Camera2D(A.t.entityManager, RealVector2D[2.0,2.0], 0.9, maxSpeed: 5).activate;

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
