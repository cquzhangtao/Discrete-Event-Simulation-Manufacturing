package enterprise.materialflow.simulation.result.data;

import java.io.Serializable;
import java.util.ArrayList;

public class ResourceGroupDataset implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8762331968679254274L;
	private ArrayList<ResourceGroupData> resourceGroupDataset=new ArrayList<ResourceGroupData>();
	private ResourceData allResourceData=new ResourceData("All","All");
	private WaitDataset allWaitDataset;
	private BlockDataset allBlockDataset;
	private int toolNum=0;
	
	public void add(ResourceGroupData data,int priod)
	{
		resourceGroupDataset.add(data);

		allWaitDataset=new WaitDataset(priod);
		allBlockDataset=new BlockDataset(priod);
		allWaitDataset.setBufferName("All");
		allBlockDataset.setBufferName("All");
	}
	public void reset(){
		allResourceData.reset();
		allWaitDataset.reset();
		allBlockDataset.reset();
		toolNum=0;
		for(int i=0;i<resourceGroupDataset.size();i++){
			resourceGroupDataset.get(i).reset();}
	}
	
	public void stat(){
		int minIndex=-1;
		long minSize=999999999;
		int interruptNum=0;
		long totalSetupTime=0;
		for(int i=0;i<resourceGroupDataset.size();i++){
			resourceGroupDataset.get(i).stat();
			toolNum+=resourceGroupDataset.get(i).getResourceNum();
			allResourceData.setFreeTime(allResourceData.getFreeTime() + resourceGroupDataset.get(i).getResourceGroupDataSum().getFreeTime());
			allResourceData.setSetupTime(allResourceData.getSetupTime() + resourceGroupDataset.get(i).getResourceGroupDataSum().getSetupTime());
			allResourceData.setBlockTime(allResourceData.getBlockTime() + resourceGroupDataset.get(i).getResourceGroupDataSum().getBlockTime());
			allResourceData.setProcessTime(allResourceData.getProcessTime() + resourceGroupDataset.get(i).getResourceGroupDataSum().getProcessTime());
			allResourceData.setBreakdownTime(allResourceData.getBreakdownTime()
					+ resourceGroupDataset.get(i).getResourceGroupDataSum().getBreakdownTime());
			allResourceData.setMaintenanceTime(allResourceData.getMaintenanceTime()
					+ resourceGroupDataset.get(i).getResourceGroupDataSum().getMaintenanceTime());
			
			if(resourceGroupDataset.get(i).getResourceGroupDataSum().getUtiliztionbyDay().size()<minSize){
				minSize=resourceGroupDataset.get(i).getResourceGroupDataSum().getUtiliztionbyDay().size();
				minIndex=i;
			}
			interruptNum+=resourceGroupDataset.get(i).getResourceGroupDataSum().getInterruptNum();
			totalSetupTime+=resourceGroupDataset.get(i).getResourceGroupDataSum().getTotalSetupTime();
			
		}
		allResourceData.stat();
		//muse below here
		allResourceData.setInterruptNum(interruptNum);
		allResourceData.setTotalSetupTime(totalSetupTime);
		
		if(minIndex!=-1){
			for(int i=0;i<resourceGroupDataset.get(minIndex).getResourceGroupDataSum().getUtiliztionbyDay().size();i++){
				
				double sum=0;
				for(int j=0;j<resourceGroupDataset.size();j++){
					sum+=(resourceGroupDataset.get(j).getResourceGroupDataSum().getUtiliztionbyDay().get(i));
				}
				allResourceData.getUtiliztionbyDay().add(sum/resourceGroupDataset.size());
			}
		}
		double sum=0;
		double maxWaitTime=0;
		minSize=999999999;
		double sum1=0;
		double maxBlockTime=0;
		int minSize1=999999999;
		for(int i=0;i<resourceGroupDataset.size();i++){
			//toolGroupDataset.get(i).waitDataset.stat();
			sum+=resourceGroupDataset.get(i).getWaitDataset().getAvgWaitTime();
			
			if(resourceGroupDataset.get(i).getWaitDataset().getMaxWaitTime()>maxWaitTime){
				maxWaitTime=resourceGroupDataset.get(i).getWaitDataset().getMaxWaitTime();
			}
			if(resourceGroupDataset.get(i).getWaitDataset().size()<minSize){
				minSize=resourceGroupDataset.get(i).getWaitDataset().size();
			}
			
			sum1+=resourceGroupDataset.get(i).getBlockDataset().getAvgBlockTime();
			
			if(resourceGroupDataset.get(i).getBlockDataset().getMaxBlockTime()>maxBlockTime){
				maxBlockTime=resourceGroupDataset.get(i).getBlockDataset().getMaxBlockTime();
			}
			if(resourceGroupDataset.get(i).getBlockDataset().size()<minSize1){
				minSize1=resourceGroupDataset.get(i).getBlockDataset().size();
			}
		}
		allWaitDataset.setAvgWaitTime(sum/resourceGroupDataset.size());
		allWaitDataset.setMaxWaitTime(maxWaitTime);
		allBlockDataset.setAvgBlockTime(sum1/resourceGroupDataset.size());
		allBlockDataset.setMaxBlockTime(maxBlockTime);

		for(int i=0;i<minSize;i++){
			sum=0;
			for(int j=0;j<resourceGroupDataset.size();j++){
				sum+=resourceGroupDataset.get(j).getWaitDataset().getWaitDatasetbyDay().get(i);
			}
			allWaitDataset.getWaitDatasetbyDay().add(1.0*sum/resourceGroupDataset.size());
		}
		for(int i=0;i<minSize1;i++){
			sum1=0;
			for(int j=0;j<resourceGroupDataset.size();j++){
				sum1+=resourceGroupDataset.get(j).getBlockDataset().getBlockDatasetbyDay().get(i);
			}
			allBlockDataset.getBlockDatasetbyDay().add(1.0*sum1/resourceGroupDataset.size());
		}
		
	}

	public int size() {
		// TODO Auto-generated method stub
		return resourceGroupDataset.size();
	}

	public ResourceGroupData get(int i) {
		// TODO Auto-generated method stub
		return resourceGroupDataset.get(i);
	}
	public ResourceData get(String toolGroupName) {
		// TODO Auto-generated method stub
		for(int i=0;i<resourceGroupDataset.size();i++){
			if(resourceGroupDataset.get(i).getResourceGroupName().equals(toolGroupName))
			{
				return resourceGroupDataset.get(i).getResourceGroupDataSum();
			}
		}
		return null;
	}
	public ResourceGroupData getResourceGroupData(String toolGroupName) {
		// TODO Auto-generated method stub
		for(int i=0;i<resourceGroupDataset.size();i++){
			if(resourceGroupDataset.get(i).getResourceGroupName().equals(toolGroupName))
			{
				return resourceGroupDataset.get(i);
			}
		}
		return null;
	}
	
	public void save(){
		for(ResourceGroupData td:resourceGroupDataset)
			td.save();
	}
	public void recover(){
		for(ResourceGroupData td:resourceGroupDataset)
			td.recover();
	}
	
}
