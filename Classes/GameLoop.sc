
GameLoop{
       classvar <instance;
       var <sceneWidth, <sceneHeight, <cellSize;
       var <entManager, <repManager;
       var <mainRoutine;

  *new{ arg sceneWidth = 40, sceneHeight = 40, cellSize = 1;
      if(instance.isNil,
        {
        ^super.newCopyArgs(sceneWidth, sceneHeight, cellSize).init;
        },
        {"There is already an active instance of GameLoop".error;}
      );
  }

  init{
    instance   = this;
    entManager = EntityManager(SpatialHashing(sceneWidth, sceneHeight, cellSize));
    repManager = RepresentationManager.new;
    CmdPeriod.add({this.clear});
  }

  switchSpace{ arg width = sceneWidth, height = sceneHeight, cell = cellSize;
    entManager.newIndex(SpatialHashing(width, height, cell));

    sceneWidth = width;
    sceneHeight = height;
    cellSize = cell;

    this.clearEntities;
    this.resetCamera;
  }

  gui{var gui;
      gui = GameLoopGUI(this);
      this.addDependant(gui);
  }

  guiClose{
      GameLoopGUI.instance.clear;
  }

  world{
    ^entManager
  }

  play{ arg rate;
    if (mainRoutine.isNil,
      {
        if(rate != nil, {entManager.dt = rate});
        mainRoutine = Routine{
          inf.do{
            entManager.doAll;
            this.changed([\update]);
            entManager.dt.wait;
            }
        }.play(TempoClock.default)
      },
      {
        mainRoutine.reset.play;
      }
    );
  }

  stop{
    mainRoutine.stop;
  }

  center{
    ^RealVector2D[sceneWidth * 0.5, sceneHeight*0.5];
  }

  dt{
    ^entManager.dt;
  }

  clear{
    mainRoutine.stop;
    entManager.clear;
    this.removeCamera;
    this.guiClose;
    instance = nil;
  }

  clearEntities{
    entManager.clear;
  }

  currentCamera{
    ^Camera2D.instance;
  }

  resetCamera{
    this.currentCamera.reset;
  }

  removeCamera{
    this.currentCamera.remove(true);
  }

  makeWalls{
    entManager.addWall(Wall(RealVector2D[0, sceneHeight], RealVector2D[0, 0]));
    entManager.addWall(Wall(RealVector2D[0, 0], RealVector2D[sceneWidth, 0]));
    entManager.addWall(Wall(RealVector2D[sceneWidth, 0], RealVector2D[sceneWidth, sceneHeight]));
    entManager.addWall(Wall(RealVector2D[sceneWidth, sceneHeight], RealVector2D[0, sceneHeight]));
  }

}
