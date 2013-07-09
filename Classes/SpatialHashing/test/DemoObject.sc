DemoObject{var <position, <radius, <color, <penWidth;

	*new { arg position = [0,0], radius = 1.0;  
	^super.newCopyArgs(position, radius).init
	} 

	init{   color = Color.green;
		penWidth = 0.9;
	}

	deselect{this.init;
	}

	select{color = Color.red;
	       penWidth = 2.0;
	}

}                                          
