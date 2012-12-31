
(
//create the manger and start the game loop at 20 frames per second
A.t.entityManager = EntityManager(SpatialHashing(40, 40, 1)).play(0.05);

f = { var position, radius, width;

	width = A.t.entityManager.center[0]*2;
	position = RealVector[rrand(2.0, width), rrand(2.0, width)];
	radius = 0.4; 

	A.t.entity = SimpleCircle(
		A.t.entityManager, 
		position, 
		radius, 
		maxSpeed: 40, 
		//collisionFunc: {arg entList, entity; entity.remove; entList.do{arg i; i.remove}}
	); 
	A.t.entity.collisionType_(\mobile);
	A.t.entity.activate;
};
);


50.do{f.value;};
f.value;

A.t.entityManager.clear;
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
SimpleCircle(A.t.entityManager, RealVector[2.0,2.0], 0.9, maxSpeed: 5).activate;
