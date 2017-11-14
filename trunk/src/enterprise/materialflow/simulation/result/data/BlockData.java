package enterprise.materialflow.simulation.result.data;

import java.io.Serializable;

public class BlockData implements Serializable{

	private long endBlockTime;
	private long blockTime;
	
	public BlockData(long endBlockTime, long blockTime) {
		this.setEndBlockTime(endBlockTime);
		this.setBlockTime(blockTime);		
	}

	public long getEndBlockTime() {
		return endBlockTime;
	}

	public void setEndBlockTime(long endBlockTime) {
		this.endBlockTime = endBlockTime;
	}

	public long getBlockTime() {
		return blockTime;
	}

	public void setBlockTime(long blockTime) {
		this.blockTime = blockTime;
	}

}
