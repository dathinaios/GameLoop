RepresentationManager{ var <repList;

	*new {
	^super.new.init;
	} 

	init{ repList = List.new;
	}

	//notification(s) from EntityManager
	update { arg theChanger, message; //message contains the entity and a message
			 var representation;
		//here we will interpret the message
		switch (message[1])
		{\prepare} {
			//create and attach the relevant rerpesentation if it exists 
			//(notice that the repManager is passed as the second arg in 
			//order to allow the EntityRepresemtation to add itself later)
			representation = 
				(message[0].class.asString++"Representation").asSymbol.asClass.new(message[0], this);
			representation ?? message[0].attach(representation);
		}
		{\remove} {
			//remove the representation from the repList variable
			message[0].dependants.do{arg i; this.remove(i)}
		};
	}

	add{arg entity; 
		repList.add(entity);
	}

	remove{ arg entity; 
		repList.remove(entity);
	}
	
	clear{repList.clear;
	}
}       
