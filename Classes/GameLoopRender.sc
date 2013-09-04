
GameLoopRender{
			 classvar <instance;
			 var <entManager, <repManager;
			 var <mainRoutine;
			 var dimensions, gridSize, cellSize, <mainView;
			 var <cameraEntity, <cameraActive = false, leftRotationRoutine, rightRotationRoutine;

	*new{ arg entManager, repManager;
			if(instance.isNil, 
				{
				^super.newCopyArgs(entManager, repManager).init;
				},
				{"There is already an active instance of GameLoopRender".error;}
			);		
	}

	init{
		instance = this;
		CmdPeriod.add({this.clear});
		cameraActive = true;

		dimensions = [0, entManager.center[0]*2];

		leftRotationRoutine = Routine{ 
			loop{
			cameraEntity.rotateLeft(0.01pi);
			0.05.wait;
			};
		};

		rightRotationRoutine = Routine{
			loop{
			cameraEntity.rotateRight(0.01pi);
			0.05.wait;
			};
		};
		//run the visualisation
		/* this.gui; */
	}

	render {
			mainView.refresh;
	}

	close {
		mainView.close;
	}

	addCamera{  
		cameraEntity = Camera2D(
			entManager, 
			position: entManager.center,
			radius: 0.5,
			mass: 0.04,
			maxSpeed: 20
		); 
	}

	removeCamera{
		entManager.remove(cameraEntity);
		cameraEntity.changed(\remove);
		cameraActive = false;
		Camera2D.instance = nil;
	}

	resetCamera{
		cameraEntity.reset;	
	}

	clear{
		mainView.close;
		instance = nil;
	}

	gui{

		var   h = 400, v = 400, seed, run = true,  spaceUnits, spaceUnits2, meterInPixels,  speakerRadInPixels;
		var text;
		mainView = Window("Visuals", Rect(-450, 400, h, v), false);
		mainView.view.background = Color.black;
		mainView.onClose = { run = false }; // stop the thread on close
		mainView.front;
		mainView.alwaysOnTop = true;
		//for  space of 700 pixels is 20 meters one meter is 35 pixels
		meterInPixels = h/(dimensions[1]-dimensions[0]); //assumes h = v
		speakerRadInPixels = 2 * meterInPixels;
		mainView.drawFunc= {
		Pen.width = 2;

		/* display some useful info */
		text = StaticText(mainView, Rect(3, 3, 200, 20)).stringColor_(Color.grey);
		//to draw the obstacles 
		
		mainView.drawFunc = {

		/* Pen.use { */
			var divisions, subOrAdd;
			var repList;
			
			text.string = "Ents: " + entManager.activeEntities.asString + 
									  "- Reps: " + repManager.activeReps.asString;
			repList = repManager.repList;
			repList.size.do { 
				arg index; 
				var spaceIn, currentObst, curRadPix, curWidth, obstacle, obstacPos; 
				obstacle = repList[index]; //get the current object
				//get position using camera if active
				if(cameraActive,
					{
					obstacPos = 
					if(obstacle.class == Camera2DRepresentation,
						{entManager.center;},
						{Camera2D.instance.applyTransformation(obstacle)+entManager.center});
					},
					{obstacPos = obstacle.position}
				);
				Pen.width = obstacle.penWidth;
				Pen.color = obstacle.color.alpha_(0.7);
				Pen.beginPath;
				//find the radius in meters and then in pixels
				currentObst = obstacle.radius;
				curRadPix = currentObst*meterInPixels;
				curWidth = curRadPix + curRadPix;
				//Pen.strokeOval(Rect((obstacle.position[0]*meterInPixels)-curRadPix, ((obstacle.position[1]*meterInPixels).linlin(0, 700, 700, 0))-curRadPix, curWidth, curWidth));
				obstacle.draw((Rect((obstacPos[0]*meterInPixels)-curRadPix, ((obstacPos[1]*meterInPixels).linlin(0, h, v, 0))-curRadPix, curWidth, curWidth)))
			};

			//Specific mainview setting and keyboard controls
			mainView.view.keyDownAction = 
				{arg view, char, modifiers, unicode, keycode;
					switch (keycode)
					{126}{cameraEntity.moveFwd(4)}
					{125}{cameraEntity.moveBack(4)}
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
				};

		};
	};
	}

}

