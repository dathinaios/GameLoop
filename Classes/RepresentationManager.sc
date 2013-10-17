RepresentationManager{ var <repList;

	*new {
	^super.new.init;
	}

	init{ repList = List.new;
	}

	//notification(s) from EntityManager
	/* update {
		arg theChanger, message; //message contains the entity and a message
		var representation;
		//here we will interpret the message
		switch (message[1])
		{\remove} {
			//remove the representation from the repList variable
			message[0].dependants.do{arg i; this.remove(i)}
		};
	}
	*/

	add{arg rep;
		repList.add(rep);
	}

	remove{ arg rep;
		repList.remove(rep);
	}

	clear{repList.clear;
	}

	activeReps{
		^repList.size;
	}

}
