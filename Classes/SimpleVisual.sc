
SimpleVisual : EntityRepresentation { 

	var  >color, >collisionColor, >collisionFunc;
	var <penWidth = 1.5, <>shape = 0;
							
	*new { arg  repManager, color, collisionColor, collisionFunc;  
		^super.new(repManager)
					.color_(color) 
					.collisionColor_(collisionColor)
					.collisionFunc_(collisionFunc);
	}

	init { 

		super.init;
		collisionFunc = collisionFunc ?? {{}};

		color = color ?? {Color.green};
		collisionColor = collisionColor ?? {Color.red};
		this.play;

	}

	play {
			if (entity.active.not){entity.add};
			repManager.add(this);
	}

	color { if(this.colliding, {^collisionColor },{^color})
	}

	remove{
			repManager.remove(this);
			attached = false;
	}

	draw{arg rect; 
		case
		{shape == 0} {Pen.strokeOval(rect)}
		{shape == 1} {Pen.strokeRect(rect)};
	}

	preUpdate{
	}
}   

