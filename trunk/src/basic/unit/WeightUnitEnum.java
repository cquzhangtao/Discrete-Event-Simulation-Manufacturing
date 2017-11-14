package basic.unit;


public enum WeightUnitEnum implements IUnit{
	ton,kg,g;

	@Override
	public Number convert(Number number,  IUnit toUnit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return this.name();
	}

	@Override
	public Object[] getAllUnits() {
		Object[] possibleValues = this.getDeclaringClass().getEnumConstants();
		return possibleValues;
	}

	@Override
	public UnitSystem getType() {
		return UnitSystem.Weight;
	}

}
