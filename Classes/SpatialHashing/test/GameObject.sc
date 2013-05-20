GameObject{var <position, <radius;

	*new { arg position = [0,0], radius = 1.0;  
	^super.newCopyArgs(position, radius).init
	} 

	init {
	} 

}                    