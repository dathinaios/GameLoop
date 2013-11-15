
/* For implementation details refer to the book Game AI by example by Mat Buckland */

Seek {
  *new{ arg entity, targetPos = RealVector2D[10,13];
       var desiredVelocity;

    desiredVelocity = targetPos - entity.position;
    desiredVelocity = desiredVelocity.normalize;
    desiredVelocity = desiredVelocity * entity.maxSpeed;
    ^(desiredVelocity - entity.velocity);
  }
}

Arrive {
  //Deceleration{slow = 3, normal = 2, fast = 1};
  *new{ arg entity, targetPos = RealVector2D[10,13], deceleration = 2, tweak = 0.3;
           var desiredVelocity, toTarget, speed, dist;

      toTarget = targetPos - entity.position;
      dist = toTarget.norm;

      if ( dist > 0,
        {
        speed = dist / (deceleration * tweak);
        speed = speed.min(entity.maxSpeed);
        desiredVelocity = (toTarget*speed)/dist;
        ^(desiredVelocity - entity.velocity);
        },
        {
        ^RealVector2D[0,0];
        }
      );


  }
}

Wander {
  *new{
    arg entity, wanderRadius = 1.2, wanderDistance = 2.0, wanderJitter = 80;
    var wanderTarget = RealVector2D[0, 0], targetLocal, targetWorld;
    var theta;

    //from java : this behavior is dependent on the update rate, so this line must
    //be included when using time independent framerate.
    //double JitterThisTimeSlice = m_dWanderJitter * m_pVehicle.getTimeElapsed();

    //stuff for the wander behavior
    theta = 1.0.rand *2pi;
    //create a vector to a target position on the wander circle
    /*java code --> wanderTarget = new Vector2D(wanderRadius * Math.cos(theta), wanderRadius * Math.sin(theta)); */
    wanderTarget = RealVector2D[ wanderRadius * theta.cos, wanderRadius * theta.sin];

    //first, add a small random vector to the target's position
    /*java:  m_vWanderTarget.add(new Vector2D(RandomClamped() * JitterThisTimeSlice, RandomClamped() * JitterThisTimeSlice)); */
    wanderTarget = wanderTarget + RealVector2D[rrand(-1.0, 1.0) * wanderJitter, rrand(-1.0, 1.0) * wanderJitter];

    //reproject this new vector back on to a unit circle
    wanderTarget = wanderTarget.normalize;

    //increase the length of the vector to the same as the radius
    //of the wander circle
    wanderTarget = wanderTarget * wanderRadius;

    //move the target into a position wanderDist in front of the agent
    //This does not work as explained in the book as I am currently not
    // using local space.
    //targetLocal = wanderTarget + RealVector2D[wanderDistance, 0];

    //instead I will try to use the current velocity
    targetLocal = wanderTarget + (wanderDistance + atan2(entity.velocity[0], entity.velocity[1]));

    //project the target into world space
    /* targetWorld = PointToWorldSpace(targetLocal,￼entity.heading, entity.side, entity.position); */
    //and steer toward it
    ^targetLocal - entity.position;
    }
}

PathFollowing{
  *new{ arg entity, path, seekDistance = 0.5;
       var wayPoint;
      wayPoint = path.wayPoint;

      if (entity.position.distanceSq(wayPoint) < seekDistance) {path.setNextWayPoint};

      if (path.finished,
        {
        ^Arrive.new(entity, wayPoint);
        },
        {
        ^Seek.new(entity, wayPoint);
        }
      );
  }
}

/* related classes */

Path{
  var <wayPoints, <>loop, curWayPoint = 0;

  *new { arg  wayPoints, loop = false;
  ^super.newCopyArgs(wayPoints, loop).init
  }

  init{
    //PathsManager.add(this)
  }

  setNextWayPoint{
    if (wayPoints[curWayPoint] == wayPoints.last,
        {if(loop, {curWayPoint = 0})},
        {curWayPoint = curWayPoint+1}
    );
  }

  finished{
    if(loop,
      {^false},
      {^wayPoints[curWayPoint] == wayPoints.last}
    );
  }

  wayPoint{ ^wayPoints[curWayPoint]
  }

}


PathsManager{
  /* the manager is not currently used */
  classvar <paths;

  *initClass{
    paths = List.new;
  }

  *add{ arg path; paths = paths.add(path);
  }

  *clear{ paths.clear;
  }

}
