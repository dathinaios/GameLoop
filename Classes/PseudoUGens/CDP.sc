
//************************\\
//------CDP inspired------\\
//************************\\

Stack : UGen{ //stack back to back ebband flow generator

  //goes through the file

  *ar { arg buf, transp, count, attTime, rate = 1, mul = 0.5, numChans = 1; //count: number of copies. attTime: time of attack
    var bufDur, bufRateScale, in;
      bufRateScale = BufRateScale.ir(buf);
    bufDur = BufDur.ir(buf);
    //play the normal sample
    in = PlayBuf.ar(1, buf, rate*bufRateScale) * mul;
    //generate the stack versions
    (count).do{ arg i; var curAtt, curDur, curRate;
      //to start the count from 1
      i = i+1;
      //calculate the rate
      curRate = (transp*i).midiratio; //.debug("transp");
      //calculate the duration according to the rate
      curDur = (curRate.reciprocal)*bufDur*(rate.reciprocal);
      //calc the attack time in the new duration
      curAtt = DC.ar(attTime).linlin(0, bufDur, 0, curDur);
      in = in + DelayN.ar(PlayBuf.ar(1, buf, rate*curRate*bufRateScale) * mul, bufDur, (attTime - curAtt));
    };
    ^in;
  }

}

Stack2 : UGen{ //stack back to back ebband flow generator

  //more efficient version

  *ar { arg buf, transp, count, attTime, rate = 1, mul = 0.5, numChans = 1; //count: number of copies. attTime: time of attack
    var bufDur, bufRateScale, in, delayBuf, timesArray = [], inArray = [], server, rateRecip;
    server = Server.default;
      bufRateScale = BufRateScale.ir(buf);
    rateRecip = rate.reciprocal;
    bufDur = buf.duration;
    delayBuf = {LocalBuf(server.sampleRate * (bufDur*rateRecip + 2), 1).clear};
    //delayBuf = Buffer.alloc(server, server.sampleRate * (bufDur*rateRecip), 1);
    //play the normal sample
    in = PlayBuf.ar(1, buf, rate*bufRateScale) * mul;
    //generate the stack versions
    (count).do{ arg i; var curAtt, curDur, curRate;
      //to start the count from 1
      i = i+1;
      //calculate the rate
      curRate = (transp*i).midiratio; //.debug("transp");
      //calculate the duration according to the rate
      curDur = (curRate.reciprocal)*bufDur*rateRecip;
      //calc the attack time in the new duration
      curAtt = DC.ar(attTime).linlin(0, bufDur, 0, curDur);
      //add the time as a delay
      timesArray = timesArray.add(attTime - curAtt);
      inArray = inArray.add(PlayBuf.ar(1, buf, rate*curRate*bufRateScale) * mul);
    };
    in = in + MultiInputDelay.ar(`timesArray, `inArray, bufnum: delayBuf.value);
    ^in;
  }

}

