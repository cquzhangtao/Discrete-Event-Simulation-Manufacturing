package basic.unit;

public enum UnitSystem implements IUnitSystem {
	Length, Time, Weight, Area, Volume, Quautity,Money;

	@Override
	public IUnit getDefaultUnit() {
		if (this == Length) {
			return LengthUnitEnum.m;

		} else if (this == Time) {
			return TimeUnitEnum.Hour;
		} else if (this == Weight) {
			return WeightUnitEnum.kg;
		} else if (this == Quautity) {
			return QuatityUnitEnum.Unit;
		}
		return null;
	}
}
