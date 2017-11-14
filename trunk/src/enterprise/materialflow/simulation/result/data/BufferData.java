package enterprise.materialflow.simulation.result.data;

import java.io.Serializable;
import java.util.ArrayList;

public class BufferData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8809002582114769983L;
	private String bufferName;
	private ArrayList<Long> changeTime = new ArrayList<Long>();
	private ArrayList<Integer> bufferSize = new ArrayList<Integer>();
	private int maxBufferSize;
	private double avgBufferSize;
	private ArrayList<Double> bufferSizebyDay = new ArrayList<Double>();
	private int changeTimeSize;
	private int bufferSizeSize;
	private int interval = 7*24 * 3600;
	
	public BufferData(String name){
		setBufferName(name);
	}
	
	public void save(){
		changeTimeSize=changeTime.size();
		bufferSizeSize=bufferSize.size();
	}
	public void recover(){
		while(changeTime.size()>changeTimeSize)
			changeTime.remove(changeTimeSize);
		while(bufferSize.size()>bufferSizeSize)
			bufferSize.remove(bufferSizeSize);
	}
	
	public void reset(){
		changeTime.clear();
		bufferSize.clear();
		getBufferSizebyDay().clear();
		setMaxBufferSize(0);
		setAvgBufferSize(0);
	}
	
	public void add(long time, int size) {
		if(changeTime.size()==0)
		{
			changeTime.add((long) 0);
			bufferSize.add(0);
			changeTime.add(time);
			bufferSize.add(size);
			return;
		}
		
		if (changeTime.get(changeTime.size() - 1) == time) {
			changeTime.remove(changeTime.size() - 1);
			bufferSize.remove(bufferSize.size() - 1);
		}
		changeTime.add(time);
		bufferSize.add(size);
	}

	public void stat() {

		int index=0;
		int beginIndex=0;
		if(changeTime.size()>0){
		for(int i=interval;i<changeTime.get(changeTime.size()-1);i=i+interval)
		{
			long sum=0;
			while(changeTime.get(index)<i){
				sum+=bufferSize.get(index)*(changeTime.get(index+1)-changeTime.get(index));
				index++;
			}
			if(beginIndex!=0)
				sum+=(changeTime.get(beginIndex)-i+interval)*bufferSize.get(beginIndex-1);
			sum-=(changeTime.get(index)-i)*bufferSize.get(index-1);
			getBufferSizebyDay().add(1.0*sum/interval);
			beginIndex=index;
		}
		
		long sum=0;
		for (int i = 1; i < bufferSize.size(); i++) {
			if (bufferSize.get(i) > getMaxBufferSize()) {
				setMaxBufferSize(bufferSize.get(i));
			}
			sum += bufferSize.get(i - 1)
					* (changeTime.get(i) - changeTime.get(i - 1));
		
		}
		long period=changeTime.get(changeTime.size() - 1);
		if(period==0)
			setAvgBufferSize(0);
		else
			setAvgBufferSize(1.0 * sum /period) ;
		}
		changeTime.clear();
		bufferSize.clear();
		
	}

	public double getAvgBufferSize() {
		return avgBufferSize;
	}

	public void setAvgBufferSize(double avgBufferSize) {
		this.avgBufferSize = avgBufferSize;
	}

	public int getMaxBufferSize() {
		return maxBufferSize;
	}

	public void setMaxBufferSize(int maxBufferSize) {
		this.maxBufferSize = maxBufferSize;
	}

	public ArrayList<Double> getBufferSizebyDay() {
		return bufferSizebyDay;
	}

	public void setBufferSizebyDay(ArrayList<Double> bufferSizebyDay) {
		this.bufferSizebyDay = bufferSizebyDay;
	}

	public String getBufferName() {
		return bufferName;
	}

	public void setBufferName(String bufferName) {
		this.bufferName = bufferName;
	}


}
