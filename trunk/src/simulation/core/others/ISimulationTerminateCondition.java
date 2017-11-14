package simulation.core.others;

import java.io.Serializable;

import common.Clonable;

public interface ISimulationTerminateCondition extends Serializable,Clonable<ISimulationTerminateCondition>{
	public boolean isSatisfied(long time);

}
