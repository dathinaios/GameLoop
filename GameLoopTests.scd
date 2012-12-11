Server.killAll;

(
AmbiDecoderCentre.startDecoder2('stereo');
//AmbiDecoderCentre.startBinaural;
A.t.entityManager = EntityManager2(SpatialHashing(40, 40, 1));
//Make a visualiser
A.t.mainClock = TempoClock.new;
A.t.visualiser = RenderVisuals([0, A.t.entityManager.center[0]*2], RepresentationManager.repList, 20, 0.5);
A.t.visualiser.activate;
//The main game loop
Routine{
inf.do{
	A.t.entityManager.refreshIndex1; //unregisterAll
	A.t.entityManager.update; 
	A.t.entityManager.refreshIndex2; //reregisterAll
	A.t.entityManager.collisionCheck; 
	{A.t.visualiser.render}.defer; // render the representation/view
	0.05.wait;
	}
}.play(A.t.mainClock);

//initialize the camera
//ATTENTION!! The movement of the Camera is no longer working

Camera2D.initialize(A.t.entityManager);

//buffers

BufferPool.new;
BufferPool.loadPreset('DotFly');

);


(
f = { var path, position, width;
				width = A.t.entityManager.center[0]*2;
				position = MyVector2D[rrand(2.0, width), rrand(2.0, width)];
				path = Path(Array.fill(rrand(8.0, 20.0),{MyVector2D[position[0] + rrand(-1, 1.0), position[1] + rrand(-1, 1.0)]}),true);
				EntityParams([
								'world', A.t.entityManager, 'position', position, //'elev', rrand(-pi, pi),
								'radius', 0.1, 'rel', 0.1, 'maxSpeed', rrand(5.0, 40.0), //rrand(5.0, 40.0), //up to 70 sounds cool
								'steering', {
											 arg entity; PathFollowing.calculate(entity,path, 0.5)
											}, 
								'input', {	
											arg vel, gate; var in, velc;
											velc = vel.linlin(0, 10, 0.1, Rand(134, 325));
											in = Impulse.ar(velc) * 0.3;
											in = in + Saw.ar(Rand(200, 14000),0.1* LFCub.kr(velc, mul: 0.5, add: 0.5));											in = BPF.ar(in, vel.linlin(0, 10, Rand(20, 20000), Rand(20, 20000)).clip(20, 20000), 0.1);
									 		in = in*Fade2.kr(0.015, 1.0, gate)*0.5;
									 	 }
				]);
};
Camera2D.instance.reset;
A.t.entity = TestGameFactory.getEntity('MobileUnit', f.value);
);

A.t.entity = TestGameFactory.getEntity('MobileUnit', f.value);



/////////////

(
f = { var path, position, width;
				width = A.t.entityManager.center[0]*2;
				position = MyVector2D[rrand(2.0, width), rrand(2.0, width)];
				path = Path(Array.fill(rrand(8.0, 20.0),{MyVector2D[position[0] + rrand(-1, 1.0), position[1] + rrand(-1, 1.0)]}),true);
				EntityParams([
				'world', A.t.entityManager, 'position', position, //'elev', rrand(-pi, pi),
				'radius', 0.1, 'rel', 0.1, 'maxSpeed', 0, //rrand(0.3, 1.3)
				'steering', {arg entity; PathFollowing.calculate(entity, path, 0.5)}, 
				'input', {
							arg vel, gate; var in, velc, bufnum;
							bufnum = BufferPool.instance.buffersDict.choose.choose.bufnum;
							PlayBuf.ar(1, bufnum, BufRateScale.kr(bufnum), loop: 1) * 
							Fade2.kr(0.015, 1.0, gate); 
					 	  }
				])};
				
A.t.entity = TestGameFactory.getEntity('MobileUnit', f.value);
);

A.t.entity = TestGameFactory.getEntity('MobileUnit', f.value);

//////////////////
//==============\\
// Just a super simple example to remember how this works


(
f = { var path, position, width;
				width = A.t.entityManager.center[0]*2;
				position = MyVector2D[rrand(2.0, width), rrand(2.0, width)];
				path = Path([MyVector2D[3, 20], MyVector2D[17, 20]],true);
				EntityParams([
				'world', A.t.entityManager, 'position', position, //'elev', rrand(-pi, pi),
				'radius', 0.1, 'rel', 0.1, 'maxSpeed', 10.0, //rrand(0.3, 1.3)
				'steering', {arg entity; PathFollowing.calculate(entity, path, 0.5)}, 
				'input', {
							arg vel, gate;
							Impulse.ar(17) * Fade2.kr(0.015, 1.0, gate); 
					 	  }
				])};
				
A.t.entity = TestGameFactory.getEntity('MobileUnit', f.value);
);

A.t.entity.remove;
