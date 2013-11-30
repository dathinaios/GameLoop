TestEntity : UnitTest{ var entity, entManager;

  setUp {
    entManager = EntityManager(SpatialHashing(20,20,0.1));
    entity = Entity(world: entManager, position: RealVector2D[20, 20], radius: 0.5, mass: 0.3);
  }

  tearDown {
    // this will be called after each test
  }

  test_attach{
    entity.attach("dep1");
    this.assert(entity.dependants.size == 1, "after adding dependant the size is 1");
  }

  test_detach{ var attachment;
    attachment = "dep1";
    entity.attach(attachment);
    entity.detach(attachment);
    this.assert(entity.dependants.size == 0, "detaching dependants has the xpected results");
  }

  test_detachAll{
    entity.attach("dep1");
    entity.attach("dep2");
    entity.attach("dep3");
    entity.detachAll;
    this.assert(entity.dependants.size == 0, "dependants have been cleared correctly")
  }

  test_add_remove{
    entity.add;
    this.assert(entity.active == true, "addition should set active to true");
    this.assert(entManager.freeList.size == 1, "world free list should increase to 1");
    entity.remove;
    this.assert(entity.active == false, "removal should set active to false");
    this.assert(entManager.freeList.size == 0, "world freeList should decrease to 0");
  }

}
