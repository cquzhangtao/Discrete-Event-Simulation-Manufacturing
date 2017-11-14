package basic.volume;

import java.io.Serializable;

import basic.unit.IUnit;

public class Volume <T extends IUnit>implements Serializable{
	private T unit;
	private Number value;
	
	public Volume(){
		
	}
	public T getUnit() {
		return unit;
	}
	public Number getValue() {
		return value;
	}
	public void setUnit(T unit) {
		this.unit = unit;
	}
	public void setValue(Number value) {
		this.value = value;
	}
}
