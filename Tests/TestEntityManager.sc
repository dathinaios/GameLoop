TestEntityManager : UnitTest{ var entManager;

  setUp {
    entManager = EntityManager(SpatialHashing(20,20.1));
  }

  tearDown {
    // this will be called after each test
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
