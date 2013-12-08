
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

  update { arg theChanged, message;
    switch (message[0])
    {\update}
    {visualiser.render}
    {\switchSpace}
    {visualiser.calculateMeterUnit};
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

  gui{ var button;
    gameloop.addDependant(this);
    mainView ?? {
      this.createMainView;

      this.createVisualiserButton;
      this.createWallButton;

      this.setWindowKeyActions;
    }
  }

  createVisualiserButton{var button;
      button = this.createButton;
      this.assignActionToButton(button, {visualiser.gui}, {visualiser.close});
      this.setButtonStates(button, "Visualiser", "Close Visualiser");
      this.decideStateOfVisualiserButton(button);
  }

  createWallButton{ var button;
      button = this.createButton;
      this.assignActionToButton(button, {gameloop.makeWalls}, {gameloop.clearWalls});
      this.setButtonStates(button, "Add Walls", "Remove Walls");
  }

  createMainView{
     var   h = 700, v = 400, run = true;
     mainView = Window("GameLoop", Rect(-1350, 600, h, v), false);
     mainView.addFlowLayout;
     mainView.view.background = Color.black;
     mainView.onClose = { run = false; mainView = nil; }; // stop the thread on close
     mainView.front;
     mainView.alwaysOnTop = true;
     mainView.drawFunc = { };
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

}


