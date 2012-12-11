GroupManager { classvar <inGroup,<between1,<between2,<between3, <tailGroup ;
     
     *initClass { var makeGroupsFunction;
     
     makeGroupsFunction = 
     		{
     		inGroup= Group.head(Server.default); // create a group and put it in the head
			between1= Group.after(inGroup);
			between2= Group.after(between1);
			between3= Group.after(between2);
			tailGroup= Group.after(between3); // create a group for the FX and add it in the end //Server 
     		};
     		
	StartUp.add({
		Server.default.waitForBoot{
		
			makeGroupsFunction.value;
			
			//I have no idea what this does but the groups stay...
			Server.default.tree = {makeGroupsFunction.value}
		
		}}
	)
	}

//s.queryAllNodes;

}