
/* For implementation details refer to the book Game AI by example by Mat Buckland */

Seek {

  *new{ "You can not have an instance of this".error;
  }

  *calculate{ arg entity, targetPos = RealVector2D[10,13];
       var desiredVelocity;

    desiredVelocity = targetPos - entity.position;
    desiredVelocity = desiredVelocity.normalize;
    desiredVelocity = desiredVelocity * entity.maxSpeed;
    ^(desiredVelocity - entity.velocity);
  }

}

Arrive {

  //Deceleration{slow = 3, normal = 2, fast = 1};

  *new{ "You can not have an instance of this".error;
  }

  *calculate{ arg entity, targetPos = RealVector2D[10,13], deceleration = 2, tweak = 0.3;
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

PathFollowing{

  *new{ "You can not have an instance of this".error;
  }

  *calculate{ arg entity, path, seekDistance = 0.5;
       var wayPoint;
      wayPoint = path.wayPoint;

      if (entity.position.distanceSq(wayPoint) < seekDistance) {path.setNextWayPoint};

      if (path.finished,
        {
        ^Arrive.calculate(entity, wayPoint);
        },
        {
        ^Seek.calculate(entity, wayPoint);
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


//Red Universe extension

RedSeek {

  *new{ "You can not have an instance of this".error;
  }

  *calculate{ arg entity, targetPos = RedVector2D[10,13], maxSpeed; //^force
       var desiredVelocity; //, maxSpeed = 30;

    //seek steering behaviour
    desiredVelocity = targetPos - entity.loc;
    desiredVelocity = desiredVelocity.normalize;
    desiredVelocity = desiredVelocity * maxSpeed;
    ^(desiredVelocity - entity.vel);
  }
}


