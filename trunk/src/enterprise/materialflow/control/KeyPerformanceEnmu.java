package enterprise.materialflow.control;

public enum KeyPerformanceEnmu {
	averageCT,averageWIP,makespan,tardiness,lateness;
	
	public static boolean isBetter(double d1,double d2,KeyPerformanceEnmu type){
		if(type==KeyPerformanceEnmu.averageCT){
			return d1<d2;
		}else if(type==KeyPerformanceEnmu.averageWIP){
			return d1<d2;
		}else if(type==KeyPerformanceEnmu.makespan){
			return d1<d2;
		}
		
		//TODO please finish it
		return false;
		
	}
}
