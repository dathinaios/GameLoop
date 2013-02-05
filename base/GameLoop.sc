
GameLoop{
			 classvar <instance;
			 var <sceneWidth, <sceneHeight, <cellSize;
			 var <dt, <mainRoutine, <mainClock;
			 var <entManager, <repManager;

	*new{ arg sceneWidth = 40, sceneHeight = 40, cellSize = 1;

			if(instance.isNil, 
				{
				^super.newCopyArgs(sceneWidth, sceneHeight, cellSize).init;
				},
				{"There is already an active instance of GameLoop".error;}
			);		
	}

	init{
		dt = 0.05; //20 FPS
		mainClock = TempoClock.new;
		entManager = EntityManager(SpatialHashing(sceneWidth, sceneHeight, cellSize));
		instance = this;
		//create and run the representation manager
		repManager = RepresentationManager(this);
		repManager.run;
	}

	world{ // at the moment we are returning the entManager
		   // but in the future we could return one of
		   // a collection of worlds
		^entManager
	}

	play{ arg rate; //start the gameloop at framerate rate
		if (mainRoutine.isNil,
			{ //1st condition
			  dt = rate ?? dt; //TODO: not very elegant. 
				mainRoutine = Routine{
					inf.do{
						instance.refreshIndex1; //unregisterAll
						instance.update; 
						instance.refreshIndex2; //reregisterAll
						instance.collisionCheck; 
						{repManager.render}.defer; //render!!
						dt.wait;
						}
				}.play(mainClock)
			}, 
			{ //2nd condition
			  mainRoutine.reset.play;
			}
		);
	}

	stop{
		mainRoutine.stop;
		//mainRoutine.reset;
	}

	addCamera{
		repManager.addCamera;
	}

	removeCamera{
		repManager.removeCamera;
	}

	center{
		^RealVector2D[sceneWidth * 0.5, sceneHeight*0.5];
	}

}
