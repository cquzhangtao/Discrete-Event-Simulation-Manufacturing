package enterprise.materialflow.simulation.result.data;

public class AverageData {
	
	public static WaitDataset average(WaitDataset[] wds){
		WaitDataset wd=new WaitDataset(0);
		int num=wds.length;
		for(int i=0;i<num;i++){
			wd.setAvgWaitTime(wd.getAvgWaitTime() + wds[i].getAvgWaitTime());
			wd.setMaxWaitTime(wd.getMaxWaitTime() + wds[i].getMaxWaitTime());
		}
		wd.setAvgWaitTime(wd.getAvgWaitTime() / num);
		wd.setMaxWaitTime(wd.getMaxWaitTime() / num);
		
		return wd;
	}
	
	
	public static BlockDataset average(BlockDataset[] wds){
		BlockDataset wd=new BlockDataset(0);
		int num=wds.length;
		for(int i=0;i<num;i++){
			wd.setAvgBlockTime(wd.getAvgBlockTime() + wds[i].getAvgBlockTime());
			wd.setMaxBlockTime(wd.getMaxBlockTime() + wds[i].getMaxBlockTime());
		}
		wd.setAvgBlockTime(wd.getAvgBlockTime() / num);
		wd.setMaxBlockTime(wd.getMaxBlockTime() / num);
		
		return wd;
	}
	
	public static BufferData average(BufferData[] wds,String name){
		BufferData wd=new BufferData(name);
		int num=wds.length;
		for(int i=0;i<num;i++){
			wd.setAvgBufferSize(wd.getAvgBufferSize()
					+ wds[i].getAvgBufferSize());
			wd.setMaxBufferSize(wd.getMaxBufferSize()
					+ wds[i].getMaxBufferSize());
		}
		wd.setAvgBufferSize(wd.getAvgBufferSize() / num);
		wd.setMaxBufferSize(wd.getMaxBufferSize() / num);
		
		return wd;
	}
	
	public static ResourceData average(ResourceData[] tds){
		ResourceData td=new ResourceData();
		td.setBlockTime(0);
		td.setFreeTime(0);
		td.setProcessTime(0);
		td.setBreakdownTime(0);
		td.setMaintenanceTime(0);
		td.setInterruptNum(0);
		td.setSetupTime(0);
		td.setTotalSetupTime(0);
		
		int num=tds.length;
		for(int i=0;i<num;i++){
			td.setBlockTime(td.getBlockTime() + tds[i].getBlockTime());
			td.setFreeTime(td.getFreeTime() + tds[i].getFreeTime());
			td.setProcessTime(td.getProcessTime() + tds[i].getProcessTime());
			td.setBreakdownTime(td.getBreakdownTime()
					+ tds[i].getBreakdownTime());
			td.setMaintenanceTime(td.getMaintenanceTime()
					+ tds[i].getMaintenanceTime());
			td.setInterruptNum(td.getInterruptNum() + tds[i].getInterruptNum());
			td.setSetupTime(td.getSetupTime() + tds[i].getSetupTime());
			td.setTotalSetupTime(td.getTotalSetupTime()
					+ tds[i].getTotalSetupTime());
		}
		td.setBlockTime(td.getBlockTime() / num);
		td.setFreeTime(td.getFreeTime() / num);
		td.setProcessTime(td.getProcessTime() / num);
		td.setBreakdownTime(td.getBreakdownTime() / num);
		td.setMaintenanceTime(td.getMaintenanceTime() / num);
		td.setInterruptNum(td.getInterruptNum() / num);
		td.setSetupTime(td.getSetupTime() / num);
		td.setTotalSetupTime(td.getTotalSetupTime() / num);
		
		return td;
	}


	public  static ProductData average(ProductData[]rds,String product){
		ProductData rd=new ProductData(product);
		rd.setMinCycleTime(0);
		int num=rds.length;
		for(int i=0;i<num;i++){
			rd.setAvgCycleTime(rd.getAvgCycleTime() + rds[i].getAvgCycleTime());
			rd.setAvgWip(rd.getAvgWip() + rds[i].getAvgWip());
			rd.setFinishedLotNum(rd.getFinishedLotNum()
					+ rds[i].getFinishedLotNum());
			rd.setMaxCycleTime(rd.getMaxCycleTime() + rds[i].getMaxCycleTime());
			rd.setMinCycleTime(rd.getMinCycleTime() + rds[i].getMinCycleTime());
			rd.setMaxWip(rd.getMaxWip() + rds[i].getMaxWip());
			rd.setProductivity(rd.getProductivity() + rds[i].getProductivity());
			rd.setReleasedLotNum(rd.getReleasedLotNum()
					+ rds[i].getReleasedLotNum());
			rd.setReleaseRatio(rd.getReleaseRatio() + rds[i].getReleaseRatio());
		}
		rd.setAvgCycleTime(rd.getAvgCycleTime() / num);
		rd.setAvgWip(rd.getAvgWip() / num);
		rd.setFinishedLotNum(rd.getFinishedLotNum() / num);
		rd.setMaxCycleTime(rd.getMaxCycleTime() / num);
		rd.setMinCycleTime(rd.getMinCycleTime() / num);
		rd.setMaxWip(rd.getMaxWip() / num);
		rd.setProductivity(rd.getProductivity() / num);
		rd.setReleasedLotNum(rd.getReleasedLotNum() / num);
		rd.setReleaseRatio(rd.getReleaseRatio() / num);
		rd.setRawProcessTime(rds[0].getRawProcessTime());
		//rd.product=rds[0].product;
		return rd;
		
	}
}
