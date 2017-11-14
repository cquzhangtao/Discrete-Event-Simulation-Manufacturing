package enterprise.materialflow.simulation.result.data;

import java.io.Serializable;
import java.util.ArrayList;

public class ResourceGroupData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2746333220859078335L;
	private String resourceGroupName;
	private ArrayList<ResourceData> resourceDataset=new ArrayList<ResourceData>();
	private ResourceData resourceGroupDataSum=new ResourceData();
	private WaitDataset waitDataset;
	private BlockDataset blockDataset;
	private int resourceNum;
	
	public ResourceGroupData(String name,int period){
		setResourceGroupName(name);
//		for(int i=0;i<toolNum;i++){
//			toolDataset.add(new ToolData());
//		}
		getResourceGroupDataSum().setResourceName(name);
		getResourceGroupDataSum().setResourceGroupName(name);

		setWaitDataset(new WaitDataset(period));
		setBlockDataset(new BlockDataset(period));
		getWaitDataset().setBufferName(name);
	}
	public void reset(){
		for(int i=0;i<resourceDataset.size();i++){
			resourceDataset.get(i).reset();
		}
		getResourceGroupDataSum().reset();
		getWaitDataset().reset();
		getBlockDataset().reset();
		
	}
	public void addResourceData(ResourceData data){
		//data.toolGroupData=toolGroupData;
		resourceDataset.add(data);
	}
	
	public void addFreeTime(int index,long time){
		resourceDataset.get(index).setFreeTime(resourceDataset.get(index).getFreeTime() + time);
	}
	public void addSetupTime(int index,long time){
		resourceDataset.get(index).setSetupTime(resourceDataset.get(index).getSetupTime() + time);
	}
	public void addProcessTime(int index,long time){
		resourceDataset.get(index).setProcessTime(resourceDataset.get(index).getProcessTime()+time);
	}
	public void addBlockTime(int index,long time){
		resourceDataset.get(index).setBlockTime(resourceDataset.get(index).getBlockTime() + time);
	}
	public void addBreakdownTime(int index,long time){
		resourceDataset.get(index).setBreakdownTime(time+resourceDataset.get(index).getBreakdownTime());
	}
	public void addMaintenanceTime(int index,long time){
		resourceDataset.get(index).setMaintenanceTime(resourceDataset.get(index).getMaintenanceTime() + time);
	}
	public void stat()
	{
		int minSize=999999999;
		int minIndex=-1;
		int interruptNum=0;
		long totalSetupTime=0;
		setResourceNum(resourceDataset.size());
		for(int i=0;i<resourceDataset.size();i++){
			resourceDataset.get(i).stat();
			getResourceGroupDataSum().setFreeTime(getResourceGroupDataSum().getFreeTime() + resourceDataset.get(i).getFreeTime());
			getResourceGroupDataSum().setSetupTime(getResourceGroupDataSum().getSetupTime() + resourceDataset.get(i).getSetupTime());
			getResourceGroupDataSum().setProcessTime(getResourceGroupDataSum().getProcessTime() + resourceDataset.get(i).getProcessTime());
			getResourceGroupDataSum().setBlockTime(getResourceGroupDataSum().getBlockTime() + resourceDataset.get(i).getBlockTime());
			getResourceGroupDataSum().setBreakdownTime(getResourceGroupDataSum().getBreakdownTime()
					+ resourceDataset.get(i).getBreakdownTime());
			getResourceGroupDataSum().setMaintenanceTime(getResourceGroupDataSum().getMaintenanceTime()
					+ resourceDataset.get(i).getMaintenanceTime());
			if(resourceDataset.get(i).getUtiliztionbyDay().size()<minSize){
				minSize=resourceDataset.get(i).getUtiliztionbyDay().size();
				minIndex=i;
			}
			interruptNum+=resourceDataset.get(i).getInterruptNum();
			totalSetupTime+=resourceDataset.get(i).getTotalSetupTime();
			
		}
		getResourceGroupDataSum().stat();
		getResourceGroupDataSum().setInterruptNum(interruptNum);
		getResourceGroupDataSum().setTotalSetupTime(totalSetupTime);
		if(minIndex!=-1){
			for(int i=0;i<resourceDataset.get(minIndex).getUtiliztionbyDay().size();i++){
				
				double sum=0;
				for(int j=0;j<resourceDataset.size();j++){
					sum+=(resourceDataset.get(j).getUtiliztionbyDay().get(i));
				}
				getResourceGroupDataSum().getUtiliztionbyDay().add(sum/resourceDataset.size());
			}
		}
		getWaitDataset().stat();
		getBlockDataset().stat();
		
		
	}
	public int size() {
		return resourceDataset.size();
	}
	public ResourceData get(int j) {
		return resourceDataset.get(j);
	}
	public ResourceData get(String toolName) {
		for(int i=0;i<resourceDataset.size();i++){
			if(resourceDataset.get(i).getResourceName().equals(toolName))
			{
				return resourceDataset.get(i);
			}
		}
		return null;
	}
	
	public void save(){
		for(int i=0;i<resourceDataset.size();i++){
			resourceDataset.get(i).save();
		}
		getWaitDataset().save();
		getBlockDataset().save();
	}
	public void recover(){
		for(int i=0;i<resourceDataset.size();i++){
			resourceDataset.get(i).recover();
		}
		getWaitDataset().recover();
		getBlockDataset().recover();
	}
	public ResourceData getResourceGroupDataSum() {
		return resourceGroupDataSum;
	}
	public void setResourceGroupDataSum(ResourceData resourceGroupDataSum) {
		this.resourceGroupDataSum = resourceGroupDataSum;
	}
	public int getResourceNum() {
		return resourceNum;
	}
	public void setResourceNum(int resourceNum) {
		this.resourceNum = resourceNum;
	}
	public WaitDataset getWaitDataset() {
		return waitDataset;
	}
	public void setWaitDataset(WaitDataset waitDataset) {
		this.waitDataset = waitDataset;
	}
	public BlockDataset getBlockDataset() {
		return blockDataset;
	}
	public void setBlockDataset(BlockDataset blockDataset) {
		this.blockDataset = blockDataset;
	}
	public String getResourceGroupName() {
		return resourceGroupName;
	}
	public void setResourceGroupName(String resourceGroupName) {
		this.resourceGroupName = resourceGroupName;
	}
	

}
