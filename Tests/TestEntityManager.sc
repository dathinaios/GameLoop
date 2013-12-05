TestEntityManager : UnitTest{ var entManager;

  setUp {
    entManager = EntityManager(SpatialHashing(20,20.1));
  }

  tearDown {
    // this will be called after each test
  }

  test_add_remove{ var entity;

    entity = Entity(world: entManager, position: RealVector2D[20, 20], radius: 0.5, mass: 0.3);
    entity.add;
    this.assert(entManager.freeList.size == 1, "world free list should increase to 1");
    entity.remove;
    this.assert(entManager.freeList.size == 0, "world freeList should decrease to 0");

    entity = Entity(world: entManager, position: RealVector2D[20, 20], radius: 0.5, mass: 0.3);
    entity.collisionType_(\mobile);
    entity.add;
    this.assert(entManager.mobList.size == 1, "world moblist should increase to 1");
    entity.remove;
    this.assert(entManager.mobList.size == 0, "world moblist should decrease to 0");

    entity = Entity(world: entManager, position: RealVector2D[20, 20], radius: 0.5, mass: 0.3);
    entity.collisionType_(\static);
    entity.add;
    this.assert(entManager.staticList.size == 1, "world static list should increase to 1");
    entity.remove;
    this.assert(entManager.staticList.size == 0, "world static list should decrease to 0");

  }

  test_removeStaticEntitiesFromSet{ var set, entity;
    entity = Entity(entManager, collisionType: \static);
    set = Set[entity];
    set = entManager.removeStaticEntitiesFromSet(set);
    this.assert(set.size == 0, "should remove static entities from set");
  }

  test_doForPotentialCollisions{

  }

  test_checkForCollisionsWithObjects{

  }

  test_collectCollidingObjects{

  }

}
