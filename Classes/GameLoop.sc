
GameLoop{
			 classvar <instance;
			 var <sceneWidth, <sceneHeight, <cellSize;
			 var <entManager, <repManager, renderer;
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
		renderer   = GameLoopRender(entManager,repManager);

		CmdPeriod.add({this.clear});

		/* visuals */
		renderer.gui;
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
						{renderer.render}.defer;
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
		entManager.clear;
		renderer.clear;
		instance = nil;
	}

	clearEntities{
		entManager.clear;
	}

}
