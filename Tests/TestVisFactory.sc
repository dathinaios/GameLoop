
TestVisFactory : EntityFactory {

	*getEntity{ arg type, entityParams;var entity, soundRep, clock;
		case
		{type == 'SimpleCircle'}
		{ 
			entity = Vehicle(entityParams);
			entity.attach(SimpleCircle(entity, entityParams)); //for visualisation	
			entity.activate;
			^entity;
		}
	}
}
