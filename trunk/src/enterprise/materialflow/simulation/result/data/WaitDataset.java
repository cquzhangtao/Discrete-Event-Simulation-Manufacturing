package enterprise.materialflow.simulation.result.data;

import java.io.Serializable;
import java.util.ArrayList;

public class WaitDataset implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1566768495213338359L;
	private String bufferName;
	private double maxWaitTime = 0;
	private double avgWaitTime = 0;
	private int interval = 7 * 24 * 3600;
	private ArrayList<WaitData> waitDataset = new ArrayList<WaitData>();
	private ArrayList<Double> waitDatasetbyDay = new ArrayList<Double>();

	private int waitDatasetSize;

	public WaitDataset(int period) {
		// TODO Auto-generated constructor stub
		interval = period;
	}

	public void save() {
		waitDatasetSize = waitDataset.size();
	}

	public void recover() {
		while (waitDataset.size() > waitDatasetSize)
			waitDataset.remove(waitDatasetSize);
	}

	public void add(String jobName, long endWaitTime, long waitTime) {
		waitDataset.add(new WaitData(jobName, endWaitTime, waitTime));
	}

	public void reset() {
		setMaxWaitTime(0);
		setAvgWaitTime(0);
		waitDataset.clear();
		getWaitDatasetbyDay().clear();
	}

	public void stat() {
		int count = 0;
		int time = interval;
		long sum = 0;
		for (int i = 0; i < waitDataset.size(); i++) {
			if (waitDataset.get(i).getEndWaitTime() < time) {
				count++;
				sum += waitDataset.get(i).getWaitTime();
			} else {
				if (count == 0)
					getWaitDatasetbyDay().add(0.0);
				else {
					getWaitDatasetbyDay().add(1.0 * sum / count / 3600);
					count = 0;
				}
				sum = 0;

				time += interval;
				i = i - 1;
			}
		}
		sum = 0;
		for (int i = 0; i < waitDataset.size(); i++) {
			if (waitDataset.get(i).getWaitTime() > getMaxWaitTime()) {
				setMaxWaitTime(waitDataset.get(i).getWaitTime());
			}
			sum += waitDataset.get(i).getWaitTime();
		}
		if (waitDataset.size() > 0)
			setAvgWaitTime(1.0 * sum / waitDataset.size() / 3600);
		else
			setAvgWaitTime(0);
		setMaxWaitTime(1.0 * getMaxWaitTime() / 3600);
		waitDataset.clear();
		// waitDataset=null;
	}

	public int size() {
		// TODO Auto-generated method stub
		return getWaitDatasetbyDay().size();
	}

	public double getAvgWaitTime() {
		return avgWaitTime;
	}

	public void setAvgWaitTime(double avgWaitTime) {
		this.avgWaitTime = avgWaitTime;
	}

	public double getMaxWaitTime() {
		return maxWaitTime;
	}

	public void setMaxWaitTime(double maxWaitTime) {
		this.maxWaitTime = maxWaitTime;
	}

	public String getBufferName() {
		return bufferName;
	}

	public void setBufferName(String bufferName) {
		this.bufferName = bufferName;
	}

	public ArrayList<Double> getWaitDatasetbyDay() {
		return waitDatasetbyDay;
	}

	public void setWaitDatasetbyDay(ArrayList<Double> waitDatasetbyDay) {
		this.waitDatasetbyDay = waitDatasetbyDay;
	}

}
