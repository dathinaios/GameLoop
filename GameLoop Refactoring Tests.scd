
(
A.t.entityManager = EntityManager(SpatialHashing(40, 40, 1));
//Make a visualiser
A.t.mainClock = TempoClock.new;
//A.t.visualiser = RenderVisuals([0, A.t.entityManager.center[0]*2], RepresentationManager.repList, 20, 0.5);
//A.t.visualiser.activate;

//The main game loop
A.t.routiniaris = Routine{
inf.do{
	A.t.entityManager.refreshIndex;
	A.t.entityManager.update; 
	A.t.entityManager.collisionCheck; 
	//{A.t.visualiser.render}.defer; // render the representation/view
	0.05.wait;
	}
}.play(A.t.mainClock);

);
//initialize the camera
//ATTENTION!! The movement of the Camera is no longer working

Camera2D.initialize(A.t.entityManager);

(
f = { var path, position, width;
				width = A.t.entityManager.center[0]*2;
				position = MyVector2D[rrand(2.0, width), rrand(2.0, width)];
				path = Path(Array.fill(rrand(8.0, 20.0),{MyVector2D[position[0] + rrand(-1, 1.0), position[1] + rrand(-1, 1.0)]}),true);
				EntityParams([
								'world', A.t.entityManager, 'position', position,
								'radius', 0.1, 'rel', 0.1, 'maxSpeed', rrand(5.0, 40.0),
								//'steering', { arg entity; PathFollowing.calculate(entity,path, 0.5) }, 
				]);
};
//A.t.entity = TestGameFactory.getEntity('SimpleCircle', f.value);
);

(
~entityParams = f.value;
A.t.entity =SimpleCircleEnt(~entityParams);
A.t.entity.attach(SimpleCircleRep(A.t.entity, ~entityParams)); //for visualisation	
A.t.entity.activate;
)

A.t.entityManager.entityList;
RepresentationManager.repList
A.t.routiniaris.stop
A.t.entityManager.clear;

