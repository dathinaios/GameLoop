
//FIXME: Redesign the Singleton. as it is I could initialize a camera without entityParams argument.


Camera2D : Vehicle { classvar <>fwd, <>back, <>rotLeft, <>rotRight, <instance;

*new{ arg entityParams;
	if(instance.isNil, 
		{^super.newCopyArgs(entityParams).initHook1.initVars.init.initHook2},
		{"There is alredy an active instance of Camera2D".error;}
	);		
}

*initClass{
	//initialise the values for the keyboard hack
	fwd = false;
	back = false;
	rotLeft = false;
	rotRight = false;
	//make the menus with the shortcuts for the keyboard hack
	if(GUI.current.asSymbol == 'CocoaGUI',	
		{
		MenuItem.add(["Camera2D motion", "frw"],
			{Camera2D.fwd_(true)})
			.setShortCut ("i", true);
		MenuItem.add(["Camera2D motion", "back"],
			{Camera2D.back_(true)})
			.setShortCut (",", true);
		MenuItem.add(["Camera2D motion", "left"],
			{Camera2D.rotLeft_(true)})
			.setShortCut ("j", true);
		MenuItem.add(["Camera2D motion", "right"],
			{Camera2D.rotRight_(true)})
			.setShortCut ("l", true);
	});

}

*initialize{ arg manager;
	if(instance.isNil,
		{
		instance = this.new(EntityParams(['world', manager, 'theta', 0, 'mass', 0.1, 'maxSpeed', 5/*7*/]));
		instance.attach(Camera2DVRep(instance, instance.entityParams)); 
		instance.activate;
		 ^instance;
		},
		{^instance});
	
}

initHook1 { 
	var window, view;
	controller = Camera2DCtrl(this, entityParams);

	//Hack for keyboard input
	// we create a window
	window = Window.new("Play a keyboard", Rect(0,0, 500, 500));
	// to be is not to appear, better explain this to window
	window.front;
	view = TextView.new(window, window.view.bounds)
	   .stringColor_(Color(1,1,1))
	   .background_(Color(0.1, 0, 0, 0.9))
	   .font_(Font("Palatino", 20)) ;
	view.background = Color(0.1, 0.8, 0) ;
	view.keyDownAction = { arg view, char, modifiers, unicode, keycode;
		case
		{unicode == 105} //"frw"
		{Camera2D.fwd_(true)}
		{unicode == 44} //"back"      
		{Camera2D.back_(true)}
		{unicode == 106} //"left"      
		{Camera2D.rotLeft_(true)}
		{unicode == 108} //"right"
		{Camera2D.rotRight_(true)};
	};
}

add{
	world.addFree(this)
}

remove{
	world.removeFree(this)
}

*applyTransformation{ arg ent;
	^instance.applyTransformation(ent);
}

*position{
	^instance.position
}

*position_{ arg i;
	instance.position_(i);
}

applyTransformation{ arg entity; 
					 var entPos, newPos, theta, thetaSin, thetaCos, rad, x,y;
					 var xMinusx, yMinusy;
	//translate position according to rotation	
	entPos = entity.position;
	theta = entityParams.get['theta'];
	thetaSin = theta.sin;
	thetaCos = theta.cos;
	xMinusx = entPos[0] - position[0];
	yMinusy = entPos[1] - position[1];
	x = (xMinusx * thetaCos) - (yMinusy * thetaSin);
	y = (xMinusx * thetaSin) + (yMinusy * thetaCos);
	^RealVector[x,y];
}

moveFwd{arg amount = 0.02; var theta, x, y;
	theta = entityParams.get['theta'];
	y = theta.cos;
	x = theta.sin;
	position = position + (amount *RealVector[x, y]);
}

moveBack{arg amount = 0.02; var theta, x, y;
	theta = entityParams.get['theta'];
	y = theta.cos;
	x = theta.sin;
	position = position - (amount *RealVector[x, y]);
}


moveLeft{arg amount = 0.23; 
	position = position - RealVector[amount, 0]
}

rotateLeft{arg amount = 0.001pi;
	entityParams.set('theta', entityParams.get['theta'] - amount)
}

rotateRight{arg amount = 0.001pi;
	entityParams.set('theta', entityParams.get['theta'] + amount)
}

reset{
	position = world.center;
	entityParams.set('theta', 0);
}

}

Camera2DCtrl : Controller{
	
	getForce { arg entity, amount = 9, rotAmount = 0.025pi; 
			var theta, x,y, fwd, back, rotLeft, rotRight;
			fwd = Camera2D.fwd;
			back = Camera2D.back;
			rotLeft = Camera2D.rotLeft;
			rotRight = Camera2D.rotRight;
			if(fwd || back || rotLeft || rotRight,
				{
				  case
					{fwd}
					{ 	
						Camera2D.fwd_(false);
						theta = entityParams.get['theta'];
						y = theta.cos;
						x = theta.sin;
						^(entity.velocity + (amount *RealVector[x, y].normalize));
					}
					{back}
					{ 	
						Camera2D.back_(false);
						theta = entityParams.get['theta'];
						y = theta.cos;
						x = theta.sin;
						^(entity.velocity - (amount *RealVector[x, y].normalize));
					}
					{rotLeft}
					{ 	Camera2D.rotLeft_(false);
						entityParams.set('theta', entityParams.get['theta'] - rotAmount);
						^RealVector[0,0]
					}
					{rotRight}
					{ 	Camera2D.rotRight_(false);
						entityParams.set('theta', entityParams.get['theta'] + rotAmount);
						^RealVector[0,0]
					};
				},
				{
					entity.velocity = entity.velocity * 0.5;
					^RealVector[0,0]
				}
			)
	}

}

Camera2DVRep : VisualRep{

	draw{arg rect; Pen.strokeRect(rect)}

}


/*	
		{Camera2D.instance.moveFwd(0.4)}).setShortCut ("i", true);
CocoaMenuItem.add(["Camera2D motion", "back"],
		{Camera2D.instance.moveBack(0.4)}).setShortCut (",", true);
CocoaMenuItem.add(["Camera2D motion", "left"],
		{Camera2D.instance.rotateLeft(0.025pi)}).setShortCut ("j", true);
CocoaMenuItem.add(["Camera2D motion", "right"],
		{Camera2D.instance.rotateRight(0.025pi)}).setShortCut ("l", true);
*/

//This is a comment to practice vim and nothing else
