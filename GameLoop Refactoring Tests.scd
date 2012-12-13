
(
A.t.entityManager = EntityManager(SpatialHashing(40, 40, 1));
//Make a visualiser
A.t.mainClock = TempoClock.new;
A.t.visualiser = RenderVisuals([0, A.t.entityManager.center[0]*2], RepresentationManager.repList, 20, 0.5);
A.t.visualiser.activate;

//The main game loop
A.t.routiniaris = Routine{
inf.do{
	A.t.entityManager.refreshIndex;
	A.t.entityManager.update; 
	A.t.entityManager.collisionCheck; 
	{A.t.visualiser.render}.defer; // render the representation/view
	0.05.wait;
	}
}.play(A.t.mainClock);

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

	A.t.entity =SimpleCircleEnt(world, position, radius, controllerFunc: forceFunc);
	A.t.entity.attach(SimpleCircleRep(A.t.entity)); //for visualisation	
	A.t.entity.activate;

};
//A.t.entity = TestGameFactory.getEntity('SimpleCircle', f.value);
);


35.do{f.value;};
f.value;

A.t.entityManager.entityList;
RepresentationManager.repList
A.t.routiniaris.stop
A.t.entityManager.clear;

