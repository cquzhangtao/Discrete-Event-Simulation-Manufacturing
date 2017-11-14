package enterprise.materialflow.model.plant.resource.setup;

import java.io.Serializable;

import common.IEntity;
import simulation.model.activity.IActivity;

public interface ISetupTime extends IEntity{

	public long getTime (IActivity preActivity, IActivity curActivity);

}
