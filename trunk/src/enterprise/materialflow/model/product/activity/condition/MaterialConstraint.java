package enterprise.materialflow.model.product.activity.condition;

import enterprise.materialflow.model.product.activity.IMaterialInvolvedActivity;
import simulation.model.activity.IActivity;
import simulation.model.activity.IActivityStartCondition;

public class MaterialConstraint implements IActivityStartCondition{

	@Override
	public boolean isSatisfied(IActivity activity) {
		IMaterialInvolvedActivity mactivity = (IMaterialInvolvedActivity)activity;
		return mactivity.hasEnoughMaterial();
	}

}
