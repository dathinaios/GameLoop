TestGameLoopGUI : UnitTest{ var gameloop, gameLoopGUI;

  setUp {
    gameloop = GameLoop(20,20,1);
    gameLoopGUI = GameLoopGUI(gameloop);
  }

  tearDown {
    gameloop.clear;
  }

  test_gui{
    gameloop.gui;
    this.assert(gameLoopGUI.mainView.notNil, "it creates the main view");
    this.assert(gameLoopGUI.gameloop.dependants.findMatch(gameLoopGUI) == gameLoopGUI, "it adds itself as a dependant to the object passed in as gameloop");
    this.assert(GameLoopGUI.instance == gameLoopGUI, "it should have the instance stored in a class variable");
    this.assert(GameLoopGUI(gameloop) == GameLoopGUI , "it should return an error and the class if we try to create another instance");
  }

}
