package enterprise.materialflow.optimization.ga;

import java.util.ArrayList;
import java.util.List;

import enterprise.materialflow.model.product.activity.IProcessActivity;
import enterprise.materialflow.optimization.model.IOptimizationModel;

public class PopulationUtilities {
	
	public static Population generateRandomPopulation(IOptimizationModel model, int popSize){
		List<Individual> individuals=new ArrayList<Individual>();
		for(int i=0;i<popSize;i++){
			individuals.add(generateIndividual(model));
		}
		Population pop=new Population();
		pop.setIndividuals(individuals);
		pop.setModel(model);
		return pop;
	}
	
	public static Individual generateIndividual(IOptimizationModel model){
		Individual ind=new Individual();
		ind.setResources(model.getResources());
		List<ActivityEx> acts=new ArrayList<ActivityEx>();
		for(IProcessActivity act:model.getActivities()){
			acts.add(new ActivityEx(act));
		}
		SchedulingUtilities.randomScheduling(acts);
		ind.setActivities(acts);
		ind.encode();		
		return ind;
		
	}
	

}
