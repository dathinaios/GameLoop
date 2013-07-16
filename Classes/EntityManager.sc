

EntityManager {
			 var <spatialIndex;
			 var <freeList, <mobList, <staticList;
			 var <>dt, <mainRoutine;
			 var <sceneWidth, <sceneHeight, <repManager;
			 var <currentCollisionList;

	*new { arg spatialIndex = SpatialHashing(20, 20, 0.5); 
	^super.newCopyArgs(spatialIndex).init
	} 
	
	init{
		dt = 0.05; //20 FPS
		freeList = List.new;
		mobList = List.new;
		staticList = List.new;
		currentCollisionList = List.new;
		//get the dimension of the space from the spatial index
		sceneWidth = spatialIndex.sceneWidth;
		sceneHeight = spatialIndex.sceneHeight;
	}

	stop{
		mainRoutine.stop;
	}

	activeEntities{
		^(freeList.size + mobList.size + staticList.size)
	}

	/* 
	prepare{ arg entity;
		this.changed([entity, \prepare]);
	} 
	*/

	/* EntityManager has three types of objects. Ones that dont collide,
	ones that collide with everything and ones that collide but not 
	between each other.*/
	add{ arg entity;
		switch (entity.collisionType)
		{\free} {freeList.add(entity)}
		{\mobile} {mobList.add(entity); spatialIndex.register(entity)}
		{\static} {staticList.add(entity); spatialIndex.register(entity)};
		//notify dependants that an entity was added passing the entity
		//this.changed([entity, \add])
	}

	remove{ arg entity; 
		switch (entity.collisionType)
		{\free} {freeList.remove(entity)}
		{\mobile} {mobList.remove(entity); spatialIndex.unregister(entity)}
		{\static} {staticList.remove(entity); spatialIndex.unregister(entity)};
		//this.changed([entity, \remove]);
	}
		
	update{
			freeList.do{arg i; i.update};
			mobList.do{arg i; i.update};
			staticList.do{arg i; i.update};
	}
	
	clear { var listCopy;
 		[freeList.copy, mobList.copy, staticList.copy].flat.do{arg i; i.remove};
	}

	/* refresh can not happen simply by clearing all buckets because
	we need to keep the registered static elements */

	refreshIndex1 {
				mobList.do{arg i;
					spatialIndex.unregister(i);
				};				 
	}
	
	refreshIndex2 { 
				mobList.do{arg i;
					spatialIndex.register(i);
				};				 
	}

	collisionCheck{ 
	
		mobList.do{ arg i; var nearest, collidingWith;
			// a list to store the objects that are found to collide with the entity
			collidingWith = List.new; 
			nearest = spatialIndex.getNearest(i);
			if(nearest.size>0, {
					nearest.do{arg i2; 
						if(this.circlesCollide(i, i2)) {collidingWith = collidingWith.add(i2)};
					};
					if(collidingWith.size != 0,
						{currentCollisionList.add([i, collidingWith])}, //form is [entity, [ListofCollidingWithEntities]]
						{i.colliding_(false)}
					);
					},
					{
					i.colliding_(false)
					}
			);
		};
		
		//and now for the static entities
		staticList.do{ arg i; var nearest, collidingWith;
			if(i.colliding)
				{
				collidingWith = List.new;
				nearest = spatialIndex.getNearest(i);
				//remove the static ones since we dont want to check the collisions between them
				nearest = nearest.asArray.takeThese({arg i; staticList.includes(i)});
				//if there are objects left (mobile entities) check for collisions
				if(nearest.size>0, {
						nearest.do{arg i2;
							if(this.circlesCollide(i, i2)) {collidingWith = collidingWith.add(i2)};
						};
						if(collidingWith.size != 0,
							{currentCollisionList.add([i, collidingWith])}, //form = [entity, [ListofCollidingWithEntities]]
							{i.colliding_(false)}
						);
						},
						{
						i.colliding_(false)
						}
				);
				};
		};
		
	}
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

	collisionResolution{
		//At the moment I am simply calling the collide function for every object
		//but here we could have much more elaborate collision resolutions.
		currentCollisionList.do{arg i;
			//Calling: entityColliding.collision(collidingWith)
			i[0].collision(i[1]);
		};
		//and clear the list to prepare for next time
		currentCollisionList.clear;
	}
	
	center{
		^RealVector2D[sceneWidth * 0.5, sceneHeight*0.5];
	}
}

