(
  //~decoder = GameLoopDecoder('ATK', 'newStereo');
  //~decoder = GameLoopDecoder('ATK', 'newListen');
  ~decoder = GameLoopDecoder('AmbIEM', 'binaural');
  //Allow time for the decoder to be created....
  ~gameloop = GameLoop(40, 40, 1).play(0.05);
  //~gameloop.visualiser;
  ~gameloop.gui;
  ~gameloop.makeWalls;
)

(
  ~camera = Camera2D(
    ~gameloop.world,
    position: ~gameloop.world.center,
    radius: 0.8
  );
  ~camera.collisionType_(\mobile);

  ~cameraForcePath = {var path;
  /* steering bahavior */
  path = Path(Array.fill(rrand(2.0, 38.0), {RealVector2D[rrand(15, 25.0), rrand(15.0, 25.0)]}), true);
  ~camera.force_(
    { arg entity;
      PathFollowing.calculate(entity,path, 0.5);
    });
  };

  ~camrep =Camera2DRepresentation(
    ~gameloop.repManager,
    Color.white
  ).shape_(1);

  ~camera.attach(~camrep);

  ~camera.collisionFunc_({arg entity, entList, additionalInfo;
    entList.do{ arg obstacle;
      if(obstacle.class == Wall)
      {
        entity.velocity = entity.velocity*RealVector2D[-25,-25];
      };
    };
  });
);

(
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
    ~entity.force_({ arg entity; forceManager.sum(entity); });

    ~entity.collisionFunc_({arg entity, entList;
      //NonPenetrationConstrain.calculate(entity, entList);
      forceManager.addTemp({arg ent; ent.velocity*RealVector2D[rrand(-200.0, -500),rrand(-200.0, -500)]};)
    });

    /* ~entity.force_({ arg entity; Arrive.calculate(entity, RealVector2D[10,11]);}); */
    /* ~randForcePath.value; */
  }.value;
);


~camera.collisionFunc_({});

(
  ~camera.collisionFunc_(
    { arg entity, entList;
      CollisionResolution.nonPenetrationConstrain(entity, entList, 1.6);
      /* entity.velocity*RealVector2D[rrand(-500.0, -500),rrand(-500.0, -500)]; */
    };
  );
)

s.plotTree;
s.ping(numberOfTrials);
~gameloop.switchSpace(20, 20, 0.5);
~gameloop.switchSpace(40, 30, 1);
~gameloop.gui.visualiser;

~entity.detachAll;
~gameloop.clear;
GameLoopDecoder.instance.clear
~gameloop.clearEntities;
~camera.remove(true)

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


~entity.velocity = RealVector2D[0, 0]
~entity.force = {arg entity; Arrive.calculate(entity, RealVector2D[22, 25])};
~entity.force = {arg entity; Arrive.calculate(entity, RealVector2D[16, 22])};
~entity.force = {arg entity; Arrive.calculate(entity, RealVector2D[21, 21])};
~entity.force = {arg entity; Arrive.calculate(entity, RealVector2D[19, 20.5])};

(
  var width, position, radius;
  width = ~gameloop.world.center[0]*2;
  position = RealVector2D[rrand(2.0, width), rrand(2.0, width)];
  radius = 0.4;
  ~entity.force_(
    { arg entity; var position, path;
      position = RealVector2D[rrand(2.0, width), rrand(2.0, width)];
      path = Path(Array.fill(rrand(2.0, 38.0),
      {RealVector2D[position[0] + rrand(-33, 33.0), position[1] + rrand(-33.0, 33.0)]}),true);
      PathFollowing.calculate(entity,path, 0.5);
    });
)

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

(
r = Routine{ //test
  loop{
  ~gameloop.repManager.cameraEntity.rotateRight(0.05pi);
  0.05.wait;
  };
}.play;
)

r.stop

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



//An input and steering behavior to play around with
(
  var width, position, radius;
  width = ~gameloop.center[0]*2;
  position = RealVector2D[rrand(2.0, width), rrand(2.0, width)];
  radius = 0.4;
  ~entity = SoundEntity(
    ~gameloop.world,
    position,
    radius,
    maxSpeed: 40.0,
    /*
    input:
    { arg vel;  var in;
      in =0.3*Saw.ar(rrand(500, 20000.0))*
         LFPulse.ar(linlin(0,10, 5, rrand(10, 20.0)), mul: 0.5, add: 0.5);
      in = BPF.ar(in, rrand(200, 18000.0)*MouseX.kr(0.3, 2, lag: rrand(2.0, 6.0)), 0.4);
    },*/
    //collisionFunc: {arg entity, entList; entity.remove; entList.do{arg i; i.remove}},
    //collisionType: \mobile
  ).force_(
    { arg entity; var position, path;
      position = RealVector2D[rrand(2.0, width), rrand(2.0, width)];
      path = Path(Array.fill(rrand(2.0, 38.0),
      {RealVector2D[position[0] + rrand(-33, 33.0), position[1] + rrand(-33.0, 33.0)]}),true);
      PathFollowing.calculate(entity,path, 0.5);
    });
);

//some test (delete)
~entity.remove;
~entity.collisionType;
~gameloop.clearEntities;
~gameloop.entManager.freeList;
~gameloop.entManager.freeList.choose.remove;
GameLoopDecoder.instance.decoderBus.sources;
//GameLoopDecoder.instance.decoderBus.removeAll;
Server.default.latency = 3;
Server.default.latency;
TempoClock.default.latency;

"This is a test".postln
"This is a test".postln;
"This is a test".postln;
"This is a test".postln;
"This is a test".postln;
"This is a test".postln;

Quarks.gui
Help.methodArgs("LPF.ar")
SCVim.generateTagsFile()
GameLoop(
a = String.new(
String.find()

//TESTS
SinOsc.ar(
GameLoop(
testing.add(
testing.split(
MobileEntity(
ResponseDefs.add(
TGrains.ar(bufnum:0, numChannels)
a = "test string"
a.first()


/* =================== */
/* =================== */
/*   COLLISION TESTS   */
/* =================== */
/* =================== */

(
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

~entity1.remove; ~entity2.remove;

Quarks.gui

a = RealVector2D[0,0];
b = RealVector2D[0,3];
c = b - a;
c.norm;


RealVector2D[-2, 0.9].clip(0.1,inf)
RealVector2D[-2, 0.9].cling(0.1, 200)

/* vim marker teleportation line */



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

(
  ~entity = Vehicle(
    ~gameloop.world,
    position: RealVector2D[21,25],
    radius: 0.4,
    maxSpeed: 2.6
  );
  ~entity.collisionType_(\mobile);
  //and a visual representation
  ~visrep = SimpleVisual(~gameloop.repManager);
  /* ~visrep.collisionFunc_({arg ent; "houla".postln;}); */
  ~entity.attach(~visrep);
);

~entity.position = RealVector2D[23,30]
~entity.remove
~visrep.colliding

~gameloop.clearEntities
~gameloop.entManager.mobList
~gameloop.repManager.repList

(
  a = ForceManager();
  a.add(\test, { RealVector2D[0, 10]});
  a.add(\test1, { RealVector2D[10, 0]});
  a.sum(RealVector2D[2,0]).postln;
  a.sum;
  a.list;
)

a = Wall(RealVector2D[10,20], RealVector2D[13, 30]);
b = Wall(RealVector2D[20,20], RealVector2D[40, 35]);
~gameloop.entManager.addWall(a);
~gameloop.entManager.addWall(b);
~gameloop.entManager.removeWall(b);
~gameloop.entManager.clearWalls;

c = ~gameloop.entManager.spatialIndex.getCellsForLine(b);

c.do{ arg item; ~gameloop.entManager.spatialIndex.getCell(item).postln }

(
  Routine{
    inf.do{
      b.checkCircleWallCollision(~entity).postln;
      0.05.wait;
    };
  }.play;
)

(
  a = c.collect{
    arg item, index;
    var col = 40, rows = 40, translation;
    translation = (item[0]*col)+item[1];
    translation.postln;
  };
)

a = List[1,2,3]
a.remove(2)
a
