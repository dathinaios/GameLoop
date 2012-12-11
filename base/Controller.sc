
Controller { var entity, entityParams;
	
	*new { arg entity, entityParams; 
		^super.newCopyArgs(entity, entityParams)
	}
	
	getForce { this.subclassResponsibility("getForce");
	}
	
	entityParams{
		this.subclassResponsibility("entityParams")
	}
}

FlexibleController : Controller { var <>forceFunc;

	init{forceFunc = {RealVector[0,0]}}
	
	getForce { arg entity; ^forceFunc.value(entity);
	}
}  
