package enterprise.materialflow.model.plant.resource.setup;

import common.Entity;
import simulation.model.activity.IActivity;

public class NonSetupTime extends Entity implements ISetupTime{

	@Override
	public long getTime(IActivity preActivity, IActivity curActivity) {
		return 0;
	}

}
