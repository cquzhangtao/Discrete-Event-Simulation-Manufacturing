package enterprise.materialflow.model.product.activity.processtime;


import java.io.Serializable;

public enum ProcessingTimeLogic implements Serializable{
	Resources_Independent,Resource_Dependent,Amount_Dependent,Proportion_Dependent
	
	//,Resources_Independent,Resource_Dependent,Resource_Amount_Dependent,Resource_Amount_Direct_Proportion_Dependent
}
