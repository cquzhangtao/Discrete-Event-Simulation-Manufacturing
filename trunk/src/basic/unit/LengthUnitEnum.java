package basic.unit;


public enum LengthUnitEnum implements IUnit{
	km,m,dm,cm,mm;

	@Override
	public Number convert(Number number,  IUnit toUnit) {
		// TODO Auto-generated method stub
		if(this==LengthUnitEnum.cm){
			
		}
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.name();
	}

	@Override
	public Object[] getAllUnits() {
		Object[] possibleValues = this.getDeclaringClass().getEnumConstants();
		return possibleValues;
	}

	@Override
	public UnitSystem getType() {
		return UnitSystem.Length;
	}

}
