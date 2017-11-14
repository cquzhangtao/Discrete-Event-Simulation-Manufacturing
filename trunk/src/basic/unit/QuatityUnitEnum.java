package basic.unit;


public enum QuatityUnitEnum implements IUnit{
	Lot,Box,Tin,Batch,Can,Piece,Unit;

	@Override
	public Number convert(Number number,  IUnit toUnit) {
		// TODO Auto-generated method stub
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
		return UnitSystem.Quautity;
	}

}
