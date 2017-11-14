package enterprise.materialflow.simulation.result;

import enterprise.materialflow.simulation.result.data.BufferData;
import enterprise.materialflow.simulation.result.data.BufferDataset;
import enterprise.materialflow.simulation.result.data.JobDataset;
import enterprise.materialflow.simulation.result.data.ProductDataset;
import enterprise.materialflow.simulation.result.data.ResourceGroupData;
import enterprise.materialflow.simulation.result.data.ResourceGroupDataset;


public class SimulationResult {
	public BufferDataset bufferDataset;
	public JobDataset jobDataset;
	public ProductDataset releaseDataset;
	public ResourceGroupDataset toolGroupDataset;
	
	public SimulationResult(){
		bufferDataset = new BufferDataset();
		releaseDataset = new ProductDataset();
		jobDataset = new JobDataset();
		toolGroupDataset = new ResourceGroupDataset();
	}
	
//	public void generateStructure(ManufactureModel manuModel,SimulationInfo simulationInfo){
//
//		for (int i = 0; i < manuModel.getToolGroups().size(); i++) {
//			ToolGroupInfo toolGroupInfo = manuModel.getToolGroups().get(i);
//			BufferData bufferdata = new BufferData(toolGroupInfo.getToolGroupName());
//			bufferdata.interval=simulationInfo.dataAcqTime;
//			bufferDataset.add(bufferdata);
//			ToolGroupData toolgroupData = new ToolGroupData(toolGroupInfo.getToolGroupName(),simulationInfo.dataAcqTime);
//			toolGroupDataset.add(toolgroupData,simulationInfo.dataAcqTime);
//			toolGroupInfo.bindDatasettoTool(toolgroupData,simulationInfo.dataAcqTime);
//		}
//
//		releaseDataset.init(manuModel.getProducts(),simulationInfo.dataAcqTime);
//		jobDataset.init(manuModel.getProducts().length);
//
//	}
	public ResourceGroupData getToolGroupData(String name){
		return toolGroupDataset.getResourceGroupData(name);
	}
	
	public BufferData getBufferData(String name){
		return bufferDataset.getBufferData(name);
	}
	
	public void renew(){
		bufferDataset = new BufferDataset();
		releaseDataset = new ProductDataset();
		jobDataset = new JobDataset();
		toolGroupDataset = new ResourceGroupDataset();
	}
	
	public void reset(){
		bufferDataset.reset();
		toolGroupDataset.reset();
		releaseDataset.reset();
		jobDataset.reset();
	}
	public void stat(){
		bufferDataset.stat();
		toolGroupDataset.stat();
		releaseDataset.stat();
	}
	public void save(){
		bufferDataset.save();
		toolGroupDataset.save();
		releaseDataset.save();
		jobDataset.save();
	}
	
	public void recover(){
		bufferDataset.recover();
		toolGroupDataset.recover();
		releaseDataset.recover();
		jobDataset.recover();
	}

	public BufferDataset getBufferDataset() {
		return bufferDataset;
	}

	public JobDataset getJobDataset() {
		return jobDataset;
	}

	public ProductDataset getReleaseDataset() {
		return releaseDataset;
	}

	public ResourceGroupDataset getToolGroupDataset() {
		return toolGroupDataset;
	}

	public void setBufferDataset(BufferDataset bufferDataset) {
		this.bufferDataset = bufferDataset;
	}

	public void setJobDataset(JobDataset jobDataset) {
		this.jobDataset = jobDataset;
	}

	public void setReleaseDataset(ProductDataset releaseDataset) {
		this.releaseDataset = releaseDataset;
	}

	public void setToolGroupDataset(ResourceGroupDataset toolGroupDataset) {
		this.toolGroupDataset = toolGroupDataset;
	}
}
