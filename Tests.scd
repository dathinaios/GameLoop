
( // GENERAL SETUP
  //~decoder = GameLoopDecoder('ATK', 'newStereo');
  //~decoder = GameLoopDecoder('ATK', 'newListen');
  ~decoder = GameLoopDecoder('AmbIEM', 'binaural');
  //Allow time for the decoder to be created
  ~gameloop = GameLoop(80, 80, 1).play(0.05);
  //~gameloop.visualiser;
  ~gameloop.gui;
  ~gameloop.makeWalls;
  /* a = Wall(RealVector2D[10,20], RealVector2D[13, 30]); */
  /* b = Wall(RealVector2D[23,20], RealVector2D[40, 35]); */
  /* ~gameloop.entManager.addWall(a); */
  /* ~gameloop.entManager.addWall(b); */
)

( // CAMERA SETUP
  ~camera = Camera2D(
    ~gameloop.world,
    position: ~gameloop.world.center,
    radius: 0.8
  );
  ~camera.collisionType_(\mobile);
  ~camrep =Camera2DRepresentation(
    ~gameloop.repManager,
    Color.white
  ).shape_(1);

  ~camera.attach(~camrep);

  /* ~camera.friction_(0.9); */
);

( // ENTITY
  { var forceManager;
    ~entity = Vehicle(
      ~gameloop.world,
      position: RealVector2D[rrand(8.0, 32), rrand(8.0, 32)],
      radius: 0.4,
      maxSpeed: 5.6
    );

    ~rep = SoundRepresentation(
      ~gameloop.repManager,
      input:{BPF.ar(in:WhiteNoise.ar(0.2), freq: rrand(100, 19000.0), rq:0.3)}
    );

    /* ~entity.collisionType_(\mobile); */
    ~entity.attach(~rep);
    //and a visual representation
    ~visrep = SimpleVisual(~gameloop.repManager);
    ~entity.attach(~visrep);

    ~randForcePath = {var path;
      path = Path(Array.fill(rrand(2.0, 38.0), {RealVector2D[rrand(15, 25.0), rrand(15.0, 25.0)]}), true);
      { arg entity; PathFollowing.calculate(entity,path, 0.5); };
    };

    ~entity.force_(~randForcePath.value);

  }.value;
)

~gameloop.makeWalls;
~gameloop.switchSpace(20, 20, 0.5);
~gameloop.switchSpace(200, 200, 0.5);
~gameloop.switchSpace(40, 40, 1);
~gameloop.switchSpace(40, 30, 1);
~gameloop.switchSpace(80, 80, 0.5);

~entity.detachAll;
~gameloop.clear;
GameLoopDecoder.instance.clear
~gameloop.clearEntities;
~gameloop.clearWalls;
~camera.remove(true)

~gameloop.entManager.mobList;
~camera.maxForce = 1000;
~camera.mass= 0.05;
~camera.maxSpeed = 10;
~camera.motionAmount = 30;
~camera.rotationAmount = 0.01pi;
~camera.friction = 0.9;
~camera.remove(true);

~camera.force_({arg ent; Arrive.calculate(ent, RealVector2D[24,20])});
~entity.force_({ arg entity; Arrive.calculate(entity, RealVector2D[19,20.2]);});
~cameraForcePath.value;
~randForcePath.value;

~entity.detachAll;
~entity.detach(~rep);
~entity.remove;
~gameloop.repManager.repList;
~camera.moveFwd(2);
~camera.moveBack(2);
~camera.reset;
~camera.rotateRight(0.4pi);
~camera.rotateLeft(0.4pi);
~camera.remove

~gameloop.visualisation;
~gameloop.guiClose;

~gameloop.resetCamera;
//clear the entities
~gameloop.clearEntities;
//clear and close everything
~gameloop.clear;
~gameloop.removeCamera;

~gameloop.repManager.repList
GameLoopDecoder.instance.decoderBus.sources;
GameLoopDecoder.instance.decoderProxy.sources

Server.default.queryAllNodes;
GroupManager.inGroup;
GroupManager.tailGroup;
GameLoop.instance.dt
~gameloop.entManager.clear;
~entity.do{arg i; i.remove;};
~entity[1].remove

~gameloop.cameraEntity.collisionType
~gameloop.cameraEntity.moveFwd(4);
~gameloop.cameraEntity.moveBack(4);
~gameloop.cameraEntity.goto(RealVector2D[rrand(5.0, 35.0),rrand(6.0, 30.0)]);
~gameloop.cameraEntity.rotateLeft(0.1pi);
~gameloop.cameraEntity.rotateRight(0.1pi);
~gameloop.cameraEntity.reset;
~gameloop.clear;
~gameloop.removeCamera;
~gameloop.stop
~gameloop.play
~gameloop.world.freeList;
~gameloop.staticList;
~routiniaris.stop
~gameloop.mainRoutine.play
~gameloop.mainRoutine.stop
CmdPeriod.run



//VARIOUS TESTS

( // WANDER TEST
  { var forceManager;
    ~entity = Vehicle(
      ~gameloop.world,
      position: RealVector2D[rrand(8.0, 32), rrand(8.0, 32)].debug("position"),
      radius: 0.4,
      maxSpeed: 5.6
    );

    ~rep = SoundRepresentation(
      ~gameloop.repManager,
      input:{BPF.ar(in:WhiteNoise.ar(0.2), freq: rrand(100, 19000.0), rq:0.3)}
    );

    ~entity.collisionType_(\mobile);
    ~entity.attach(~rep);
    //and a visual representation
    ~visrep = SimpleVisual(~gameloop.repManager);
    ~entity.attach(~visrep);

    ~randForcePath = {var path;
      path = Path(Array.fill(rrand(2.0, 38.0), {RealVector2D[rrand(15, 25.0), rrand(15.0, 25.0)]}), true);
      { arg entity; PathFollowing.calculate(entity,path, 0.5); };
    };
    forceManager = ForceManager([\pathSteer, ~randForcePath.value]);

    //SLIDERS TO DEBUG WANDER
    Slider(
      GameLoopGUI.instance.mainView.view,
      Rect(0, 200, 50, 200);
    ).action_({ arg i; (~wanderRadius = 100 * i.value).postln });

    Slider(
      GameLoopGUI.instance.mainView.view,
      Rect(100, 200, 50, 200);
    ).action_({ arg i; (~wanderDistance = 100 * i.value).postln });

    Slider(
      GameLoopGUI.instance.mainView.view,
      Rect(200, 200, 50, 200);
    ).action_({ arg i; (~wanderJitter = 100 * i.value).postln });


    ~wanderRadius = 1;
    ~wanderDistance = 1;
    ~wanderJitter = 1;

    ~entity.force_({ arg entity;
      Wander.calculate(entity,
        wanderRadius: ~wanderRadius,
        wanderDistance: ~wanderDistance,
        wanderJitter: ~wanderJitter
      );
      0
      /* forceManager.sum(entity); */
    });

    ~entity.collisionFunc_({arg entity, entList;
      NonPenetrationConstrain(entity,entList);
      /* forceManager.addTemp({arg ent; ent.velocity*RealVector2D[rrand(-200.0, -500),rrand(-200.0, -500)]};) */
    });

    /* ~entity.force_({ arg entity; Arrive.calculate(entity, RealVector2D[10,11]);}); */
    /* ~randForcePath.value; */
  }.value;
)

( //COLLISION TESTS
  ~entity1 = Vehicle(
    ~gameloop.world,
    position: RealVector2D[25, 20.5],
    radius: 0.4,
    maxSpeed: 10.0
  );

  ~entity2 = Vehicle(
    ~gameloop.world,
    position: RealVector2D[15, 20.5],
    radius: 0.4,
    maxSpeed: 10.0
  );

  ~entity1.collisionType_(\mobile);
  ~entity2.collisionType_(\mobile);

  ~visrep1 = SimpleVisual(~gameloop.repManager);
  ~entity1.attach(~visrep1);
  ~visrep2 = SimpleVisual(~gameloop.repManager);
  ~entity2.attach(~visrep2);

  ~soundRep1 = SoundRepresentation(
    ~gameloop.repManager,
    //collisionFunc: {arg entity, entList; entity.remove; entList.do{arg i; i.remove}}
  );
  ~entity1.attach(~soundRep1);

  ~soundRep2 = SoundRepresentation(
    ~gameloop.repManager,
    //collisionFunc: {arg entity, entList; entity.remove; entList.do{arg i; i.remove}}
  );
  ~entity2.attach(~soundRep2);

  ~entity1.force_({arg entity; Arrive.calculate(entity, RealVector2D[18, 20.5])});
  ~entity2.force_({arg entity; Arrive.calculate(entity, RealVector2D[22, 20.5])});

    ~randForcePath = {arg entity; var path;
    /* steering bahavior */
    path = Path(Array.fill(rrand(2.0, 38.0), {RealVector2D[rrand(5, 35.0), rrand(5.0, 35.0)]}), true);
    entity.force_(
      { arg entity;
        PathFollowing.calculate(entity,path, 0.5);
      });
    };

  ~randForcePath.value(~entity1);
  ~randForcePath.value(~entity2);
)

( //ASCII LOGO



                GGGGGGGGGGGGG                                                            LLLLLLLLLLL
             GGG.:::::::::::G                                                            L.::::::::L
           GG.:::::::::::::G                                                             L.::::::::L
          G.::::GGGGGGGG.:::G                                                            LL.::::::LL
         G.::::G       GGGGGG  aaaaaaaaaaaaa      mmmmmmm    mmmmmmm       eeeeeeeeeeee    L.::::L                  ooooooooooo      ooooooooooo   ppppp   ppppppppp
        G.::::G                a.:::::::::::a   mm.::::::m  m.::::::mm   ee.:::::::::::ee  L.::::L                oo.::::::::::oo  oo.::::::::::oo p.:::ppp.::::::::p
        G.::::G                aaaaaaaaa.::::a m.:::::::::mm.:::::::::m e.:::::eeeee.::::eeL.::::L               o.::::::::::::::oo.::::::::::::::oP.::::::::::::::::p
        G.::::G    GGGGGGGGGG           a.:::a m.:::::::::::::::::::::me.:::::e     e.::::eL.::::L               o.::::ooooo.::::oo.::::ooooo.::::opp.:::::ppppp.:::::p
        G.::::G    G.:::::::G    aaaaaaa.::::a m.::::mmm.:::::mmm.::::me.::::::eeeee.:::::eL.::::L               o.:::o     o.:::oo.:::o     o.:::o p.::::p     p.::::p
        G.::::G    GGGGG.:::G  aa.:::::::::::a m.:::m   m.:::m   m.:::me.::::::::::::::::e L.::::L               o.:::o     o.:::oo.:::o     o.:::o p.::::p     p.::::p
        G.::::G        G.:::G a.:::aaaa.:::::a m.:::m   m.:::m   m.:::me.:::::eeeeeeeeeee  L.::::L               o.:::o     o.:::oo.:::o     o.:::o p.::::p     p.::::p
         G.::::G       G.:::Ga.:::a    a.::::a m.:::m   m.:::m   m.:::me.::::::e           L.::::L         LLLLLLo.:::o     o.:::oo.:::o     o.:::o p.::::p    p.:::::p
          G.::::GGGGGGGG.:::Ga.:::a    a.::::a m.:::m   m.:::m   m.:::me.:::::::e        LL.::::::LLLLLLLLL.::::Lo.::::ooooo.::::oo.::::ooooo.::::o p.::::ppppp.::::::p
           GG.::::::::::::::Ga.::::aaaa.:::::a m.:::m   m.:::m   m.:::m e.:::::::eeeeeeeeL.:::::::::::::::::::::Lo.::::::::::::::oo.::::::::::::::o p.:::::::::::::::p
             GGG.:::::GGG.::G a.:::::::::aa.::am.:::m   m.:::m   m.:::m  ee.::::::::::::eL.:::::::::::::::::::::L oo.::::::::::oo  oo.::::::::::oo  p.:::::::::::::pp
                GGGGGG   GGGG  aaaaaaaaaa  aaaammmmmm   mmmmmm   mmmmmm    eeeeeeeeeeeeeeLLLLLLLLLLLLLLLLLLLLLLLL   ooooooooooo      ooooooooooo    p.:::::pppppppp
                                                                                                                                                    p.::::p
                                                                              - "Press D for Help" -                                                p.::::p
                                                                                                                                                   p.::::::p
                                                                                                                                                   p.::::::p
                                                                                                                                                   p.::::::p
                                                                                                                                                   ppppppppp
)

( //FORCE MANAGER TEST
  a = ForceManager();
  a.add(\test, { RealVector2D[0, 10]});
  a.add(\test1, { RealVector2D[10, 0]});
  a.sum(RealVector2D[2,0]).postln;
  a.sum;
  a.list;
)

( //WALL TESTS
  a = Wall(RealVector2D[5,10], RealVector2D[13, 29]);
  b = Wall(RealVector2D[20,20], RealVector2D[40, 35]);
  ~gameloop.entManager.addWall(a);
  ~gameloop.entManager.addWall(b);
  ~gameloop.entManager.removeWall(b);
  ~gameloop.entManager.clearWalls;

  c = ~gameloop.entManager.spatialIndex.getCellsForLine(b);

  c.do{ arg item; ~gameloop.entManager.spatialIndex.getCell(item).postln }
)

