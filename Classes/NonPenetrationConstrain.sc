
NonPenetrationConstrain{

	*new{ "You can not have an instance of this".error;
	}

	/* From AI by Example book p. 125 */
	*calculate{
		arg entity, collidingWith = List[], amountOfSeperation = 1;

		collidingWith.do{ arg curEntity; var toEntity, distFromEachOther, amountOfOverlap;

			toEntity = entity.position - curEntity.position;
			distFromEachOther = toEntity.norm; //curEntityPosition.dist(curEntity.position);
			amountOfOverlap = (curEntity.radius + entity.radius) - distFromEachOther;

			amountOfOverlap = amountOfOverlap * amountOfSeperation;

			if (amountOfOverlap >= 0)
			{
				entity.position = entity.position + ( (toEntity/distFromEachOther) * amountOfOverlap );
			}
		};
	}
}
