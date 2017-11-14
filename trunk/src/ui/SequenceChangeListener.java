package ui;

import simulation.model.activity.IActivity;



public interface SequenceChangeListener{
	public void sequenceChanged(IActivity activity,int newpos);
}