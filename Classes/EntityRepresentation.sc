EntityRepresentation { var repManager, <>collisionFunc;
  var <position, <radius, <speed, entity, attached = false;
  var <>type = 'sound';

  *new { arg repManager, collisionFunc;
    ^super.newCopyArgs(repManager, collisionFunc)
  }

  init{ var latency;
    /* initialize data */
    this.getData;
    collisionFunc = collisionFunc ?? {{}};
  }

  /* dependant notification from entity */
  update { arg entity, message;
    switch (message[0])
    {\preUpdate}
    { var transPosition;
      transPosition = this.cameraTransform(entity);
      this.preUpdate(entity, transPosition);
    }
    {\update}
    {this.getData}
    {\attach}
    {this.attach(entity) }
    {\remove}
    {this.remove;}
    {\detach}
    {this.detach(message)}
    {\collision}
    {this.collision(message)};
  }

  attach{ arg entity;
    if (attached.isNil or:{attached.not},
    { attached = true;
      this.storeEntity(entity);
      this.init; }
    );
  }

  detach{ arg message;
      if (attached and:{message[1] == this}, {attached = false; this.remove;});
  }

  cameraTransform{arg entity;
    if (Camera2D.active,
      {^(Camera2D.instance.applyTransformation(entity))},
      {^position}
    );
  }

  preUpdate{ arg entity, transposition;
    position = transposition + entity.worldCenter;
  }

  getData{
    position = entity.position - entity.worldCenter;
    radius = entity.radius;
    speed = entity.velocity.norm;
  }

  addEntity{
    if (entity.active.not){entity.add};
  }

  storeEntity{ arg item;
    entity = item;
  }

  colliding{
    ^entity.colliding;
  }

  dt{
    ^entity.dt;
  }

  add{
    this.subclassResponsibility(thisMethod);
  }

  remove{
    repManager.remove(this);
    attached = false;
  }

  collision{ arg message;
    /* message should have a list at [2] with the colliding with entities*/
    collisionFunc.value(this, entity, message[1]);
  }

}
