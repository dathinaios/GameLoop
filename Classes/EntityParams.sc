/*
	TODO Not very efficient that I am moving the list to the dictionary. Hacking away?
*/

EntityParams{ var list, dict; // type of entity
	
	*new { arg list; 
	^super.newCopyArgs(list).init;
	}
	
	init{
		dict = Dictionary.new;
		list.pairsDo{
			arg name, value;
			dict.put(name, value)
			};
	}
	
	get{ ^dict
	}
	
	set{arg name, value;
		^dict.put(name, value);
	}
	
/*	at{ arg name;
		list.
	}*/

}


//Example of usage

/*

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

Camera2D.initialize(A.t.entityManager);

//buffers

BufferPool.new;
BufferPool.loadPreset("Laughter_1");

);
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
					 	  },
				'type', 'mob',
				'collisionFunc', {"Boom".postln;}

				])};
				
A.t.entity = TestGameFactory.getEntity('MobileUnit', f.value);
);

*/
