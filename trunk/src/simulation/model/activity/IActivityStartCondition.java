package simulation.model.activity;

import java.io.Serializable;

public interface IActivityStartCondition extends Serializable{
	public boolean isSatisfied(IActivity activity);
}
