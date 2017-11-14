package enterprise.materialflow.model.meta;

import common.Entity;

import basic.unit.IUnit;
import basic.unit.UnitSystem;

public class Property extends Entity{
	private static int count=0;
	private IUnit unit;
	private UnitSystem unitSystem;
	public Property(UnitSystem unitSystem){
		super();
		setName("Property"+(++count));
		this.setUnitSystem(unitSystem);
		this.setUnit(unitSystem.getDefaultUnit());
	}
	public void setUnit(IUnit unit) {
		this.unit = unit;
	}
	public IUnit getUnit() {
		return unit;
	}
	public void setUnitSystem(UnitSystem unitSystem) {
		this.unitSystem = unitSystem;
		this.setUnit(unitSystem.getDefaultUnit());
	}
	public UnitSystem getUnitSystem() {
		return unitSystem;
	}

}
