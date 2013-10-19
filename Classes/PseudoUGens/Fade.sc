Fade : UGen{

  //this creates a simple fade in and fade out
  //while makes sure the whole process wont take more than duration

  *kr { arg  inTime = 0.015, outTime = 0.015, duration = 0.1, gate = 1, curve = 'exponential', tweek = 0.000001;
     var env, trig = 1, durTrig;

    env = Env.new(
      [tweek, 1, tweek],[inTime, outTime], curve,1
    );
    durTrig = SetResetFF.kr(DC.kr(1), TDelay.kr(DC.kr(1), duration-outTime));
    ^EnvGen.kr(env, gate*durTrig, doneAction: 2);
  }

}

Fade2 : UGen{

  //this creates a simple fade in and fade out

  *kr { arg  inTime = 0.015, outTime = 0.015, gate = 1, curve = 'exponential', tweek = 0.000001;
     var env;

    env = Env.new(
      [tweek, 1, tweek],[inTime, outTime], curve, 1
    );
    ^EnvGen.kr(env, gate, doneAction: 2);
  }

}

FadeAtt : UGen{

  //this creates a simple fade in and fade out but adding a sharp attack
  //while makes sure the whole process wont take more than duration

  *kr { arg  inTime = 0.015, decTime = 0.05, outTime = 0.015, attLevel =1, duration = 0.1, gate = 1, curve = 'exponential';
     var env, trig = 1, durTrig;

    env = Env.new(
      [0.000001, attLevel, 1, 0.000001],[inTime, decTime, outTime], curve, 2
    );
    durTrig = SetResetFF.kr(DC.kr(1), TDelay.kr(DC.kr(1), duration-outTime));
    ^EnvGen.kr(env, gate*durTrig, doneAction: 2);
  }

  *ar { arg  inTime = 0.015, decTime = 0.05, outTime = 0.015, attLevel =1, duration = 0.1, gate = 1, curve = 'exponential';
     var env, trig = 1, durTrig;

    env = Env.new(
      [0.000001, attLevel, 1, 0.000001],[inTime, decTime, outTime], curve, 2
    );
    durTrig = SetResetFF.kr(DC.kr(1), TDelay.kr(DC.kr(1), duration-outTime));
    ^EnvGen.ar(env, gate*durTrig, doneAction: 2);
  }

}

Fade2Att : UGen{

  //this creates a simple fade in and fade out

  *kr { arg  inTime = 0.015, decTime = 0.05, outTime = 0.015, attLevel =1, gate = 1, curve = 'exponential';
     var env;

    env = Env.new(
      [0.000001, attLevel, 1, 0.000001],[inTime, decTime, outTime], curve, 2
    );
    ^EnvGen.kr(env, gate, doneAction: 2);
  }

  *ar { arg  inTime = 0.015, decTime = 0.05, outTime = 0.015, attLevel =1, gate = 1, curve = 'exponential';
     var env;

    env = Env.new(
      [0.000001, attLevel, 1, 0.000001],[inTime, decTime, outTime], curve, 2
    );
    ^EnvGen.ar(env, gate, doneAction: 2);
  }

}

//usage
//a = {arg gate = 1; SinOsc.ar(200, 0, 0.1)*Fade.kr(inTime: 1, outTime: 4, duration: 5)}.play;
//a.release
//a.release(8)
