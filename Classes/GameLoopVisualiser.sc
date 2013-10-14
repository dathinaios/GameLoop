
GameLoopVisualiser{
			 classvar <instance;
			 var <entManager, <repManager;
			 var <mainView, infoString; 
			 var width = 400, height = 400;
			 var <>meterInPixels = 10;
			 var leftRotationRoutine, rightRotationRoutine, fwdRotationRoutine, backRotationRoutine;

	*new{ arg entManager, repManager;
			if(instance.isNil, 
				{
				^super.newCopyArgs(entManager, repManager).init;
				},
				{"There is already an active instance of GameLoopVisualiser".error;}
			);		
	}

	init{
		instance = this;
		CmdPeriod.add({this.clear});
		this.initCameraRoutines;
	}

	render {
		if(mainView.notNil, {{ mainView.refresh }.defer});
	}

	close {
		if(mainView.notNil, {mainView.close}, {"There is no view open for GameLoopVisualiser".error});
	}

	clear{
		if(mainView.notNil, {mainView.close});
		instance = nil;
	}

	gui{
		this.createMainView;
		this.setViewOptions;
		this.setDrawFunction;
		this.setWindowKeyActions;
	}

	createMainView{ 
		mainView ?? { var text;
			mainView = Window("Visualiser", Rect(-450, 400, width, height), scroll: false);
			infoString= StaticText(mainView, Rect(3, 3, 200, 20)).stringColor_(Color.grey);
		}
	}

	setViewOptions{ 
		mainView.view.background = Color.black;
		mainView.onClose = {mainView = nil; }; 
		mainView.front;
		mainView.alwaysOnTop = true;
	}

	setDrawFunction{
		mainView.drawFunc = {
			infoString.string =	this.getInfoString;
			this.drawEntities(repManager.repList);
		  //this.drawWalls;
		};
	}

	drawEntities{ arg repList; var obstacle;
		repList.size.do { arg index; 
			obstacle = repList[index]; 
			if(obstacle.type == 'visual')
			  {this.drawEntity(obstacle)};
		}
	}

	drawEntity{arg obstacle; 
						 var radiusInPixels, widthInPixels, obstacPos; 
						 var left, top;
						 var h = 400, w = 400;

		obstacPos = obstacle.position;
		radiusInPixels = obstacle.radius * meterInPixels;
		widthInPixels = radiusInPixels + radiusInPixels;

		left = (obstacPos[0]*meterInPixels)-radiusInPixels;
		top  = ((obstacPos[1]*meterInPixels).linlin(0, h, w, 0)) - radiusInPixels;

		if (top > -4 and:{top < 396} and:{left > -4} and:{left<396})
		{
			Pen.width = obstacle.penWidth;
			Pen.color = obstacle.color.alpha_(0.7);
			Pen.beginPath;

			obstacle.draw(Rect(left, top, widthInPixels, widthInPixels));
		};
	}

	drawWalls{
		WallManager.walls.do{arg i; var pointFrom, pointTo;
			pointFrom = i.from * meterInPixels;
			pointTo = i.to * meterInPixels;
			Pen.color = Color.white;
			Pen.line(pointFrom.asPoint, pointTo.asPoint);
		}
	}


	getInfoString{ var string;
		string =   "Ents: " + entManager.activeEntities.asString +
						 "- Reps: " + repManager.activeReps.asString;
		^string;
	}
	/* Shortcuts for control of camera from focused window */

	initCameraRoutines{
		leftRotationRoutine = Routine{ 
			loop{
			Camera2D.instance.rotateLeft;
			0.05.wait;
			};
		};

		rightRotationRoutine = Routine{
			loop{
			Camera2D.instance.rotateRight;
			0.05.wait;
			};
		};

		fwdRotationRoutine = Routine{
			loop{
			Camera2D.instance.forceFwd;
			0.05.wait;
			};
		};

		backRotationRoutine = Routine{
			loop{
			Camera2D.instance.forceBack;
			0.05.wait;
			};
		};
	}

	setWindowKeyActions{
			//Specific mainview setting and keyboard controls
			mainView.view.keyDownAction = 
				{arg view, char, modifiers, unicode, keycode;
					switch (keycode)
					{126}
					{
						if(fwdRotationRoutine.isPlaying.not)
						  {fwdRotationRoutine.reset.play};
					}
					{125}
					{
						if(backRotationRoutine.isPlaying.not)
						  {backRotationRoutine.reset.play};
					}
					{123}
					{
						if(leftRotationRoutine.isPlaying.not)
						  {leftRotationRoutine.reset.play};
					}
					{124}
					{
						if(rightRotationRoutine.isPlaying.not)
						  {rightRotationRoutine.reset.play};
					}
				};

			mainView.view.keyUpAction = 
				{arg view, char, modifiers, unicode, keycode;
					switch (keycode)
					{123}{leftRotationRoutine.stop}
					{124}{rightRotationRoutine.stop}
					{125}{backRotationRoutine.stop}
					{126}{fwdRotationRoutine.stop}
				};

	}

}

