package enterprise.materialflow.simulation.result.data;

import java.io.Serializable;
import java.util.ArrayList;

public class ProductData implements Serializable {

	private String product;
	private ArrayList<Integer> wip = new ArrayList<Integer>();
	private ArrayList<Long> wipChangeTime = new ArrayList<Long>();
	private ArrayList<Double> cycleTime = new ArrayList<Double>();
	private ArrayList<Long> receivedTime = new ArrayList<Long>();
	private ArrayList<Long> releaseTime = new ArrayList<Long>();
	private long interval;
	private ArrayList<Double> wipInPeriod = new ArrayList<Double>();
	private ArrayList<Double> cycleTimeInPeriod = new ArrayList<Double>();
	private ArrayList<Integer> finishedLotNumInPeriod = new ArrayList<Integer>();
	private ArrayList<Integer> releaseLotNumInPeriod = new ArrayList<Integer>();
	private int finishedLotNum = 0;
	private int releasedLotNum = 0;
	private int maxWip = 0;
	private double avgWip;
	private double minCycleTime = Double.MAX_VALUE;
	private double maxCycleTime = 0;
	private double avgCycleTime;
	private double releaseRatio = 0;
	private double productivity = 0;
	private double rawProcessTime = 0;
	private long makespan;

	private int wipSize;
	private int wipChangeTimeSize;
	private int cycleTimeSize;
	private int receivedTimeSize;
	private int releaseTimeSize;

	public ProductData(String product) {
		this.product = product;
	}

	public void save() {
		wipSize = wip.size();
		wipChangeTimeSize = wipChangeTime.size();
		cycleTimeSize = cycleTime.size();
		receivedTimeSize = receivedTime.size();
		releaseTimeSize = releaseTime.size();
	}

	public void recover() {
		while (wip.size() > wipSize)
			wip.remove(wipSize);
		while (wipChangeTime.size() > wipChangeTimeSize)
			wipChangeTime.remove(wipChangeTimeSize);
		while (cycleTime.size() > cycleTimeSize)
			cycleTime.remove(cycleTimeSize);
		while (receivedTime.size() > receivedTimeSize)
			receivedTime.remove(receivedTimeSize);
		while (releaseTime.size() > releaseTimeSize)
			releaseTime.remove(releaseTimeSize);

	}

	public void reset() {
		wipInPeriod.clear();
		cycleTimeInPeriod.clear();
		finishedLotNumInPeriod.clear();
		releaseLotNumInPeriod.clear();
		wip.clear();
		wipChangeTime.clear();
		cycleTime.clear();
		receivedTime.clear();
		releaseTime.clear();
		setFinishedLotNum(0);
		setReleasedLotNum(0);
		setMaxWip(0);
		avgWip = 0;
		setMinCycleTime(Double.MAX_VALUE);
		setMaxCycleTime(0);
		setAvgCycleTime(0);
		setReleaseRatio(0);
		setProductivity(0);
		makespan=0;
	}

	public void addCycleTime(long time, double cycleTime) {
		this.cycleTime.add(cycleTime);
		receivedTime.add(time);

	}

	public void addReleaseTime(long time) {
		releaseTime.add(time);
	}

	public void addWIP(long time, int iwip) {
		if (wipChangeTime.size() == 0) {
			wipChangeTime.add((long) 0);
			wip.add(0);
			wipChangeTime.add(time);
			wip.add(iwip);
			return;
		}

		if (wipChangeTime.get(wipChangeTime.size() - 1) == time) {
			wipChangeTime.remove(wipChangeTime.size() - 1);
			wip.remove(wip.size() - 1);
		}
		wipChangeTime.add(time);
		wip.add(iwip);
	}

	public void stat() {

		int index = 0;
		int beginIndex = 0;
		if (wipChangeTime.size() == 0) {
			return;
		}
		for (long i = getInterval(); i < wipChangeTime
				.get(wipChangeTime.size() - 1); i = i + getInterval()) {
			long sum = 0;
			while (wipChangeTime.get(index) < i) {
				sum += wip.get(index)
						* (wipChangeTime.get(index + 1) - wipChangeTime
								.get(index));
				index++;
			}
			if (beginIndex != 0)
				sum += (wipChangeTime.get(beginIndex) - i + getInterval())
						* wip.get(beginIndex - 1);
			sum -= (wipChangeTime.get(index) - i) * wip.get(index - 1);
			wipInPeriod.add(1.0 * sum / getInterval());
			beginIndex = index;
		}

		long sum = 0;
		for (int i = 1; i < wip.size(); i++) {
			if (wip.get(i) > getMaxWip()) {
				setMaxWip(wip.get(i));
			}
			sum += wip.get(i - 1)
					* (wipChangeTime.get(i) - wipChangeTime.get(i - 1));
			// System.out.println(wip.get(i - 1)+","+wipChangeTime.get(i -
			// 1)+","+(wipChangeTime.get(i) - wipChangeTime.get(i - 1)));

		}
		long period = wipChangeTime.get(wipChangeTime.size() - 1);
		if (period == 0)
			avgWip = 0;
		else
			avgWip = 1.0 * sum / period;

		index = 0;
		setFinishedLotNum(cycleTime.size());
		if (receivedTime.size() > 1) {
			makespan=receivedTime.get(receivedTime.size()-1);
			for (long i = getInterval(); i < receivedTime.get(receivedTime
					.size() - 1); i = i + getInterval()) {
				int sum1 = 0;
				int count = 0;
				while (receivedTime.get(index) < i) {
					sum1 += cycleTime.get(index);
					count++;
					index++;
					if (index == receivedTime.size())
						break;
				}
				if (index == receivedTime.size())
					break;
				finishedLotNumInPeriod.add(count);
				cycleTimeInPeriod.add(1.0 * sum1 / count);

			}

			sum = 0;
			for (int i = 0; i < cycleTime.size(); i++) {
				if (cycleTime.get(i) > getMaxCycleTime()) {
					setMaxCycleTime(cycleTime.get(i));
				}
				if (cycleTime.get(i) < getMinCycleTime()) {
					setMinCycleTime(cycleTime.get(i));
				}

				sum += cycleTime.get(i);

			}
			if (cycleTime.size() == 0)
				setAvgCycleTime(0);
			else
				setAvgCycleTime(1.0 * sum / cycleTime.size());

			period = (receivedTime.get(receivedTime.size() - 1) - receivedTime
					.get(0));
			if (period == 0)
				setProductivity(0);
			else
				setProductivity(1.0 * getFinishedLotNum() / period
						* getInterval());

		}
		index = 0;
		setReleasedLotNum(releaseTime.size());
		if (releaseTime.size() > 1) {
			for (long i = getInterval(); i < releaseTime
					.get(releaseTime.size() - 1); i = i + getInterval()) {
				int count = 0;
				while (releaseTime.get(index) < i) {
					count++;
					index++;
					if (index == releaseTime.size())
						break;
				}
				if (index == releaseTime.size())
					break;
				releaseLotNumInPeriod.add(count);

			}

			period = releaseTime.get(getReleasedLotNum() - 1)
					- releaseTime.get(0);
			if (period == 0)
				setReleaseRatio(0);
			else
				setReleaseRatio(1.0 * getReleasedLotNum() / period
						* getInterval());
		}

		wip.clear();
		wipChangeTime.clear();
		cycleTime.clear();
		receivedTime.clear();
		releaseTime.clear();

	}

	public int getLastWip() {
		if (wip.isEmpty()) {
			return 0;
		}
		return wip.get(wip.size() - 1);
	}

	public double getAvgWip() {
		return avgWip;
	}

	public void setAvgWip(double avgWip) {
		this.avgWip = avgWip;
	}

	public double getRawProcessTime() {
		return rawProcessTime;
	}

	public void setRawProcessTime(double rawProcessTime) {
		this.rawProcessTime = rawProcessTime;
	}

	public double getMinCycleTime() {
		return minCycleTime;
	}

	public void setMinCycleTime(double minCycleTime) {
		this.minCycleTime = minCycleTime;
	}

	public double getAvgCycleTime() {
		return avgCycleTime;
	}

	public void setAvgCycleTime(double avgCycleTime) {
		this.avgCycleTime = avgCycleTime;
	}

	public int getFinishedLotNum() {
		return finishedLotNum;
	}

	public void setFinishedLotNum(int finishedLotNum) {
		this.finishedLotNum = finishedLotNum;
	}

	public double getMaxCycleTime() {
		return maxCycleTime;
	}

	public void setMaxCycleTime(double maxCycleTime) {
		this.maxCycleTime = maxCycleTime;
	}

	public int getMaxWip() {
		return maxWip;
	}

	public void setMaxWip(int maxWip) {
		this.maxWip = maxWip;
	}

	public double getProductivity() {
		return productivity;
	}

	public void setProductivity(double productivity) {
		this.productivity = productivity;
	}

	public int getReleasedLotNum() {
		return releasedLotNum;
	}

	public void setReleasedLotNum(int releasedLotNum) {
		this.releasedLotNum = releasedLotNum;
	}

	public double getReleaseRatio() {
		return releaseRatio;
	}

	public void setReleaseRatio(double releaseRatio) {
		this.releaseRatio = releaseRatio;
	}

	public long getInterval() {
		return interval;
	}

	public void setInterval(long interval) {
		this.interval = interval;
	}

	public String toString() {
		long d = 1;//000 * 60 * 60;
//		if (releasedLotNum == 0) {
//			return "";
//		}
		if (finishedLotNum == 0) {
			minCycleTime = 0;
		}
		String str = String
				.format("Product: %8s, released: %6d, finished: %6d, minCT: %8.2f, avgCT: %8.2f, maxCT: %8.2f,avgWIP: %8.2f,maxWIP: %6d",
						product, releasedLotNum, finishedLotNum, minCycleTime
								/ d, avgCycleTime / d, maxCycleTime / d,
						avgWip, maxWip);
		return str;
	}

	public long getMakespan() {
		return makespan;
	}

	public void setMakespan(long makespan) {
		this.makespan = makespan;
	}

}
