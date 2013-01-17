RepresentationManager{ var manager; 
					   var <repList, <cameraEntity;
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
			maxSpeed: 10, 
		); 
		cameraEntity.activate;
	}

	removeCamera{
		manager.remove(cameraEntity);
		cameraEntity.changed(\remove);
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
				obstacPos = obstacle.position;
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

