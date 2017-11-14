package enterprise.materialflow.simulation.result.data;

import java.io.Serializable;

public class StartEnd implements Serializable{
	private double start;
	private double end;
	private String others;
	
	public double getStart() {
		return start;
	}
	public void setStart(double start) {
		this.start = start;
	}
	public double getEnd() {
		return end;
	}
	public void setEnd(double end) {
		this.end = end;
	}
	public String getOthers() {
		return others;
	}
	public void setOthers(String others) {
		this.others = others;
	}
	public boolean startEqualsToEnd(){
		return start==end;
	}
	public StartEnd(double start,double end){
		this.start=start;
		this.end=end;
	}
	public StartEnd(double start,double end,String others){
		this(start,end);
		this.others=others;
	}
}