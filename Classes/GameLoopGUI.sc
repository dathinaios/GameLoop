
GameLoopGUI{
       classvar <instance;
       var <gameloop, <entManager, <repManager;
       var dimensions, gridSize, cellSize, <mainView;
       var leftRotationRoutine, rightRotationRoutine, fwdRotationRoutine, backRotationRoutine;
       var visualiser;

  *new{ arg gameloop;
      if(instance.isNil,
        { ^super.newCopyArgs(gameloop).init },
        {"There is already an active instance of GameLoopGUI".error }
      );
  }

  init{
    entManager = gameloop.entManager;
    repManager = gameloop.repManager;
    visualiser   = GameLoopVisualiser(entManager,repManager);
    instance = this;
    CmdPeriod.add({this.clear});
    this.initCameraRoutines;
    visualiser.gui;
    this.gui;
  }

  /* Public */

  update { arg theChanged, message;
    switch (message[0])
    {\update}
    {visualiser.render}
    {\switchSpace}
    {visualiser.calculateMeterUnit};
  }

  gui{ var button;
    gameloop.addDependant(this);
    mainView ?? {
      this.createMainView;

      this.createVisualiserButton;
      this.createWallButton;
      this.createQuitButton;

      this.setWindowKeyActions;
    }
  }

  close {
    if(mainView.notNil, {mainView.close}, {"There is no view open for GameLoopGUI".error});
  }

  clear{
    if(mainView.notNil, {mainView.close});
    instance = nil;
    visualiser.clear;
    gameloop.removeDependant(this);
  }

  /* Private */

  createMainView{
     var   h = 700, v = 400, run = true;
     mainView = Window("GameLoop", Rect(-1350, 600, h, v), false);
     mainView.addFlowLayout(10@10, 10@10);
     mainView.view.background = Color.black;
     mainView.onClose = { run = false; mainView = nil; }; // stop the thread on close
     mainView.front;
     /* mainView.alwaysOnTop = true; */
     mainView.drawFunc = { };
  }

  createVisualiserButton{var button;
      button = this.createButton;
      this.assignActionToButton(button, {visualiser.gui}, {visualiser.close});
      this.setButtonStates(button, "Visualiser", "Close Visualiser");
      this.decideStateOfVisualiserButton(button);
  }

  createWallButton{ var button;
      button = this.createButton;
      this.assignActionToButton(button, {gameloop.makeEdgeWalls}, {gameloop.clearEdgeWalls});
      this.setButtonStates(button, "Add Fence", "Remove Fence");
  }

  createQuitButton{ var button;
      button = this.createButton;
      this.assignActionToButton( button,
        {this.displayQuitButtonConfirmationDialog(button)}
      );
      this.setButtonStates(button, "Quit", "");
  }

  displayQuitButtonConfirmationDialog{ arg button;
    this.popUpWarning(
      "Are you sure you want to quit GameLoop",
      {this.clear; gameloop.clear; },
      {button.value = 0}
    );
  }

  createButton{
      ^Button(mainView, Rect(10,10,150,30)).canFocus_(false);
  }

   setButtonStates{ arg button, offStateString = "off", onStateString = "on";
     button.states_([
           [offStateString, Color.grey, Color.black],
           [onStateString, Color.green, Color.black]
     ]);
   }

  assignActionToButton{ arg button, onAction, offAction;
    button.action_({arg butt;
      switch (butt.value)
      {1}{onAction.value}
      {0}{offAction.value};
    });
  }

  decideStateOfVisualiserButton{ arg button;
     if (visualiser.mainView != nil,
        {button.value = 1},
        {button.value = 0}
      );
  }

  decideStateOfWallButton{ arg button;
     if (entManager.edgeWalls.size != 0,
        {button.value = 1},
        {button.value = 0}
      );
  }

  setWindowKeyActions{
      mainView.view.keyDownAction =
        {arg view, char, modifiers, unicode, keycode;
          switch (keycode)
          {126}
          {
            if(fwdRotationRoutine.isPlaying.not)
              {fwdRotationRoutine.reset.play};
          }
          {125}
          {
            if(backRotationRoutine.isPlaying.not)
              {backRotationRoutine.reset.play};
          }
          {123}
          {
            if(leftRotationRoutine.isPlaying.not)
              {leftRotationRoutine.reset.play};
          }
          {124}
          {
            if(rightRotationRoutine.isPlaying.not)
              {rightRotationRoutine.reset.play};
          }
        };

      mainView.view.keyUpAction =
        {arg view, char, modifiers, unicode, keycode;
          switch (keycode)
          {123}{leftRotationRoutine.stop}
          {124}{rightRotationRoutine.stop}
          {125}{backRotationRoutine.stop}
          {126}{fwdRotationRoutine.stop}
        };

  }

  initCameraRoutines{
    leftRotationRoutine = Routine{
      loop{
      Camera2D.instance.rotateLeft;
      0.05.wait;
      };
    };

    rightRotationRoutine = Routine{
      loop{
      Camera2D.instance.rotateRight;
      0.05.wait;
      };
    };

    fwdRotationRoutine = Routine{
      loop{
      Camera2D.instance.forceFwd;
      0.05.wait;
      };
    };

    backRotationRoutine = Routine{
      loop{
      Camera2D.instance.forceBack;
      0.05.wait;
      };
    };
  }

  popUpWarning {
    arg string, action, cancelAction;
    var dialog, buttonColor, buttonTextColor, destructiveButtonColor, destructiveButtonTextColor;

    dialog = Window.new("",Rect((mainView.bounds.left)+200, (mainView.bounds.top)+120, 280, 112), border: false).front;
    destructiveButtonColor = Color.new255(106,106,126);
    destructiveButtonTextColor = Color.white;
    buttonColor = Color.new255(106,106,126);
    buttonTextColor = Color.white;

    if(string.isNil, {string = "Are you sure?"});
    StaticText.new(dialog,Rect(30, 10, 220, 50))
    .string_(string)
    .align_(\left);
    Button.new(dialog,Rect(30, 70, 100, 20))
    .states_([ [ "Do it", destructiveButtonTextColor, destructiveButtonColor] ])
    .action_{|v| action.value; dialog.close};
    Button.new(dialog,Rect(150, 70, 100, 20))
    .states_([ [ "No thanks",buttonTextColor, buttonColor] ])
    .action_{|v| cancelAction.value; dialog.close};
  }

}


