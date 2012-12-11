// this class is an exact copy of RedVector (part of redUniverse toolkit /redFrik)


//--vector of any dimension
MyVector[float] : FloatArray {
	mag {^this.sum{|x| x*x}.sqrt}
	distance {|redVec| ^(this-redVec).mag}
	dot {|redVec| ^(this*redVec).sum}
	normalize {^this/this.mag}
	limit {|max| if(this.mag>max, {^this.normalize*max})}
	species {^this.class}
}

//--2d vector optimised for speed
//It needs the RedUniverse bugfixes for the issue that causes
//operations reaturning an array
MyVector2D[float] : MyVector {
	mag {^this[0].sumsqr(this[1]).sqrt}
	distance {|redVec| ^(redVec[0]-this[0]).hypot(redVec[1]-this[1])}
	dot {|redVec| ^(this[0]*redVec[0])+(this[1]*redVec[1])}
	asPoint{ ^Point(this[0], this[1])}
	//my additions
	magSq {^this[0].sumsqr(this[1])} //thus skipping the sqrt
	perp { ^MyVector2D[(-1)*this[1], this[0]]}
	distanceSq{ |redVec| var ySeparation, xSeparation;
		ySeparation = redVec[1] - this[1];
		xSeparation = redVec[0] - this[0];
		^((ySeparation*ySeparation) + (xSeparation*xSeparation));
	}
}

//--3d vector optimised for speed
MyVector3D[float] : MyVector {
	mag {^(this[0].sumsqr(this[1])+this[2].pow(2)).sqrt}
	distance {|redVec| ^(redVec[0]-this[0]).hypot((redVec[1]-this[1]).hypot(redVec[2]-this[2]))}
	dot {|redVec| ^(this[0]*redVec[0])+(this[1]*redVec[1])+(this[2]*redVec[2])}
	cross {|redVec|
		var x1, y1, z1, x2, y2, z2;
		#x1, y1, z1= this;
		#x2, y2, z2= redVec;
		^MyVector3D[(y1*z2)-(z1*y2), (z1*x2)-(x1*z2), (x1*y2)-(y1*x2)]
	}
}