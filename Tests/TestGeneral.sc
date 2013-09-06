
TestGeneral : UnitTest{	var gameloop;

		setUp {
			"==============================================\n  Running TestGeneral test class \n  This file is located in the Tests folder. \n==============================================".postln;
			this.bootServer;
			if (GameLoopDecoder.active.not) {GameLoopDecoder('AmbIEM', 'binaural')};
			gameloop = GameLoop(40, 40, 1).play(0.05);
			gameloop.gui; 
		}

		tearDown {
			gameloop.clear;
			1.wait;
			GameLoopDecoder.clear;
		}

		test_general{
			var camera, cameraForcePath, camrep;
			var entity, soundrep, visrep, randForcePath;
			camera = Camera2D(
				gameloop.world,
				position: gameloop.world.center,
				radius: 0.8
			).init;

			cameraForcePath = {var path;
				path = Path(Array.fill(rrand(2.0, 38.0), {RealVector2D[rrand(15, 25.0), rrand(15.0, 25.0)]}), true);
				camera.force_(
					{ arg entity;
						PathFollowing.calculate(entity,path, 0.5);
					});
			};
			camrep = SimpleVisual(gameloop.repManager, Color.white).shape_(1);
			camera.attach(camrep);

			/* setup an entity */

			entity = Vehicle(
				gameloop.world,
				position: RealVector2D[18, 22],
				radius: 0.4,
				maxSpeed: 10.6
			).init;

			soundrep = SoundRepresentation(
				gameloop.repManager,
				input:{BPF.ar(in:WhiteNoise.ar(0.2), freq: rrand(100, 19000.0), rq:0.3)} 
				//collisionFunc: {arg entity, entList; entity.remove; entList.do{arg i; i.remove}}
			); 
			//entity.collisionType_(\mobile);
			entity.attach(soundrep);

			visrep = SimpleVisual(gameloop.repManager);
			entity.attach(visrep);

			randForcePath = {var path;
			path = Path(Array.fill(rrand(2.0, 38.0), {RealVector2D[rrand(15, 25.0), rrand(15.0, 25.0)]}), true);
			entity.force_(
				{ arg entity;
					PathFollowing.calculate(entity,path, 0.5);
				});
			};
			entity.force_({ arg entity; Arrive.calculate(entity, RealVector2D[19,20.2]);});

			/* move the entity around and then go to static */
			2.wait;
			"\n \n \n adding random path to the entity......".postln;
			randForcePath.value;
			4.wait;
			"\n return to original position......".postln;
			entity.force_({ arg entity; Arrive.calculate(entity, RealVector2D[19,20.2]);});

			/* now move the camera around and then go to static */
			3.wait;
			"\n testing camera motion: forward, back and rotation.......".postln;
			camera.moveFwd(2);
			3.wait;
			camera.moveBack(5);
			3.wait;
			camera.rotateRight(0.4pi);
			1.wait;
			camera.rotateRight(0.4pi);
			1.wait;
			camera.rotateLeft(0.4pi);
			3.wait;
			"\n reseting camera position to the center......".postln;
			camera.reset;

			"\n remove camera......".postln;
			camera.remove;
			1.wait;
			"\n add random path to the entity again......".postln;
			randForcePath.value;
			2.wait;
			"\n remove entity...... \n\n".postln;
			entity.remove;
		}

}
