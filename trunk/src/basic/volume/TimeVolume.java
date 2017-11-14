package basic.volume;

import basic.distribution.Distribution;
import basic.unit.TimeUnitEnum;

public class TimeVolume extends RandomVolume<TimeUnitEnum> {

	public TimeVolume(Distribution dis,TimeUnitEnum unit) {
		super(dis);
		this.setUnit(TimeUnitEnum.Hour);
	}
	
	public TimeVolume(double time,TimeUnitEnum unit){
		//super();
		this.setUnit(TimeUnitEnum.Millisecond);
		this.setValue(unit.convert(time, TimeUnitEnum.Millisecond));
		//this.setDistribution(Distribution.Constant);
	}
	
	public long getMilliSeconds(){
		if(getUnit()==TimeUnitEnum.Millisecond){
			return getValue().longValue();
		}
		return getUnit().convert(getValue(),TimeUnitEnum.Millisecond).longValue();
	}


}
