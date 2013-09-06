TestGameLoop : UnitTest{	var gameloop;

		setUp {
			gameloop = GameLoop(40, 40, 1);
			/* gameloop.play(0.05); */
		}

		tearDown {
			gameloop.clear;
		}
		 
		test_init{
	  	this.assert( gameloop.entManager.class == EntityManager, "EntityManager has been initialised");
	  	this.assert( gameloop.repManager.class == RepresentationManager, "RepresentationManager has been initialised");
	  	this.assert( gameloop.visualisation.class == GameLoopVisualisation, "GameLoopVisualisation has been initialised");
		}
}
