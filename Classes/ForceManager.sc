
ForceManager { var <>forces;

  *new{ arg forces = [];
    forces = Dictionary.newFrom(forces);
    ^super.newCopyArgs(forces);
  }

  add{ arg key, forceFunction;
    key ?? {key = ("Force" ++ forces.size).asSymbol};
    forces.add(key -> forceFunction);
  }

  addTemp{ arg force;
    forces.add('tempForce' -> force);
  }

  clearTemp{
    forces.removeAt('tempForce');
  }

  remove{ arg key;
    forces.removeAt(key);
  }

  get{ arg key;
    ^forces.atFail(key, {0});
  }

  list{
    "\n=============================================".postln;

    ( "| There are" + forces.size.asString + "forces in the manager:" ).postln;
    forces.keysValuesDo{ arg key, value;
      ( "|     " + key + " = " + value.value.asString).postln;
    };

    "============================================\n".postln;
  }

  sum{ arg entity,  addedForce = 0; var sum;
    sum = addedForce;
    forces.do{arg value;
      sum = sum + value.value(entity);
    };
    this.clearTemp;
    ^sum;
  }

}
