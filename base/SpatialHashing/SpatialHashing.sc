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
		   var set, pos, rad, minPoint, maxPoint;
		set = IdentitySet.new;
		pos = object.position;
		rad = object.radius;
		//find the points for the corners of the bounding box
		//in this case the object is a point and has a radius
		//(is that the best way?)
		minPoint = [pos[0] - rad, pos[1] - rad];
		maxPoint = [pos[0] + rad, pos[1] + rad];
		//do addBacket for each corner of the rectangle
		set.add(this.addBacket(minPoint));
		set.add(this.addBacket([maxPoint[0], minPoint[1]]));
		set.add(this.addBacket(maxPoint));
		set.add(this.addBacket([minPoint[0], maxPoint[1]]));
		^set;
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

}                                                                                                                                                                                                                               
