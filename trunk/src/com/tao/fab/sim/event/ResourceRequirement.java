package com.tao.fab.sim.event;

import java.util.ArrayList;

public class ResourceRequirement extends ArrayList<MultipuleResourceRequirement>{
	
	
	public MultipuleResourceRequirement selecteResourceRequirement(IJob job,IStep step){
		return get(0);
	}

}
