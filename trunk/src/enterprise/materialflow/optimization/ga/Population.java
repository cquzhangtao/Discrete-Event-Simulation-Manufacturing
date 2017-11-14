package enterprise.materialflow.optimization.ga;

import java.util.List;

import enterprise.materialflow.optimization.model.IOptimizationModel;
import enterprise.materialflow.optimization.model.OptimizationType;

public class Population {
	
	private Individual betsIndividual;
	private List<Individual> individuals;
	private IOptimizationModel model;
	
	public Population reproduce(){
		List<Individual> newindividuals=ReproductionUtilities.select(this);
		ReproductionUtilities.crossover(newindividuals);
		ReproductionUtilities.mutate(newindividuals);
		Population pop=new Population();
		pop.setIndividuals(newindividuals);	
		pop.setModel(model);
		return pop;
	}
	
	public void evaluate(){
		for(Individual ind:individuals){
			Evaluation.evaluate(model,ind);
			if(betsIndividual==null){
				betsIndividual=ind;
			}
			else if(model.getOptimizationType()==OptimizationType.max&&
					ind.getFitness()>betsIndividual.getFitness()){
				betsIndividual=ind;
			}
			else if(model.getOptimizationType()==OptimizationType.min&&
					ind.getFitness()<betsIndividual.getFitness()){
				betsIndividual=ind;
			}
		}
	}

	public Individual getBetsIndividual() {
		return betsIndividual;
	}

	public void setBetsIndividual(Individual betsIndividual) {
		this.betsIndividual = betsIndividual;
	}

	public List<Individual> getIndividuals() {
		return individuals;
	}

	public void setIndividuals(List<Individual> individuals) {
		this.individuals = individuals;
	}

	public IOptimizationModel getModel() {
		return model;
	}

	public void setModel(IOptimizationModel model) {
		this.model = model;
	}
}
