
GameLoop{
			 classvar <instance;
			 var <sceneWidth, <sceneHeight, <cellSize;
			 var <mainRoutine, <mainClock;
			 var <entManager, <repManager;
			 var <cameraEntity, <cameraActive = false, 
				 leftRotationRoutine, rightRotationRoutine;
			 //visualisation vars
			 var dimensions, gridSize, cellSize, mainView;
			 var rowSize, cellSizeInPixels;

	*new{ arg sceneWidth = 40, sceneHeight = 40, cellSize = 1;

			if(instance.isNil, 
				{
				^super.newCopyArgs(sceneWidth, sceneHeight, cellSize).init;
				},
				{"There is already an active instance of GameLoop".error;}
			);		
	}

	init{
		instance = this;
		mainClock = TempoClock.new;
		entManager = EntityManager(SpatialHashing(sceneWidth, sceneHeight, cellSize));
		//create the representation manager
		repManager = RepresentationManager.new;
		//add the representationManager as a dependant to the entManager
		//in order to notfy of new additions
		entManager.addDependant(repManager);
		CmdPeriod.add({this.clear});
		//TODO: Tidy up visualisation
		dimensions = [0, entManager.center[0]*2];
		gridSize = 10;
		cellSize = entManager.spatialIndex.cellSize;
		//define the rotation routine
		leftRotationRoutine = Routine{ //test
			loop{
			cameraEntity.rotateLeft(0.01pi);
			0.05.wait;
			};
		};

		rightRotationRoutine = Routine{ //test
			loop{
			cameraEntity.rotateRight(0.01pi);
			0.05.wait;
			};
		};
		//run the visualisation
		this.run;
	}

	world{ // at the moment we are returning the entManager
		   // but in the future we could return one of
		   // a collection of worlds
		^entManager
	}

	play{ arg rate; //start the gameloop at framerate rate
		if (mainRoutine.isNil,
			{ //1st condition
			  if(rate != nil, {entManager.dt = rate});
				mainRoutine = Routine{
					inf.do{
						entManager.refreshIndex1; //unregisterAll
						entManager.update; 
						entManager.refreshIndex2; //reregisterAll
						entManager.collisionCheck; 
						{this.render}.defer; //render!!
						entManager.dt.wait;
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

	render {
		mainView.refresh;
	}
	
	addCamera{  
		cameraEntity = Camera2D(
			entManager, 
			entManager.center, //position
			0.5, //radius 
			mass: 0.04,
			maxSpeed: 20 
		); 
		cameraEntity.activate;
		cameraActive = true;
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

	center{
		^RealVector2D[sceneWidth * 0.5, sceneHeight*0.5];
	}

	close {
		mainView.close;
	}

	dt{ //in case I need to get the dt of the world from here. 
		//If I implement mulitple worlds this is going away.
		^entManager.dt;
	}

	clear{
		this.removeCamera;
		entManager.clear;
		mainView.close;
		instance = nil;
	}

	clearEntities{
		entManager.clear;
	}
	run{/*{{{*/

		var   h = 400, v = 400, seed, run = true,  spaceUnits, spaceUnits2, meterInPixels,  speakerRadInPixels;
		mainView = Window("Visuals", Rect(0, 0, h, v), false);
		mainView.view.background = Color.black;
		mainView.onClose = { run = false }; // stop the thread on close
		mainView.front;
		//for  space of 700 pixels is 20 meters one meter is 35 pixels
		meterInPixels = h/(dimensions[1]-dimensions[0]); //assumes h = v
		cellSizeInPixels = meterInPixels*cellSize;
		rowSize = gridSize/cellSize;
		speakerRadInPixels = 2 * meterInPixels;
		mainView.drawFunc= {
		Pen.width = 2;
		
		//to draw the obstacles 
		//spaceUnits2 = repList.collect({arg item; item.position});
		
		Pen.use {   var divisions, subOrAdd;
					var repList;
			
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
						{entManager.center},
						{cameraEntity.applyTransformation(obstacle)+entManager.center});
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
			//mainView.alwaysOnTop = true;
			mainView.view.keyDownAction = 
				{arg view, char, modifiers, unicode, keycode;
					//view.postln;
					//char.postln;
					//modifiers.postln;
					//unicode.postln;
					//keycode.postln;
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
					//view.postln;
					//char.postln;
					//modifiers.postln;
					//unicode.postln;
					//keycode.postln;
					switch (keycode)
					{123}{leftRotationRoutine.stop}
					{124}{rightRotationRoutine.stop}
				};


/* COMMENTED OUT{{{*/
//				//draw the grid
//				
//				rowSize.do{ arg i;
//					Pen.beginPath;
//					Pen.strokeColor = Color.white;
//					Pen.width = 0.2;
//					Pen.line(Point(0, i*cellSizeInPixels), Point(700, i*cellSizeInPixels));
//					Pen.stroke;
//				};
//				
//				rowSize.do{ arg i;
//					Pen.beginPath;
//					Pen.strokeColor = Color.white;
//					Pen.width = 0.2;
//					Pen.line(Point(i*cellSizeInPixels, 0), Point(i*cellSizeInPixels, 700));
//					Pen.stroke;
//				};
		};
	};/*}}}*/
	}/*}}}*/

}
