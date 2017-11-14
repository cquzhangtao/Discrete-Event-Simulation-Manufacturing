package basic.distribution;

import java.io.Serializable;

public class DistributionParameter implements Serializable{
 private DistributionParameterEnum name=DistributionParameterEnum.Min;
 private double value=0;
public DistributionParameter(DistributionParameterEnum paraName, Double value2) {
	this.name=paraName;
	this.value=value2;
}
public DistributionParameter() {
	// TODO Auto-generated constructor stub
}
public DistributionParameterEnum getName() {
	return name;
}
public double getValue() {
	return value;
}
public void setName(DistributionParameterEnum name) {
	this.name = name;
}
public void setValue(double value) {
	this.value = value;
}

 
}
