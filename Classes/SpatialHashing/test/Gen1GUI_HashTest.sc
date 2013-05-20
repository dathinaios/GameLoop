Gen1GUI_SH { var dimensions, <mainsArray, <obstacleArray, gridSize, cellSize, speakerRad, playRout, w;


    *new {arg dimensions, mainsArray, obstaclesArray, gridSize = 10, cellSize = 0.5, speakerRad = 1.5;
    		^super.newCopyArgs(dimensions, mainsArray, obstaclesArray, gridSize, cellSize, speakerRad).init;
     }
     
     init{}
	
     activate {

		var   h = 700, v = 700, seed, run = true,  spaceUnits, spaceUnits2, meterInPixels, speakerRadInPixels;
		w = Window("Gen1", Rect(0, 0, h, v), false);
		w.view.background = Color.black;
		w.onClose = { run = false }; // stop the thread on close
		w.front;
		//for  space of 700 pixels is 20 meters one meter is 35 pixels
		meterInPixels = h/(dimensions[1]-dimensions[0]); //assumes h = v
		//speakerRadInPixels = speakerRad * meterInPixels;
		
		w.drawHook = {
			Pen.width = 2;
			
			//to draw the obstacles 
			spaceUnits2 = obstacleArray.collect({arg item; item.position});
			
			//to draw the units the space is changing
			
			spaceUnits = mainsArray.collect({arg item; item.position});
			
			Pen.use { var divisions, subOrAdd;
				
				//Pen.color = Color.white;
				//divisions = speakerRadInPixels/2;
				//subOrAdd = 350-divisions;
				//Pen.strokeOval(Rect(subOrAdd, subOrAdd, speakerRadInPixels, speakerRadInPixels)); //1.5*35 =52.5 is the radius of the speaker array
				
				//Pen.fillOval(Rect(350-3, 350-3, 6, 6));
				
				obstacleArray.size.do { arg index; var spaceIn, currentObst, curRadPix, curWidth, obstacle; 
					obstacle = obstacleArray[index]; //get the current object
					spaceIn = [spaceUnits2[index][0].linlin(dimensions[0], dimensions[1], 0, 700), 
						   spaceUnits2[index][1].linlin(dimensions[0], dimensions[1], 700, 0)];
					Pen.width = obstacle.penWidth;
					Pen.color = obstacle.color.alpha_(0.7);
					Pen.beginPath;
					//find the radius in meters and then in pixels
					currentObst = obstacle.radius;
					curRadPix = currentObst*meterInPixels;
					curWidth = currentObst*2*meterInPixels;
					Pen.strokeOval(Rect(spaceIn[0]-curRadPix, spaceIn[1]-curRadPix, curWidth, curWidth));
		
				};
				
				//draw the agents
				Pen.width = 2;
				mainsArray.size.do { arg index; var spaceIn; 
					spaceIn = [spaceUnits[index][0].linlin(dimensions[0], dimensions[1], 0, 700), 
						   spaceUnits[index][1].linlin(dimensions[0], dimensions[1], 700, 0)];
					Pen.color = Color.white;
					Pen.beginPath;
					Pen.fillOval(Rect(spaceIn[0]-1.25, spaceIn[1]-1.25, 3.5, 3.5));
		
				};
				
				//draw the grid
				
				(gridSize/cellSize).do{ arg i;
					Pen.beginPath;
					Pen.strokeColor = Color.white;
					Pen.width = 0.2;
					Pen.line(Point(0, i*(meterInPixels*cellSize)), Point(700, i*(meterInPixels*cellSize)));
					Pen.stroke;
				};
				
				(gridSize/cellSize).do{ arg i;
					Pen.beginPath;
					Pen.strokeColor = Color.white;
					Pen.width = 0.2;
					Pen.line(Point(i*(meterInPixels*cellSize), 0), Point(i*(meterInPixels*cellSize), 700));
					Pen.stroke;
				};
			};
		};
		
		// fork a thread to update 20 times a second, and advance the phase each time
		playRout = { while { run } { w.refresh; 0.05.wait;} }.asRoutine.play(AppClock)
		//
		//{ while { run } { w.refresh; 0.05.wait; space = [~genMain.space, ~genMain2.space, ~genMain3.space];} }.fork(AppClock)


	}
	
	close {
		playRout.stop;
		w.close;
	}
	
	pause {
		playRout.stop;
	
	}
	
	update { arg newMains, newObstacle;
	
		mainsArray = newMains;
		obstacleArray = newObstacle;
		playRout.reset.play(AppClock);
	
	}


}                                                        