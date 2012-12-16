
(
//create the manger and start the game loop at 20 frames per second
A.t.entityManager = EntityManager(SpatialHashing(40, 40, 1)).play(0.05);

f = { var path, position, width;
	  var world, radius, mass, velocity, controller,
		  heading, side, maxSpeed, maxForce, maxTurnRate, forceFunc;

	width = A.t.entityManager.center[0]*2;
	position = RealVector[rrand(2.0, width), rrand(2.0, width)];
	path = Path(Array.fill(rrand(8.0, 20.0),{RealVector[position[0] + rrand(-1, 1.0), position[1] + rrand(-1, 1.0)]}),true);
	world =  A.t.entityManager;
	radius = 0.4; 
	maxSpeed = rrand(5.0, 40.0);
	//forceFunc = { arg entity; "BBBBBEEEEEEEO!!!!".postln; PathFollowing.calculate(entity,path, 0.5)};

	A.t.entity =SimpleCircle(world, position, radius); //.collisionType_(\mobile);
	A.t.entity.attach(SimpleCircleRep(A.t.entity)); //for visualisation	
	A.t.entity.activate;
};
);


50.do{f.value;};
f.value;

A.t.entityManager.stop
A.t.entityManager.freeList;
A.t.entityManager.mobList;
A.t.entityManager.staticList;
RepresentationManager.repList
A.t.routiniaris.stop
A.t.entityManager.clear;

1.class.asString
a = SimpleCircleEnt.new;
(A.t.entity.class.asString++"Controller").postln.asSymbol.asClass.new;
