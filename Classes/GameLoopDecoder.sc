
GameLoopDecoder {

    classvar <instance, <active = false;
    var <>library, <>type, <>doppler, <decoderProxy, <summingProxy,
    encoderChannels, decoderChannels, order, kernel;

  *new{ arg library = 'AmbIEM', type = 'binaural', doppler = true;
      if(instance.isNil,
        {
        ^super.newCopyArgs(library, type, doppler).init;
        },
        {"There is already an active instance of GameLoopDecoder".error;}
      );
  }

  init{

    instance  = this;
    active = true;

    this.calculateOrderAndNumOfChannels;
    this.calculateEncoderChannels;
    this.createDecoderProxy;
    this.chooseDecoderSources;
    this.createSumBus;

  }

  /* Public Methods */

  getEncoderProxy{
    ^NodeProxy(Server.default, 'audio', encoderChannels);
  }

  getEncoderClass{
    case
    {library =='AmbIEM' && (type == 'binaural')} {
      if (doppler == true,
        {^SpacePolarAmbIEMDp},
        {^SpacePolarAmbIEM}
      );
    }
    {library =='ATK' && (type == 'newListen')} {
      if (doppler == true,
        {^SpacePolarATKDp},
        {^SpacePolarATK}
      );
    }
    {library =='ATK' && (type == 'newStereo')} {
      if (doppler == true,
        {^SpacePolarATKDp},
        {^SpacePolarATK}
      );
    };
  }

  clear{
    Routine{
      decoderProxy.source = nil;
      1.wait;
      kernel.free;
      active = false;
      instance = nil;
    }.play;
  }

  /* Private Methods */

  calculateOrderAndNumOfChannels{
    case
    //binaural
    {library =='AmbIEM' && (type == 'binaural')} {decoderChannels = 2; order = 3}
    {library =='ATK' && (type == 'newListen')} {decoderChannels = 2; order = 1}
    //stereo
    {library =='ATK' && (type == 'newStereo')} {decoderChannels = 2; order = 1};
  }

  calculateEncoderChannels{
    switch (order)
    {1}{ encoderChannels = 4 }
    {2}{ encoderChannels = 9 }
    {3}{ encoderChannels = 16};
  }

  createDecoderProxy{
    if  (decoderProxy.numChannels != decoderChannels)
        {decoderProxy = NodeProxy(Server.default, 'audio', decoderChannels).fadeTime_(0.5)};
  }

  addDecoderSource{ arg decoderClass, kernel;
      Routine{
        1.wait;
        decoderProxy.source = {
          var in, out;
          in = \in.ar(0!encoderChannels);
          if (decoderClass == BinAmbi3O,
            { out = decoderClass.ar(in); },
            { out = decoderClass.ar(in, kernel); }
          );
          Out.ar(0, out);
        };
        this.readyMsg;
      }.play;
  }

  chooseDecoderSources{
    case
    {library =='AmbIEM' && (type == 'binaural')} {
      BinAmbi3O.init('1_4_7_4');
      this.addDecoderSource(BinAmbi3O);
    }
    {library =='ATK' && (type == 'newStereo')} {
      kernel = FoaDecoderMatrix.newStereo(131/2 * pi/180, 0.5); // Cardioids at 131 deg
      this.addDecoderSource(FoaDecode, kernel);
    }
    //check here for auto choosing correct decoder: chttp://www.ambisonictoolkit.net/Help/Guides/Intro-to-the-ATK.html
    {library =='ATK' && (type == 'newListen')} {
      kernel = FoaDecoderKernel.newListen(1013);
      this.addDecoderSource(FoaDecode, kernel);
    };
  }

  createSumBus{
    // create the summing NodeProxy that will act as the summation bus
    // see http://new-supercollider-mailing-lists-forums-use-these.2681727.n2.nabble.com/Many-to-One-Audio-Routing-in-Jitlib-td7594874.html
    summingProxy = NodeProxy(Server.default, 'audio', encoderChannels);
    //route the summation bus to the decoder
    summingProxy <>> decoderProxy;
  }

  readyMsg{
    "A decoder was created through GameLoopDecoder".postln;
  }

}
