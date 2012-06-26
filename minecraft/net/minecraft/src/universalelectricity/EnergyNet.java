package net.minecraft.src.universalelectricity;

import java.util.*;
import net.minecraft.src.*;
import net.minecraft.src.forge.*;

class EnergyNet{
  ArrayList<UETileEntityConductor> conductors;
  ArrayList<UEIConsumer> targets = new ArrayList<UEIConsumer>();
  ArrayList<UEIProducer> inputs = new ArrayList<UEIProducer>();
  ArrayList<UEIConsumer> runningTargets = new ArrayList<UEIConsumer>();
  public boolean canEnergyFlow = false;
  public EnergyNet(UETileEntityConductor initial){
      conductors = new ArrayList<UETileEntityConductor>();
  	conductors.add(initial);
  }
  public void addConsumer(UEIConsumer consumer){
    targets.add(consumer);
  }
  public void addProducer(UEIProducer producer){
    inputs.add(producer);
  }
  public void updateEnergyNet(){
    for(UEIConsumer machine : targets){
        if(machine.needsPower){
            runningTargets.add(machine);
        }else{
            try{
                runningTargets.remove(machine);
            }catch(Exception e){}
            if(runningTargets.length == 0) this.canEnergyFlow == false;
        }
  }         
}