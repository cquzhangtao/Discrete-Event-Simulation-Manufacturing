package enterprise.materialflow.optimization.ga;

import enterprise.materialflow.optimization.model.OptimizationType;

public class GeneticAlgorithm {

	private int generationNum;
	private int populationSize;
	private double crossoverRatio;
	private double mutationRatio;
	private Population population;
	private Individual betsIndividual;

	public void run() {

		for (int i = 0; i < generationNum; i++) {
			population.evaluate();
			if(betsIndividual==null){
				betsIndividual=population.getBetsIndividual();
			}
			else if(population.getModel().getOptimizationType()==OptimizationType.max&&
					population.getBetsIndividual().getFitness()>betsIndividual.getFitness()){
				betsIndividual=population.getBetsIndividual();
			}
			else if(population.getModel().getOptimizationType()==OptimizationType.min&&
					population.getBetsIndividual().getFitness()<betsIndividual.getFitness()){
				betsIndividual=population.getBetsIndividual();
			}
			System.out.println(i+","+betsIndividual.getFitness()+","+population.getBetsIndividual().getFitness());
			population = population.reproduce();			
		}
		betsIndividual.decode();
		Evaluation.evaluate(population.getModel(), betsIndividual);
		
	}

	public int getGenerationNum() {
		return generationNum;
	}

	public void setGenerationNum(int generationNum) {
		this.generationNum = generationNum;
	}

	public double getCrossoverRatio() {
		return crossoverRatio;
	}

	public void setCrossoverRatio(double crossoverRatio) {
		this.crossoverRatio = crossoverRatio;
	}

	public double getMutationRatio() {
		return mutationRatio;
	}

	public void setMutationRatio(double mutationRatio) {
		this.mutationRatio = mutationRatio;
	}

	public Individual getBetsIndividual() {
		return betsIndividual;
	}

	public void setBetsIndividual(Individual betsIndividual) {
		this.betsIndividual = betsIndividual;
	}

	public int getPopulationSize() {
		return populationSize;
	}

	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}
	
	public void setPopulation(Population pop){
		population=pop;
	}

}
