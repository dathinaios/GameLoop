/*
TODO: a method that finds entities at a given distance
*/

SpatialHashing{ var <sceneWidth, <sceneHeight, <cellSize;
	var <buckets, cols, rows;

	*new { arg sceneWidth = 10, sceneHeight = 10, cellSize = 0.5;  
		^super.newCopyArgs(sceneWidth, sceneHeight, cellSize).init
	}

	init { var numCells;

		cols= sceneWidth / cellSize;
		rows= sceneHeight / cellSize;
		numCells = cols * rows;
		buckets = List.newClear(numCells);

		for (0,numCells-1,
			{ arg i; var list;
				//("pol"++i).postln;
				buckets.put(i, List.new);
			}
		)
	}

	//**\\

	register{arg object; //registers the object in the relevant cells
		var set; //use a set to discard duplicates
		set = this.findCells(object); //returns a set of cell id's
		set.do{arg item; buckets[item].add(object)};
	}

	unregister{ arg object;
		var bucketIDs;
		bucketIDs = this.findCells(object);
		bucketIDs.do{arg i; 
			buckets[i].remove(object)
		};

	}

	//**\\

	findCells{ arg object; //calculates the cell for each objects four corners
		var set, pos, rad, cellIDs;
		set = IdentitySet.new;
		pos = object.position;
		rad = object.radius;
		cellIDs = this.findCellIDs(pos,rad);
		//do addBacket for each corner of the rectangle
		set.add(this.addBacket(cellIDs[0]));
		set.add(this.addBacket(cellIDs[1]));
		set.add(this.addBacket(cellIDs[2]));
		set.add(this.addBacket(cellIDs[3]));
		^set;
	}

	findCellIDs{ 
		arg position, radius;
		var minPoint, maxPoint;
		//find the points for the corners of the bounding box
		//in this case the object is a point and has a radius
		//(is that the best way?)
		minPoint = [position[0] - radius, position[1] - radius];
		maxPoint = [position[0] + radius, position[1] + radius];
		/* [id1,id2,id3,id4] */
		^[ minPoint, [maxPoint[0], minPoint[1]], maxPoint, [minPoint[0], maxPoint[1]] ]
	}

	addBacket{ arg pos; var backet;	//find the backet to add to
		//can  be optimised to remove the divisions! See hash optimisation paper
		^(
			(pos[0]/cellSize).floor +
			((pos[1]/cellSize).floor*cols)
		).asInteger;
	}

	getNearest{ arg object;
		var set = IdentitySet.new, bucketIDs;
		bucketIDs = this.findCells(object);
		bucketIDs.do{arg i; 
			buckets[i].do{
				arg i; set.add(i)
			}
		};
		//return the set minus the main object
		^set.remove(object);
	}

	getCell{arg index;
		^buckets[index];
	}

	clearBuckets{
		buckets.do{arg i; i.clear}
	}

	/* my surely inefficient algorithm for assigning cells to a line. */ 
	/* It involves creating and projecting spherical objects along its length. */
	/* Hopefully not too costly since it is going to be called only once. */

	getCellsForLine{ arg line;
		var halfCellSize, objectDistance, unitsInLine;

		halfCellSize = cellSize * 0.5;
		objectDistance = line.to - line.from;

		unitsInLine = objectDistance.norm/halfCellSize;

		/* gather helper objects every half a cell along the line */
		unitsInLine.do{ 
			arg i; 
			var currentPlace, xStep, yStep, x, y, pointToObject;
			var destinationX, destinationY;

			destinationX = line.to[0];
			destinationY = line.to[1];

			currentPlace = halfCellSize*i;
			xStep = objectDistance[0]/unitsInLine;
			yStep =  objectDistance[1]/unitsInLine;

			x = destinationX - (  (destinationX - line.from[0] ) - ( xStep * i ) );
			y = destinationY - (  (destinationY - line.from[1] ) - ( yStep * i ) );
			PointToObjectHelper(RealVector2D[x, y], CellSize).position.postln;
		};

	}

 }                                                                                                                                                                                                                               

 PointToObjectHelper{ var <>position, <>radius;

 	*new{ arg position = RealVector2D[0, 0], radius = 0.5;
 	  ^super.newCopyArgs(position, radius);
 	}

 }
