package com.tao.fab.sim.event;

public enum JobType {
	Lot,Unit,BatchLot,BatchUnit;

	public boolean isCollectionOf(JobType type) {
		if(this==Lot&&type==Unit){
			return true;
		}else if(this==BatchLot&&type==Lot){
			return true;
		}
		else if(this==BatchUnit&&type==Unit){
			return true;
		}
		return false;
	}

	public JobType getChildType() {
		if(this==Lot){
			return Unit;
		}else if(this==Unit){
			return null;
		}else if(this==BatchLot){
			return Lot;
		}
		else if(this==BatchUnit){
			return Unit;
		}
		return null;
	}

	public boolean isOriginalType() {
		if(this==Lot){
			return true;
		}
		return false;
	}

	public Class<Job> getJobClass() {

		return Job.class;
	}

}
