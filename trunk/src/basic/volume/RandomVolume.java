package basic.volume;

import basic.distribution.Distribution;
import basic.distribution.DistributionParameterEnum;
import basic.distribution.RandomGenerator;
import basic.unit.IUnit;

public class RandomVolume<T extends IUnit> extends Volume<T>{
	private RandomGenerator randomGenerator;
		
	public RandomVolume(Distribution dis){
		super();
		randomGenerator=new RandomGenerator(dis);
		
		
	}
	public RandomVolume(){
		
	}
	
	public String getString(){
		return randomGenerator.getString()+" Unit: "+getUnit().getName();
	}
	
	public String getShortString(){
		return randomGenerator.getShortString();
	}
	@Override
	public Number getValue(){
		if(randomGenerator==null){
			return super.getValue();
		}
		return randomGenerator.nextDouble();
//		Class<?> c=getUnitValueClass(getUnit());
//		if(c==Long.class)
//			return randomGenerator.nextLong();
//		else if(c==Double.class)
//			return randomGenerator.nextDouble();
//		else if(c==Integer.class)
//			return randomGenerator.nextInt();
//		return null;
	}

//	@Override
//	public void setValue(Number v){
//		//do nothing
//	}
	
	public void setDistribution(Distribution distribution){
		randomGenerator.setDistribution(distribution);
	}
	public void setDistributionParameter(DistributionParameterEnum paraName, double value){
		randomGenerator.setParameter(paraName, value);
	}



	public void setRandomGenerator(RandomGenerator randomGenerator) {
		this.randomGenerator = randomGenerator;
	}
	
//	private Class<?> getUnitValueClass(T unit){
//		return Double.class;
//		if(equalTo(unit,UnitEnum.box))
//			return Integer.class;
//		else if(equalTo(unit,UnitEnum.m))
//			return Double.class;
//		else if(equalTo(unit,UnitEnum.second))
//			return Long.class;
//		else if(equalTo(unit,UnitEnum.minute))
//			return Long.class;
//		else if(equalTo(unit,UnitEnum.day))
//			return Long.class;
//		
//		return null;
			
//	}
//	private boolean equalTo(T unit,UnitEnum u){
//		return unit.getName().equalsIgnoreCase(u.name());
//	}
}
