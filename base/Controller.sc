
Controller { var entity, <forceFunc;
	
	*new { arg entity, forceFunc; 
		^super.newCopyArgs(entity, forceFunc).init
	}

	init{
		forceFunc  = forceFunc ?? {{ arg entity; RealVector[0.0]}};
	}
	
	getForce { arg entity; ^forceFunc.value(entity);
	}

}
