
(
~centerr = 40;
//Make a visualiser
A.t.visualiser = RenderVisuals([0, ~centerr], RepresentationManager.repList, 20, 0.5);
A.t.entityManager = EntityManager({{A.t.visualiser.render}.defer}, SpatialHashing(40, 40, 1));
A.t.visualiser.activate;

//The main game loop
A.t.entityManager.play(0.05); //plays at the dafault 0.05 frame rate


f = { var path, position, width;
	  var world, radius, mass, velocity, controller,
		  heading, side, maxSpeed, maxForce, maxTurnRate, forceFunc;

	width = A.t.entityManager.center[0]*2;
	position = RealVector[rrand(2.0, width), rrand(2.0, width)];
	path = Path(Array.fill(rrand(8.0, 20.0),{RealVector[position[0] + rrand(-1, 1.0), position[1] + rrand(-1, 1.0)]}),true);
	world =  A.t.entityManager;
	radius = 0.4; 
	maxSpeed = rrand(5.0, 40.0);
	forceFunc = { arg entity; "BBBBBEEEEEEEO!!!!".postln; PathFollowing.calculate(entity,path, 0.5)};

	A.t.entity =SimpleCircle(world, position, radius);
	A.t.entity.attach(SimpleCircleRep(A.t.entity)); //for visualisation	
	A.t.entity.activate;
};
);


35.do{f.value;};
f.value;
A.t.entity.collisionType
A.t.entity;
A.t.entityManager.stop
Thread.killAll

A.t.entityManager.freeList;
A.t.entityManager.mobList;
A.t.entityManager.staticList;
RepresentationManager.repList
A.t.entityManager.clear;

1.class.asString
a = SimpleCircleEnt.new;
(A.t.entity.class.asString++"Controller").postln.asSymbol.asClass.new;
