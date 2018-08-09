package com.tao.fab.sim.event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Log
 */	
public class Log implements Serializable {

	private static List<String> filters=new ArrayList<String>();
	/**
     * Default constructor
     */
    public Log() {
    	
    }
    
    public static void addFilter(String f) {
    	filters.add(f);
    }
    
    public static void clearFilters() {
    	filters.clear();
    }
    
    public static void i(String tag,Object info,double time) {
    	
    	/*if(tag.equalsIgnoreCase("Batching")){
    		return;
    	}
    	if(tag.equalsIgnoreCase("Tool")&&!info.contains("16221_IMP_MC_1+2_1")){
    		return;
    	}
    	if(tag.equalsIgnoreCase("ResourcePool")&&!info.contains("16221_IMP_MC_1+2_1")){
    		return;
    	}*/


    	print("INFO",tag,info,time);
    }
    public static void i(String tag,Object info) {
    	print("INFO",tag,info);
    }
    public static void d(String tag,Object info) {
    	print("DEBUG",tag,info);
    }
    public static void e(String tag,Object info) {
    	print("ERROR",tag,info);
    }
    
    public static void d(String tag,Object info,double time) {
    	print("DEGUG",tag,info,time);
    }
    
    public static void e(String tag,Object info,double time) {
    	print("ERROR",tag,info,time);
    }
    

    private static void print(String type,String tag,Object info) {
    	print(type,tag,info,-1);
    }
    
    private static void print(String type,String tag,Object info,double time) {
    	
    	//System.out.println(info);
    	if(tag==null) {
    		tag="null";
    	}
    	if(info==null) {
    		info="null";
    	}
    	boolean output=false;
    	
    	if(type.equals("ERROR")) {
    		output=true;
    	}else {
    		if(filters.isEmpty()) {
	        	output=true;
        	}else {
        		String temp=time+type+tag+info;
	        	for(String filter:filters) {
	    	    	if(temp.toLowerCase().contains(filter.toLowerCase())){
	    	    		output=true;
	    	    		break;
	    	    	}
	        	}
        	}
    	}
    	if(!output) {
    		return;
    	}
    	
    	
    	String infoStr=info+"";
    	
    	String strs[]=infoStr.split("\r\n");
    	String before=String.format("%10.2f",time)+" "+type+": "+tag+"--";

    	for(String str:strs) {
    		String out=before+str;
    		System.out.println(out);
    		
    	}
    	if(type.equals("ERROR")){
    		assert false ;
    	}
    	
    	
    }

	@Override
	public String toString() {
		return super.toString();
	}

	/**
	 * This number is here for model snapshot storing purpose<br>
	 * It needs to be changed when this class gets changed
	 */ 
	private static final long serialVersionUID = 1L;
	public static void w(String tag, String info) {
		print("Warning",tag,info);
		
	}

} 
