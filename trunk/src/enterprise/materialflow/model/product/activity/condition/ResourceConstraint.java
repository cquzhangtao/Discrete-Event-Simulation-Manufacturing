package enterprise.materialflow.model.product.activity.condition;

import enterprise.materialflow.model.product.activity.ProcessActivity;
import simulation.model.activity.IActivity;
import simulation.model.activity.IActivityStartCondition;

public class ResourceConstraint implements IActivityStartCondition{

	@Override
	public boolean isSatisfied(IActivity activity) {
		ProcessActivity pactivity=(ProcessActivity) activity;
		return pactivity.getResourceSelector().isSelectable(pactivity,pactivity.getAlternativeResources());
	}

}
