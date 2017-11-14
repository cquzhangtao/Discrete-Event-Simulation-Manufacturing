package enterprise.materialflow.optimization.ga;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import enterprise.materialflow.model.plant.resource.IResource;

public class ReproductionUtilities {
	
	public static List<Individual> select(Population pop){
		Random rnd=new Random(9);
		double sum=0;
		List<Double> roulette=new ArrayList<Double>();
		for(Individual ind:pop.getIndividuals()){
			sum+=1/ind.getFitness();
			if(roulette.isEmpty()){
				roulette.add(1/ind.getFitness());
				continue;
			}
			roulette.add(roulette.get(roulette.size()-1)+1/ind.getFitness());
		}
		List<Individual> newIndividuals=new  ArrayList<Individual>();
		Individual best = pop.getBetsIndividual().clone();
		best.setBest(true);
		newIndividuals.add(best);
		for(int i=1;i<pop.getIndividuals().size();i++){
			double rv=rnd.nextDouble()*sum;
			for(Double d:roulette){
				if(d>rv){
					newIndividuals.add(pop.getIndividuals().get(roulette.indexOf(d)).clone());
					break;
				}
			}
		}		
		return newIndividuals;
	}
	
	public static void crossover(List<Individual> individuals){
		
	}

	public static void mutate(List<Individual> individuals){
		Random rnd=new Random();
		for(Individual ind:individuals){
			if(ind.isBest()){
				continue;
			}
			mutateAllocation(ind);
			ind.decode();
			while(!SchedulingUtilities.randomSequencing(ind.getActivities(),rnd.nextInt())){
				//System.out.println("Reallocated");
				mutateAllocation(ind);
				SchedulingUtilities.removeDisjunctiveArcs(ind.getActivities());
			} 
			SchedulingUtilities.removeDisjunctiveArcs(ind.getActivities());
			ind.encode();
		}
		
	}
	
	private static void mutateAllocation(Individual ind){
		Random rnd=new Random();
		double mutateProb=0.01;
		for(int i=0;i<ind.getActivities().size();i++){
			if(rnd.nextDouble()<mutateProb){
				ActivityEx act=ind.getActivities().get(i);
				List<IResource> ress = act.getActivity().getResourceRequirement().getAlternativeResources();
				IResource res=ress.get(rnd.nextInt(ress.size()));
				ind.getAllocation().set(i, ind.getResources().indexOf(res));
				
			}
		}
		
	}
	



}
