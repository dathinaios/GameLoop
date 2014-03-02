SVUnit{ var gameloop, input, position, radius, mass, velocity, collisionType, maxSpeed;
        var soundRep, visualRep;

  *new{ arg gameloop, input, position, radius, mass, velocity, collisionType, maxSpeed;
    ^super.newCopyArgs(gameloop, input, position, radius, mass, velocity, collisionType, maxSpeed).init;
  }

  init{
       //implementation
  }
}
