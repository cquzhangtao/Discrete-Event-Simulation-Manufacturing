package enterprise.materialflow.simulation.result.data;

import java.io.Serializable;
import java.util.ArrayList;

public class BlockDataset implements Serializable {

	private String bufferName;
	private double maxBlockTime = 0;
	private double avgBlockTime = 0;
	private int interval = 24 * 3600;
	private ArrayList<BlockData> blockDataset = new ArrayList<BlockData>();
	private ArrayList<Double> blockDatasetbyDay = new ArrayList<Double>();
	private int blockDatasetSize;

	public BlockDataset(int period) {
		interval = period;
	}

	public void save() {
		blockDatasetSize = blockDataset.size();
	}

	public void recover() {
		while (blockDataset.size() > blockDatasetSize)
			blockDataset.remove(blockDatasetSize);
	}

	public void add(long endBlockTime, long blockTime) {
		blockDataset.add(new BlockData(endBlockTime, blockTime));
	}

	public void reset() {
		setMaxBlockTime(0);
		setAvgBlockTime(0);
		blockDataset.clear();
		getBlockDatasetbyDay().clear();
	}

	public void stat() {
		int count = 0;
		int time = interval;
		long sum = 0;
		for (int i = 0; i < blockDataset.size(); i++) {
			if (blockDataset.get(i).getEndBlockTime() < time) {
				count++;
				sum += blockDataset.get(i).getBlockTime();
			} else {
				if (count == 0)
					getBlockDatasetbyDay().add(0.0);
				else {
					getBlockDatasetbyDay().add(1.0 * sum / count / 3600);
					count = 0;
				}
				sum = 0;

				time += interval;
				i = i - 1;
			}
		}
		sum = 0;
		for (int i = 0; i < blockDataset.size(); i++) {
			if (blockDataset.get(i).getBlockTime() > getMaxBlockTime()) {
				setMaxBlockTime(blockDataset.get(i).getBlockTime());
			}
			sum += blockDataset.get(i).getBlockTime();
		}
		if (blockDataset.size() > 0)
			setAvgBlockTime(1.0 * sum / blockDataset.size() / 3600);
		else
			setAvgBlockTime(0);
		setMaxBlockTime(1.0 * getMaxBlockTime() / 3600);
		blockDataset.clear();
		// blockDataset=null;

	}

	public int size() {
		// TODO Auto-generated method stub
		return getBlockDatasetbyDay().size();
	}

	public double getAvgBlockTime() {
		return avgBlockTime;
	}

	public void setAvgBlockTime(double avgBlockTime) {
		this.avgBlockTime = avgBlockTime;
	}

	public double getMaxBlockTime() {
		return maxBlockTime;
	}

	public void setMaxBlockTime(double maxBlockTime) {
		this.maxBlockTime = maxBlockTime;
	}

	public String getBufferName() {
		return bufferName;
	}

	public void setBufferName(String bufferName) {
		this.bufferName = bufferName;
	}

	public ArrayList<Double> getBlockDatasetbyDay() {
		return blockDatasetbyDay;
	}

	public void setBlockDatasetbyDay(ArrayList<Double> blockDatasetbyDay) {
		this.blockDatasetbyDay = blockDatasetbyDay;
	}

}
