package enterprise.materialflow.simulation.result;

import common.IEntity;

import enterprise.materialflow.control.KeyPerformanceEnmu;
import enterprise.materialflow.simulation.result.data.ProductDataset;

public interface ISimulationResult extends IEntity{

	double getKPI(KeyPerformanceEnmu averagect);
}
