package com.tao.fab.sim.event;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import common.Entity;




/**
 * BatchingConfiguration
 */	
public class BatchingConfiguration extends Entity implements Serializable {
	
	private int minSize=12;
	private int maxSize=96;
	private Set<Step> suitableSteps=new HashSet<Step>();
	private BatchingPolicy batchingPolicy=BatchingPolicy.NEARFULL;
	private double maximalBatchingTime=5*24*3600;
    private boolean enableMaxBatchingTime=true;
	/**
     * Default constructor
     */
    public BatchingConfiguration() {
    	
    }
    
    public boolean isEnableMaxBatchingTime(){
    	return enableMaxBatchingTime;
    }
    
    public void setEnableMaxBatchingTime(boolean enable){
    	enableMaxBatchingTime=enable;
    }
    
    public BatchingPolicy getBatchingPolicy(){
    	return batchingPolicy;
    }
    
    public void setBatchingPolicy(BatchingPolicy batchingPolicy){
    	this.batchingPolicy=batchingPolicy;
    }
    
    public double getMaximalBatchingTime(){
    	return maximalBatchingTime;
    }
    
    public void setMaximalBatchingTime(double maximalBatchingTime){
    	this.maximalBatchingTime=maximalBatchingTime;
    }
    
    public int getMinSize(){
    	return minSize;
    }
    
    public int getMaxSize(){
    	return maxSize;
    }
    
    public void setMinSize(int minSize){
    	this.minSize=minSize;
    }
    
    public void setMaxSize(int maxSize){
    	this.maxSize=maxSize;
    }
    
    public Set<Step> getSuitableSteps(){
    	return suitableSteps;
    }
    
    public void addSuitableSteps(Step step){
    	suitableSteps.add(step);
    }

	@Override
	public String toString() {
		return "Min:"+minSize+", Max:"+maxSize;
	}

	/**
	 * This number is here for model snapshot storing purpose<br>
	 * It needs to be changed when this class gets changed
	 */ 
	private static final long serialVersionUID = 1L;

} 
