
NonPenetrationConstrain{

  /* From AI by Example book p. 125 */

  *new{

    arg entity, collidingWith = List[], amountOfSeperation = 1;

    collidingWith.do{ arg curEntity;
      entity.position = this.calculate(entity.position, curEntity.position, entity.radius, curEntity.radius, amountOfSeperation)
    };
  }

  *calculate{

    arg entityPosition, obstaclePosition, entityRadius, obstacleRadius = 1, amountOfSeperation = 1;
    var toEntity, distFromEachOther, amountOfOverlap;

    toEntity = entityPosition - obstaclePosition;
    distFromEachOther = toEntity.norm;
    amountOfOverlap = (obstacleRadius + entityRadius) - distFromEachOther;
    amountOfOverlap = amountOfOverlap * amountOfSeperation;

    if (amountOfOverlap >= 0,
      {
        entityPosition = entityPosition + ( (toEntity/distFromEachOther) * amountOfOverlap );
        ^entityPosition;
      },
      {
        ^entityPosition;
      }
    );
  }

}

NonPenetrationConstrainWall{

  *new{ arg entity, entList, separationRadius = 1;
    entList.do{ arg obstacle;
      if(obstacle.class == Wall)
      {
        entity.position =
        NonPenetrationConstrain.calculate(
          entity.position,
          obstacle.closestPointOnWall(entity.position),
          entity.radius,
          separationRadius
        );
        /* entity.velocity = entity.velocity*RealVector2D[-25, -25]; */
      };
    };
  }

}
