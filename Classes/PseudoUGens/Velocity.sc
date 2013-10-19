
VelocityXY : UGen{

  //controls the sound using direction and speed aka. velocity

  *ar {arg velocityX = 0, velocityY = 0, xStart =0, yStart = 0, xSize = [-10, 10], ySize = [-10, 10];
    var azim, rad, speed;
    var x, sr;
    var y;

       sr = SampleRate.ir;

    speed = ( //finding the everage velocity with the pythagorean.
        (velocityX.abs).squared + (velocityY.abs).squared
    ).sqrt;
    //in order to start from any point
    xStart = Line.kr(xStart, xSize[0], 0.01);
    yStart = Line.kr(yStart, ySize[0], 0.01);
    //the phasors actually move the sound in speed meters/s
    x = RedPhasor2.ar(1, velocityX/sr, xStart, xSize[1], 0); //.poll(label: "X");
    y = RedPhasor2.ar(1, velocityY/sr, yStart, ySize[1], 0); //.poll(label: "Y");
    //calculate the azimuth and radius
    rad = hypot(x,y); //finding the radius,
    azim = atan2(y,x); //x.linlin(xmin, ySize, -1, 1);
    //return an array with usefull variables
    //x   //y
    ^[x, y, rad, azim, speed]

  }

  *kr {arg velocityX = 0, velocityY = 0, xStart =0, yStart = 0, xSize = [-10, 10], ySize = [-10, 10];
    var azim, rad, speed;
    var x,  sr;
    var y;

    sr = ControlRate.ir;
    speed = ( //finding the everage velocity with the pythagorean.
        (velocityX.abs).squared + (velocityY.abs).squared
    ).sqrt;

    //in order to start from any point
    xStart = Line.kr(xStart, xSize[0], 0.01);
    yStart = Line.kr(yStart, ySize[0], 0.01);
    //the phasors actually move the sound in speed meters/s
    x = RedPhasor2.kr(1, velocityX/sr, xStart, xSize[1], 0); //.poll(label: "X");
    y = RedPhasor2.kr(1, velocityY/sr, yStart, ySize[1], 0); //.poll(label: "Y");
    //calculate the azimuth and radius
    rad = hypot(x,y); //finding the radius,
    azim = atan2(y,x); //x.linlin(xmin, ySize, -1, 1);
    //return an array with usefull variables
    //x   //y
    ^[x, y, rad, azim, speed]

  }

}

Velocity : UGen{

  *ar {arg direction = 0, magnitude = 0, xStart =0, yStart = 0, xSize = [-10, 10], ySize = [-10, 10];
    var azim, rad;
    var x, y, sr;
    sr = SampleRate.ir;
    x = magnitude*(direction.cos); //find the velocity x
    y = magnitude*(direction.sin); //find the velocity y    //in order to start from any point
    xStart = Line.kr(xStart, xSize[0], 0.01);
    yStart = Line.kr(yStart, ySize[0], 0.01);
    //the phasors actually move the sound in speed meters/s
    x = RedPhasor2.ar(1, x/sr, xStart, xSize[1], 0); //.poll(label: "X");
    y = RedPhasor2.ar(1, y/sr, yStart, ySize[1], 0); //.poll(label: "Y");
    //calculate the azimuth and radius
    rad = hypot(x,y);
    azim = atan2(y,x);
    //return an array with usefull variables
    //x   //y
    ^[x, y, rad, azim, magnitude]

  }

  *kr {arg direction = 0, magnitude = 0, xStart =0, yStart = 0, xSize = [-10, 10], ySize = [-10, 10];
    var azim, rad;
    var x, y, sr;
    sr = ControlRate.ir;
    x = magnitude*(direction.cos); //find the velocity x
    y = magnitude*(direction.sin); //find the velocity y      //in order to start from any point
    xStart = Line.kr(xStart, xSize[0], 0.01);
    yStart = Line.kr(yStart, ySize[0], 0.01);
    //the phasors actually move the sound in speed meters/s
    x = RedPhasor2.kr(1, x/sr, xStart, xSize[1], 0); //.poll(label: "X");
    y = RedPhasor2.kr(1, y/sr, yStart, ySize[1], 0); //.poll(label: "Y");
    //calculate the azimuth and radius
    rad = hypot(x,y);
    azim = atan2(y,x);
    //return an array with usefull variables
    //x   //y
    ^[x, y, rad, azim, magnitude]

  }

}

Velocity1D : UGen{ //controls the sound using direction and speed aka. velocity

  *ar {arg velocity = 1, min = 1, max = 1, start = 0;
    var sr;
    sr = SampleRate.ir;
    //in order to start from any point
    min = Line.kr(start, min, 0.01);
    //the phasors actually move the sound in speed meters/s
    ^RedPhasor2.ar(1, velocity/sr, min, max, 0); //.poll(label: "X");

  }

  *kr {arg velocity = 1, min = 1, max = 1, start = 0;
    var sr;
    sr = ControlRate.ir;
    //in order to start from any point
    min = Line.kr(start, min, 0.01);
    //the phasors actually move the sound in speed meters/s
    ^RedPhasor2.kr(1, velocity/sr, min, max, 0); //.poll(label: "X");

  }
}
