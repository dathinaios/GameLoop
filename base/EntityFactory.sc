/*
	TODO Quote: "With the factory class which I have created you can... 
	always expect to have an entity correctly instantiated by
	 calling GameFactory.instance.getEntity( “type” ); Now, if 
	after writing several hundred lines of code which multiple 
	entities being instantiated all over the place I decide that 
	each time a certain entity is created, I will also play a sound...I
	simply add that to the Game Factory. I do not have to worry about 
	finding all cases where the object is created and changing it there. 
	Now you can see where the power of the factory method comes into play.""

*/

EntityFactory{ classvar latency;
	*new {
		"You can not have an instance of EntityFactory. Use class methods.".error;
	}
	
	*initClass{
		latency = Server.default.latency;
	}
	
	*getEntity{ arg type;
		this.subclassResponsibility("getEntity");
		/*
		case
		{type == "example"} {
			Here do what you have to do to initialise the entity.
			Should all the data being taken by enquiring on another class
			EntityParams again passing the "type"?
		}*/
		
	}
	
}
