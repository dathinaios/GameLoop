TestEntity : UnitTest {

    setUp {
        // this will be called before each test
    }
    tearDown {
        // this will be called after each test
    }
     
    test_yourMethod { var p;
     	
      	// every method named test_
      	// will be run
     	
      	this.assert( 6 == 6, "6 should equal 6");
     	
      	this.assertEquals( 9, 9, "9 should equal 9");
     	
      	this.assertFloatEquals( 4.0 , 1.0 * 4.0 / 4.0 * 4.0, 
      		"floating point math should be close to equal");
     
      	// we are inside a Routine, you may wait
      	1.0.wait;
     	
      	// this will wait until the server is booted
      	this.bootServer;
     	
      	// if the server is already booted it will free all nodes
      	// and create new allocators, giving you a clean slate
     	
      	p = { SinOsc.ar };
      	p.play;
      	p.register;
     	
      	// will wait until the condition is true
      	// will be considered a failure after 10 seconds
      	this.wait( { p.isPlaying }, "waiting for synth to play", 10);
     	
    }
    
}
