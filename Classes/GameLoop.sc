
GameLoop{
			 classvar <instance;
			 var <sceneWidth, <sceneHeight, <cellSize;
			 var <entManager, <repManager, gui, visualiser;
			 var <mainRoutine;

	*new{ arg sceneWidth = 40, sceneHeight = 40, cellSize = 1;
			if(instance.isNil, 
				{
				^super.newCopyArgs(sceneWidth, sceneHeight, cellSize).init;
				},
				{"There is already an active instance of GameLoop".error;}
			);		
	}

	init{
		instance   = this;
		entManager = EntityManager(SpatialHashing(sceneWidth, sceneHeight, cellSize));
		repManager = RepresentationManager.new;
		visualiser   = GameLoopVisualisation(entManager,repManager);
		gui = GameLoopGUI(entManager,repManager);
		CmdPeriod.add({this.clear});
	}

	visualiser{ 
		visualiser.gui;
	}

	visualiserClose{
		visualiser.close;
	}

	gui{
		gui.gui;
	}

	guiClose{
		gui.close;
	}

	world{ //could be a collection of worlds...
		^entManager
	}

	play{ arg rate;
		if (mainRoutine.isNil,
			{ 
			  if(rate != nil, {entManager.dt = rate});
				mainRoutine = Routine{
					inf.do{
						entManager.doAll;
						{visualiser.render}.defer;
						entManager.dt.wait;
						}
				}.play(TempoClock.default)
			}, 
			{
			  mainRoutine.reset.play;
			}
		);
	}

	stop{
		mainRoutine.stop;
	}

	center{
		^RealVector2D[sceneWidth * 0.5, sceneHeight*0.5];
	}

	dt{
		^entManager.dt;
	}

	clear{
		mainRoutine.stop;
		entManager.clear;
		visualiser.clear;
		instance = nil;
	}

	clearEntities{
		entManager.clear;
	}

}
