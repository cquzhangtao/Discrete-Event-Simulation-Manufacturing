package enterprise.materialflow.model.product.activity.condition;

import simulation.model.activity.IActivity;
import simulation.model.activity.IActivityStartCondition;

public class PrecedenceConstraint implements IActivityStartCondition{

	@Override
	public boolean isSatisfied(IActivity activity) {
		boolean stisfied=true;
		for(IActivity act:activity.getPredecessors()){
			stisfied=stisfied&&act.isFinished();
		}
		return stisfied;
	}

	
}
