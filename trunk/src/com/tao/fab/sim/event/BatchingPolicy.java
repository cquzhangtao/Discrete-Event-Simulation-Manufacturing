package com.tao.fab.sim.event;

/**
 * BatchingPolicy
 */	
public enum BatchingPolicy{
	FORCEFULL, NEARFULL;
	
	public static BatchingPolicy fromString(String rule){
		   rule=rule.toUpperCase();
		   return BatchingPolicy.valueOf(rule);
		  
	   }
	   
   public static BatchingPolicy fromIndex(int index){
	   return BatchingPolicy.values()[index];
	   
   }
   public static int toIndex(BatchingPolicy rule){
	   int index=0;
	   for(BatchingPolicy r:BatchingPolicy.values()){
		   if(r==rule){
			   return index;
		   }
		   index++;
	   }
	   return index;
   }

} 
