/*
	TODO Can I resolve the specialisation of EntityManager2 so that at least it can be along with any other class that differs from EntityManager a subclass of it?
*/

EntityManager{ 
			 var <spatialIndex;
			 var <entityList;
			 var <dt;

	*new { arg index = SpatialHashing(20, 20, 0.5); 
	^super.newCopyArgs(index).init
	} 
	
	init{
		dt = 0.05; //20 FPS
		entityList = List.new;
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
		^RealVector[width * 0.5, height*0.5];
	}

}

// Entity Manager 2 has three types of objects. Ones that dont collide,
// ones that collide with everything and ones that collide but not between each other.

EntityManager2 {
			 var <spatialIndex;
			 var <freeList, <mobList, <staticList;
			 var <dt;

	*new { arg index = SpatialHashing(20, 20, 0.5); 
	^super.newCopyArgs(index).init
	} 
	
	init{
		dt = 0.05; //20 FPS
		freeList = List.new;
		mobList = List.new;
		staticList = List.new;
	}

	addFree{ arg entity; 
		freeList.add(entity);
	}

	removeFree{ arg entity; 
		freeList.remove(entity);
	}
	
	addMob{ arg entity; 
		mobList.add(entity);
		spatialIndex.register(entity);
	}

	removeMob{ arg entity; 
		mobList.remove(entity);
		spatialIndex.unregister(entity);
	}
	
	addStat{ arg entity; 
		staticList.add(entity);
		spatialIndex.register(entity);
	}

	removeStat{ arg entity; 
		staticList.remove(entity);
		spatialIndex.unregister(entity);
	}
		
	update{ //update all Entities
			freeList.do{arg i; i.update};
			mobList.do{arg i; i.update};
			staticList.do{arg i; i.update};
	}
	
	clear { var listCopy;
//		    listCopy = freeList.copy;
//		    listCopy.do{arg i; i.remove};
//		    
//		    listCopy = mobList.copy;
//		    listCopy.do{arg i; i.remove};
//		    
//		    listCopy = staticList.copy;
//		    listCopy.do{arg i; i.remove};

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

	//Collision detection

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
//	}


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
	
	center{ var width, height;
		width = spatialIndex.sceneWidth;
		height = spatialIndex.sceneHeight;
		^RealVector[width * 0.5, height*0.5];
	}

}

//basic use:

//(
//(
//
////create he manager and pass it the spatial index instance.
//~entityManager = EntityManager(SpatialHashing(20, 20, 0.5));
//g = Gen1GUI_GameLoopTest([0, 20], RepresentationManager.repList, 20, 0.5);
//g.activate;
//
///// START ///
//{
//inf.do{
//
//	~entityManager.update; 
//	~entityManager.refreshIndex;
//	~entityManager.collisionCheck;
//	{g.render}.defer; //and update the representation/view
//	0.05.wait;
//
//}
//}.asRoutine.play
//)
//)

//finding the object witht the smallest ID for collsion

//	collision { arg ents; var smallestID;
//	
//		smallestID = ents.add(this).minItem({ arg item, i; item.id });
//		if(smallestID == this)
//		{
//		ents.do{arg i; i.remove};
//		
//		"BANG BANG!!!!".postln
//
//		}
//		
//	
//	}
