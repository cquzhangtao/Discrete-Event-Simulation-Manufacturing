package com.tao.fab.sim.event;

public enum ProcessType  {

    Unit,Lot,Batch;
 
    
    public static ProcessType valueOf(int index){
 	   return ProcessType.values()[index];
 	   
    }
    public static int indexOf(ProcessType rule){
 	   int index=0;
 	   for(ProcessType r:ProcessType.values()){
 		   if(r==rule){
 			   return index;
 		   }
 		   index++;
 	   }
 	   return index;
    }

} 
