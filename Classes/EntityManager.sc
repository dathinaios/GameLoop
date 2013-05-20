
// EntityManager has three types of objects. Ones that dont collide,
// ones that collide with everything and ones that collide but not between each other.

EntityManager {
			 var <spatialIndex; //render hook is a function that will be called once every loop
			 var <freeList, <mobList, <staticList;
			 var <>dt, <mainRoutine, <mainClock;
			 var <sceneWidth, <sceneHeight, <repManager;

	*new { arg spatialIndex = SpatialHashing(20, 20, 0.5); 
	^super.newCopyArgs(spatialIndex).init
	} 
	
	init{
		dt = 0.05; //20 FPS
		freeList = List.new;
		mobList = List.new;
		staticList = List.new;
		//mainClock = TempoClock.new;
		//get the dimension of the space from the spatial index
		sceneWidth = spatialIndex.sceneWidth;
		sceneHeight = spatialIndex.sceneHeight;
	}

	stop{
		mainRoutine.stop;
		//mainRoutine.reset;
	}

	add{ arg entity;
		switch (entity.collisionType)
		{\free} {freeList.add(entity)}
		{\mobile} {mobList.add(entity); spatialIndex.register(entity)}
		{\static} {staticList.add(entity); spatialIndex.register(entity)};
		//notify dependants (represemtationManager) that an entity was added
		this.changed([entity, \add])
		//repManager.newEntity(entity);
	}

	remove{ arg entity; 
		switch (entity.collisionType)
		{\free} {freeList.remove(entity)}
		{\mobile} {mobList.remove(entity); spatialIndex.unregister(entity)}
		{\static} {staticList.remove(entity); spatialIndex.unregister(entity)};
		this.changed([entity, \remove]);
	}
		
	update{ //update all Entities
			freeList.do{arg i; i.update};
			mobList.do{arg i; i.update};
			staticList.do{arg i; i.update};
	}
	
	clear { var listCopy;
 		[freeList.copy, mobList.copy, staticList.copy].flat.do{arg i; i.remove};
	}
	
	refreshIndex1 { //refresh can not happen simply by clearing buckets as in manager1 because we need to keep
				 //the registered static elements
				 
				mobList.do{arg i;  //unregister for mobile objects
					spatialIndex.unregister(i);
				};				 

	}
	
	refreshIndex2 { //refresh can not happen simply by clearing buckets as in manager1 because we need to keep
				 //the registered static elements
				 
				mobList.do{arg i;  //reregister for mobile objects
					spatialIndex.register(i);
				};				 

	}

	//Collision detection/*{{{*/

//	collisionCheck{ 
//	
//		mobList.do{ arg i; var nearest;
//			//get the near by objects
//			nearest = spatialIndex.getNearest(i);
//			//if there were objects found check for collisions
//			if(nearest.size>0, {
//					nearest.do{arg i2; 
//						if(this.circlesCollide(i, i2)) {i.collision(i2)};
//					};
//					}
//			);
//		};
//		
//	}/*}}}*/


	collisionCheck{ 
	
		mobList.do{ arg i; var nearest, collidingWith;
			// a list to store the objects that are found to collide with the entity
			collidingWith = List.new; //I use a list to allow for any number of objects
			//get the near by objects
			nearest = spatialIndex.getNearest(i);
			//if there were objects found check for collisions
			if(nearest.size>0, {
					nearest.do{arg i2; //gather all the colliding with entity objects in the list
						if(this.circlesCollide(i, i2)) {collidingWith = collidingWith.add(i2)};
					};
					if(collidingWith.size != 0,//if there are results
						// call the collision function passing the list as an argument
						{i.collision(collidingWith)},
						//else set the colliding to false
						{i.colliding_(false)}
					);
					},
					//if not set the colliding variable to false
					{
					i.colliding_(false)
					}
			);
		};
		
		//and now for the static entities
		/*
			TODO line 243 the take this method may not be very efficient
		*/
		staticList.do{ arg i; var nearest, collidingWith;
			//dont do anything unless it was found to collide
			if(i.colliding)//if it was found to collide find with which and send the collision msg.
				{
				// a list to store the objects that are found to collide with the entity
				collidingWith = List.new; //I use a list to allow for any number of objects
				//get the near by objects
				nearest = spatialIndex.getNearest(i);
				//remove the static ones since we dont want to check the collisions between them
				nearest = nearest.asArray.takeThese({arg i; staticList.includes(i)});
				//if there were objects found check for collisions
				if(nearest.size>0, {
						nearest.do{arg i2; //gather all the colliding with entity objects in the list
							if(this.circlesCollide(i, i2)) {collidingWith = collidingWith.add(i2)};
						};
						if(collidingWith.size != 0,//if there are results
							// call the collision function passing the list as an argument
							{i.collision(collidingWith)},
							//else set the colliding to false
							{i.colliding_(false)}
						);
						},
						//if not set the colliding variable to false
						{
						i.colliding_(false)
						}
				);
				};
		};
		
	}

	//faster:
	circlesCollide{ arg cA, cB; //circleA circleB
				  var r1, r2, dx, dy;
				  var a;
			r1 = cA.radius;
			r2 = cB.radius;
			a = (r1+r2) * (r1+r2);
			dx = cA.position[0] - cB.position[0];//distance of x points
			dy = cA.position[1] - cB.position[1];//distance of y points

	        if (a > ((dx*dx) + (dy*dy)),
	        {^true}
	        ,{^false});			

	}
	
	center{
		^RealVector2D[sceneWidth * 0.5, sceneHeight*0.5];
	}

}


//I could fix this class in case we want to have collision detection for every object. In that cas it might be faster to
// do it through EntityManager2

EntityManager2{ /*{{{*/
			 var renderHook, <spatialIndex;
			 var <entityList;
			 var <dt, <mainRoutine, <mainClock;
			 var <sceneWidth, <sceneHeight;

	*new { arg index = SpatialHashing(20, 20, 0.5); 
	^super.newCopyArgs(index).init
	} 
	
	init{
		dt = 0.05; //20 FPS
		entityList = List.new;
		mainClock = TempoClock.new;
		sceneWidth = spatialIndex.sceneWidth;
		sceneHeight = spatialIndex.sceneHeight;
	}

	play{ arg rate; //start the gameloop at framerate rate
		if (mainRoutine.isNil.postln,
			{ //1st condition
			  dt = rate ?? dt;
				mainRoutine = Routine{
					inf.do{
						this.update; 
						this.refreshIndex; //unregisterAll
						this.collisionCheck; 
						renderHook.value;
						dt.wait;
						}
				}.play(mainClock)
			}, 
			{ //2nd condition
			  mainRoutine.play;
			}
		);
	}

	stop{
		mainRoutine.stop.reset;
	}


	add{ arg entity; 
		entityList.add(entity);
		spatialIndex.register(entity);
	}

	remove{ arg entity; 
		entityList.remove(entity);
		spatialIndex.unregister(entity);
	}
	
	update{ entityList.do{arg i; i.update;}
	}
	
	clear { var listCopy;
		    listCopy = entityList.copy;
		    listCopy.do{arg i; i.remove};
	}
	
	refreshIndex { 
				spatialIndex.clearBuckets; //clear the index
				entityList.do{arg i;  //recalculate for all objects
					spatialIndex.register(i);
				};
	}
	
	//Collision detection
	
//	collisionCheck{ 
//		entityList.do{ arg i; var nearest;
//			//get the near by objects
//			nearest = spatialIndex.getNearest(i);
//			//if there were objects found check for collisions
//			if(nearest.size>0, {
//					nearest.do{arg i2; 
//						if(this.circlesCollide(i, i2)) {i.collision(i2)};
//					};
//					}
//			);
//		}
//	}

	collisionCheck{ 
		entityList.do{ arg i; var nearest, collidingWith;
			// a list to store the objects that are found to collide with the entity
			collidingWith = List.new; //I use a list to allow for any number of objects
			//get the near by objects
			nearest = spatialIndex.getNearest(i);
			//if there were objects found check for collisions
			if(nearest.size>0, {
					nearest.do{arg i2; //gather all the colliding with entity objects in the list
						if(this.circlesCollide(i, i2)) {collidingWith = collidingWith.add(i2)};
					};
					if(collidingWith.size != 0,//if there are results
						// call the collision function passing the list as an argument
						{i.collision(collidingWith)},
						//else set the colliding to false
						{i.colliding_(false)}
					);
					},
					//if not set the colliding variable to false
					{
					i.colliding_(false)
					}
			);
		}
	}

	//faster:
	circlesCollide{ arg cA, cB; //circleA circleB
				  var r1, r2, dx, dy;
				  var a;
			r1 = cA.radius;
			r2 = cB.radius;
			a = (r1+r2) * (r1+r2);
			dx = cA.position[0] - cB.position[0];//distance of x points
			dy = cA.position[1] - cB.position[1];//distance of y points

	        if (a > ((dx*dx) + (dy*dy)),
	        {^true}
	        ,{^false});			

	}
	
	center{ var width, height;
		width = spatialIndex.sceneWidth;
		height = spatialIndex.sceneHeight;
		^RealVector2D[width * 0.5, height*0.5];
	}

}/*}}}*/
