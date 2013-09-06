TestSimpleVisual : UnitTest { var repManager;

    setUp{
    	repManager = RepresentationManager.new;
    }

    tearDown{
    }

    test_setShape{ var representation;
    	representation = SimpleVisual.new(repManager, color: Color.white); 
    	/* representation.shape_(1); */
    	/* this.assert(shape == 'circle', */ 
    }

}
