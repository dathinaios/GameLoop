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
		{\add} {
			//create and attach the relevant rerpesentation if it exists
			representation = (message[0].class.asString++"Representation").asSymbol.asClass.new(message[0]);
			representation ?? message[0].attach(representation);
			//add the representation to the repList variable
			this.add(representation);
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
