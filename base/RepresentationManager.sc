RepresentationManager{ var manager; 
					   var <repList, <cameraEntity, <cameraActive = false, 
					   		leftRotationRoutine, rightRotationRoutine;
					   //visualisation vars TODO: tidy up and fix
					   var dimensions, gridSize, cellSize, mainView;
					   var rowSize, cellSizeInPixels;

	*new { arg manager;
	^super.newCopyArgs(manager).init
	} 

	init{
		repList = List.new;
		CmdPeriod.add({this.clear});
		//TODO: Tidy up visualisation
		dimensions = [0, manager.center[0]*2];
		gridSize = 10;
		cellSize = manager.spatialIndex.cellSize;
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
	}

	//the method called by entityManager to notify of the creation of new entities
	newEntity{ arg entity; var representation;
		//create and attach the relevant rerpesentation if it exists
		representation = (entity.class.asString++"Representation").asSymbol.asClass.new(entity);
		representation ?? entity.attach(representation);
	}
	
	addCamera{  
		cameraEntity = Camera2D(
			manager, 
			manager.center, //position
			0.5, //radius 
			mass: 0.04,
			maxSpeed: 20 
		); 
		cameraEntity.activate;
		cameraActive = true;
	}

	removeCamera{
		manager.remove(cameraEntity);
		cameraEntity.changed(\remove);
		cameraActive = false;
	}

	add{arg entity; 
		repList.add(entity);
	}

	remove{ arg entity; 
		repList.remove(entity);
	}
	
	clear{repList.clear;
	}

	render {
		mainView.refresh;
	}
	
	close {
		mainView.close;
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
		
		Pen.use { var divisions, subOrAdd;
			
			repList.size.do { 
				arg index; 
				var spaceIn, currentObst, curRadPix, curWidth, obstacle, obstacPos; 
				obstacle = repList[index]; //get the current object
				//get position using camera if active
				if(cameraActive,
					{
					obstacPos = 
					if(obstacle.class == Camera2DRepresentation,
						{manager.center},
						{cameraEntity.applyTransformation(obstacle)+manager.center});
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
	}
}       

///////////////

