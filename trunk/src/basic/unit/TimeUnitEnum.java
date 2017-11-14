package basic.unit;


public enum TimeUnitEnum implements IUnit{
	Year,Day,Month,Week,Hour,Minute,Second,Millisecond;

	@Override
	public Number convert(Number number,  IUnit toUnit) {
		// TODO Auto-generated method stub
		if(this==TimeUnitEnum.Millisecond){
			if(toUnit==TimeUnitEnum.Millisecond){
				return number.doubleValue();
			}
		}
		if(this==TimeUnitEnum.Second){
			if(toUnit==TimeUnitEnum.Millisecond){
				return number.doubleValue()*1000;
			}
		}
		if(this==TimeUnitEnum.Minute){
			if(toUnit==TimeUnitEnum.Millisecond){
				return number.doubleValue()*1000*60;
			}
		}
		if(this==TimeUnitEnum.Hour){
			if(toUnit==TimeUnitEnum.Millisecond){
				return number.doubleValue()*60*60*1000;
			}
		}
		if(this==TimeUnitEnum.Day){
			if(toUnit==TimeUnitEnum.Millisecond){
				return number.doubleValue()*1000*60*60*24;
			}
		}
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
		return UnitSystem.Time;
	}

}
