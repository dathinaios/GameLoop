TestEntityRepresentation : UnitTest{var entity, rep, world, repManager;

  setUp {
    world = ();
    world.center = RealVector2D[20, 20];
    repManager = [];
    rep = EntityRepresentation(repManager);
    entity = MobileEntity(world);
  }

  tearDown {
    // this will be called after each test
  }

  test_attach{
    rep.attach(entity);
    this.assert(rep.attached, "it returns true for attached message");
    this.assert(rep.entity == entity, "it stores the entity in an instance variable");
    this.assert(entity.radius == rep.radius, "is stores the entity radius in representation");
    this.assert(entity.velocity.norm == rep.speed, "it store the normal ofentity velocity as speed");
    this.assert(
      entity.position == (rep.position + entity.worldCenter),
      "it stores the entity position to the rep position subtracting the world center (for camera transform)"
    );
  }

  test_detach{
    rep.attach(entity);
    rep.detach([nil, rep]);
    this.assert(rep.attached.not, "it returns false after detach message");
  }

  test_addAll{
  }

}
