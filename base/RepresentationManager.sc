RepresentationManager{ classvar <repList;

	*new {"you can not have an instance of RepresentationManager".error} 
	
	*initClass{
		repList = List.new;
		CmdPeriod.add({this.clear});
	}

	*add{ arg entity; 
		repList.add(entity);
	}

	*remove{ arg entity; 
		repList.remove(entity);
	}
	
	*clear{repList.clear;
	}

}       

///////////////

