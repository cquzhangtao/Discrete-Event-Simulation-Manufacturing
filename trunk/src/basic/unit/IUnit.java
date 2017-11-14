package basic.unit;

import java.io.Serializable;




public interface IUnit extends Serializable{
	public Number convert(Number number,IUnit toUnit);
	public String getName();
	public Object[] getAllUnits();
	public UnitSystem getType();
}
