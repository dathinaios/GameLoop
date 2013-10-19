//*******************************\\
//------Fill Factor TGrains------\\
//*******************************\\

TGrainsFF1 : UGen{

  //goes through the file

  *ar { arg numChannels = 2, tstretch = 1, density=1, ff = 0.5, bufnum=0, rate=1,
       pan=0, amp=1.0, loop = 1, interp=4;
     var bufDur, bufFrames, trig;

    bufDur = BufDur.ir(bufnum);
    bufFrames = BufFrames.ir(bufnum);
    trig = Impulse.ar(density);

    ^TGrains.ar(  2,
          trig,
          bufnum,
          rate,
          RedPhasor.ar(0, (bufDur*tstretch)/bufFrames,0, bufDur, loop, 0, bufDur),
          density.reciprocal*ff,
          pan,
          amp,
          interp);
    }
}

TGrainsFF2 : UGen{

  //goes through the file

  *ar { arg numChannels = 2, tstretch = 1, density=1, ff = 0.5, bufnum=0, rate=1,
       pan=0, amp=1.0, att = 0.5, dec = 0.5, loop = 1, interp=4;
     var bufDur, bufFrames, trig;

    bufDur = BufDur.ir(bufnum);
    bufFrames = BufFrames.ir(bufnum);
    trig = Impulse.ar(density);

    ^TGrains2.ar( 2,
          trig,
          bufnum,
          rate,
          RedPhasor.ar(0, (bufDur*tstretch)/bufFrames,0, bufDur, loop, 0, bufDur),
          density.reciprocal*ff,
          pan,
          amp,
          att,
          dec,
          interp);
   }

}

