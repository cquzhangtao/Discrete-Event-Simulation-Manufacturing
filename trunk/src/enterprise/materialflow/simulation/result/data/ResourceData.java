package enterprise.materialflow.simulation.result.data;

import java.io.Serializable;
import java.util.ArrayList;

public class ResourceData implements Serializable {

	private String resourceName = "";
	private String resourceGroupName = "";
	private double freeTime;
	private double blockTime;
	private double processTime;
	private double breakdownTime;
	private double maintenanceTime;
	private double setupTime;
	private double totalSetupTime;
	private ArrayList<Double> beginFreeTime = new ArrayList<Double>();
	private ArrayList<Double> ifreeTime = new ArrayList<Double>();
	private ArrayList<Double> utiliztionbyDay = new ArrayList<Double>();
	private ArrayList<StartEnd> setupTimeDataset = new ArrayList<StartEnd>();
	private ArrayList<StartEnd> blockTimeDataset = new ArrayList<StartEnd>();
	private ArrayList<Double> beginInterruptTime = new ArrayList<Double>();
	private ArrayList<Double> interruptTime = new ArrayList<Double>();
	private ArrayList<StartEnd> processTimeDataset = new ArrayList<StartEnd>();
	private ArrayList<StartEnd> interruptTimeDataset = new ArrayList<StartEnd>();
	private int interruptNum;

	private int interval = 7 * 24 * 3600;

	private double freeTime_s;
	private double blockTime_s;
	private double processTime_s;
	private double breakdownTime_s;
	private double maintenanceTime_s;
	private double setupTime_s;
	private int beginFreeTimeSize;
	private int ifreeTimeSize;
	private int beginInterruptTimeSize;
	private int interruptTimeSize;
	private int processTimeDatasetSize;
	private int blockTimeDatasetSize;
	private int setupTimeDataTimeSize;
	private int interruptTimeDatasetSize;

	public void save() {
		freeTime_s = getFreeTime();
		blockTime_s = getBlockTime();
		processTime_s = processTime;
		breakdownTime_s = breakdownTime;
		maintenanceTime_s = getMaintenanceTime();
		setupTime_s = getSetupTime();

		beginFreeTimeSize = beginFreeTime.size();
		ifreeTimeSize = ifreeTime.size();
		beginInterruptTimeSize = beginInterruptTime.size();
		interruptTimeSize = interruptTime.size();

		blockTimeDatasetSize = blockTimeDataset.size();
		setupTimeDataTimeSize = setupTimeDataset.size();
		processTimeDatasetSize = processTimeDataset.size();
		interruptTimeDatasetSize = interruptTimeDataset.size();
	}

	public void recover() {
		setFreeTime(freeTime_s);
		setBlockTime(blockTime_s);
		processTime = processTime_s;
		breakdownTime = breakdownTime_s;
		setMaintenanceTime(maintenanceTime_s);
		setSetupTime(setupTime_s);
		while (beginFreeTime.size() > beginFreeTimeSize)
			beginFreeTime.remove(beginFreeTimeSize);
		while (ifreeTime.size() > ifreeTimeSize)
			ifreeTime.remove(ifreeTimeSize);
		while (beginInterruptTime.size() > beginInterruptTimeSize)
			beginInterruptTime.remove(beginInterruptTimeSize);
		while (interruptTime.size() > interruptTimeSize)
			interruptTime.remove(interruptTimeSize);
		while (blockTimeDataset.size() > blockTimeDatasetSize)
			blockTimeDataset.remove(blockTimeDatasetSize);
		while (setupTimeDataset.size() > setupTimeDataTimeSize)
			setupTimeDataset.remove(setupTimeDataTimeSize);
		while (processTimeDataset.size() > processTimeDatasetSize)
			processTimeDataset.remove(processTimeDatasetSize);
		while (interruptTimeDataset.size() > interruptTimeDatasetSize)
			interruptTimeDataset.remove(interruptTimeDatasetSize);
	}

	public ResourceData(String toolName, String toolGroupName) {
		this.setResourceName(toolName);
		this.setResourceGroupName(toolGroupName);
	}

	public ResourceData() {
		setResourceName("");
		setResourceGroupName("");
	}

	public void addSetupTime(double setupBeginTime, double d) {
		setupTimeDataset.add(new StartEnd(setupBeginTime, d));
	}

	public void addBlockTime(double blockBeginTime, double d) {
		blockTimeDataset.add(new StartEnd(blockBeginTime, d));
	}

	public void addFreeTime(double freeBeginTime, double d) {
		beginFreeTime.add(freeBeginTime);
		ifreeTime.add(d);

	}

	public void addProcessTime(double processBeginTime, double d, String job) {
		processTimeDataset.add(new StartEnd(processBeginTime, d, job));

	}

	public void addInterruptTime(double breakdownBeginTime, double d) {
		beginInterruptTime.add(breakdownBeginTime);
		interruptTime.add(d - breakdownBeginTime);
		interruptTimeDataset.add(new StartEnd(breakdownBeginTime, d));

	}

	public void reset() {
		setFreeTime(0);
		setSetupTime(0);
		processTime = 0;
		setBlockTime(0);
		breakdownTime = 0;
		setMaintenanceTime(0);
		setTotalSetupTime(0);
		setInterruptNum(0);
		beginFreeTime.clear();
		ifreeTime.clear();
		getUtiliztionbyDay().clear();
		beginInterruptTime.clear();
		interruptTime.clear();
		this.blockTimeDataset.clear();
		this.setupTimeDataset.clear();
		this.processTimeDataset.clear();
		this.interruptTimeDataset.clear();
	}

	public String data2String() {
		double totalTime = getFreeTime() + getSetupTime() + processTime + getBlockTime()
				+ breakdownTime + getMaintenanceTime();
		// System.out.println(totalTime/3600.0/24);
		return String.format("%s-%s-%f-%f-%f-%f-%f-%f", getResourceName(),
				getResourceGroupName(), getFreeTime() / (1.0 * totalTime), getSetupTime()
						/ (1.0 * totalTime), processTime / (1.0 * totalTime),
				getBlockTime() / (1.0 * totalTime), breakdownTime
						/ (1.0 * totalTime), getMaintenanceTime()
						/ (1.0 * totalTime));
	}

	public void stat() {
		setTotalSetupTime(1.0 * getSetupTime() / 3600);
		double totalTime = getFreeTime() + getSetupTime() + processTime + getBlockTime()
				+ breakdownTime + getMaintenanceTime();
		if (totalTime > 0) {
			setFreeTime(getFreeTime() / (1.0 * totalTime));
			setSetupTime(getSetupTime() / (1.0 * totalTime));
			processTime /= (1.0 * totalTime);
			setBlockTime(getBlockTime() / (1.0 * totalTime));
			breakdownTime /= (1.0 * totalTime);
			setMaintenanceTime(getMaintenanceTime() / (1.0 * totalTime));
		}

		if (beginFreeTime.size() == 0)
			return;
		setInterruptNum(beginInterruptTime.size());
		int index = 0;
		for (int i = interval; i < beginFreeTime.get(beginFreeTime.size() - 1)
				+ ifreeTime.get(beginFreeTime.size() - 1); i += interval) {
			long sum = 0;
			if (index > 0) {
				if (beginFreeTime.get(index - 1) + ifreeTime.get(index - 1) > i
						- interval) {
					sum += beginFreeTime.get(index - 1)
							+ ifreeTime.get(index - 1) - i + interval;
				}
			}
			while (beginFreeTime.get(index) < i) {
				sum += ifreeTime.get(index);
				index++;
				if (index == beginFreeTime.size())
					break;
			}
			if (index == beginFreeTime.size()) {
				if (beginFreeTime.get(index - 1) + ifreeTime.get(index - 1) > i) {
					sum -= (beginFreeTime.get(index - 1)
							+ ifreeTime.get(index - 1) - i);
					getUtiliztionbyDay().add(1 - 1.0 * sum / interval);
				}
				break;
			}
			if (index > 0) {
				if (beginFreeTime.get(index - 1) + ifreeTime.get(index - 1) > i) {
					sum -= (beginFreeTime.get(index - 1)
							+ ifreeTime.get(index - 1) - i);
				}
			}
			getUtiliztionbyDay().add(1 - 1.0 * sum / interval);

		}
		beginFreeTime.clear();
		ifreeTime.clear();

	}

	public double totalTime() {
		return getFreeTime() + getSetupTime() + processTime + getBlockTime() + breakdownTime
				+ getMaintenanceTime();
	}

	public void updateData(String s) {

		String[] temp = s.split("-");
		if (temp.length < 8)
			return;
		setResourceName(temp[0]);
		setResourceGroupName(temp[1]);
		setFreeTime(Double.valueOf(temp[2]));
		setSetupTime(Double.valueOf(temp[3]));
		processTime = Double.valueOf(temp[4]);
		setBlockTime(Double.valueOf(temp[5]));
		breakdownTime = Double.valueOf(temp[6]);
		setMaintenanceTime(Double.valueOf(temp[7]));
	}

	public double getBreakdownTime() {
		return breakdownTime;
	}

	public void setBreakdownTime(double breakdownTime) {
		this.breakdownTime = breakdownTime;
	}

	public double getProcessTime() {
		return processTime;
	}

	public void setProcessTime(double processTime) {
		this.processTime = processTime;
	}

	public double getUtilization() {
		if (totalTime() == 0) {
			return 0;
		}
		return processTime / totalTime();
	}

	public double getBlockTime() {
		return blockTime;
	}

	public void setBlockTime(double blockTime) {
		this.blockTime = blockTime;
	}

	public double getFreeTime() {
		return freeTime;
	}

	public void setFreeTime(double freeTime) {
		this.freeTime = freeTime;
	}

	public double getMaintenanceTime() {
		return maintenanceTime;
	}

	public void setMaintenanceTime(double maintenanceTime) {
		this.maintenanceTime = maintenanceTime;
	}

	public int getInterruptNum() {
		return interruptNum;
	}

	public void setInterruptNum(int interruptNum) {
		this.interruptNum = interruptNum;
	}

	public double getSetupTime() {
		return setupTime;
	}

	public void setSetupTime(double setupTime) {
		this.setupTime = setupTime;
	}

	public double getTotalSetupTime() {
		return totalSetupTime;
	}

	public void setTotalSetupTime(double totalSetupTime) {
		this.totalSetupTime = totalSetupTime;
	}

	public String getResourceGroupName() {
		return resourceGroupName;
	}

	public void setResourceGroupName(String resourceGroupName) {
		this.resourceGroupName = resourceGroupName;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public ArrayList<Double> getUtiliztionbyDay() {
		return utiliztionbyDay;
	}

	public void setUtiliztionbyDay(ArrayList<Double> utiliztionbyDay) {
		this.utiliztionbyDay = utiliztionbyDay;
	}

}
