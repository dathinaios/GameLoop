
Controller { var <>entity;
	
	*new { arg entity; 
		^super.newCopyArgs(entity)
	}
	
	getForce { this.subclassResponsibility("getForce");
	}
}

FlexibleController { var entity, <forceFunc;
	
	*new { arg entity, forceFunc; 
		^super.newCopyArgs(entity, forceFunc).init
	}

	init{
		forceFunc  = forceFunc ?? {{ arg entity; RealVector2D[0.0, 0.0]}};
	}
	
	getForce { arg entity; ^forceFunc.value(entity);
	}

}

