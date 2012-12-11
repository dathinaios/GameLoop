RenderVisuals { var dimensions, <obstacleArray, gridSize, cellSize, w;
					var rowSize, cellSizeInPixels;

    *new {arg dimensions, obstaclesArray, gridSize = 10, cellSize = 0.5;
    		^super.newCopyArgs(dimensions, obstaclesArray, gridSize, cellSize).init;
     }
     
     init{
     	
     }
	
     activate {

		var   h = 400, v = 400, seed, run = true,  spaceUnits, spaceUnits2, meterInPixels,  speakerRadInPixels;
		w = Window("Visuals", Rect(0, 0, h, v), false);
		w.view.background = Color.black;
		w.onClose = { run = false }; // stop the thread on close
		w.front;
		//for  space of 700 pixels is 20 meters one meter is 35 pixels
		meterInPixels = h/(dimensions[1]-dimensions[0]); //assumes h = v
		cellSizeInPixels = meterInPixels*cellSize;
		rowSize = gridSize/cellSize;
		speakerRadInPixels = 2 * meterInPixels;
		w.drawFunc= {
		
			Pen.width = 2;
			
			//to draw the obstacles 
			//spaceUnits2 = obstacleArray.collect({arg item; item.position});
			
			Pen.use { var divisions, subOrAdd;
				
				obstacleArray.size.do { arg index; var spaceIn, currentObst, curRadPix, curWidth, obstacle, obstacPos; 
					obstacle = obstacleArray[index]; //get the current object
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
//				
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
		};
		
		// fork a thread to update 20 times a second, and advance the phase each time
		//playRout = { while { run } { w.refresh; 0.05.wait;} }.asRoutine.play(AppClock)
		//
		//{ while { run } { w.refresh; 0.05.wait; space = [~genMain.space, ~genMain2.space, ~genMain3.space];} }.fork(AppClock)


	}
	
	render { w.refresh;
	}
	
	close {
		w.close;
	}
	
	update { arg newMains, newObstacle;
		obstacleArray = newObstacle;
	
	}


}                                                        
