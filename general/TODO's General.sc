/*
	TODO FAILURE /n_set Node not found when starting the MobileUnit
	TODO The units should be able to choose easily between types of output (binaural, ambisonic etc.)
	TODO make a class or an extension for cartesian to polar conversions
	TODO A mechanism to add a force one time and then remove it 
	and one to add a constant force such as gravity	and a method 
	to remove it if we want.
	TODO Account for variable frame rate
	TODO Using the .onEnd({entity.remove(false)}) that removes the entity when the sound
	event is finoshed seems a bit inelegant but is there another way? In general give some
	thought to the methods for ending an entity and how they may be implemented cleanly.
	TODO Implement the concept of triggers
	TODO A mechanism to be able to have things activating the passive ones
	without removing themselves. It could happen by checking if the thing that they hit is still.
	(I did it by checking the type and if it is PEFEnt to not remove. Not very elegant)
	TODO When one object is not removed after collision we want a convenient way to have only one 
	effect sound until is hit again. Now the effect repeats because the collision is detected every frame.
	TODO Is it ok that I am using the entityParams for the controller and the representation as well?
*/