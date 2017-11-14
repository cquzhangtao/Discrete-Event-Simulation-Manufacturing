package simulation.model.activity;

import java.io.Serializable;


public interface ActivityListener extends Serializable{
	public void onEnd(IActivity activity,long time);
	public void onStart(IActivity activity,long time);
}
