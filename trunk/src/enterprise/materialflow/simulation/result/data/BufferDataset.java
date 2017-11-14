package enterprise.materialflow.simulation.result.data;

import java.io.Serializable;
import java.util.ArrayList;

public class BufferDataset implements Serializable {
	
	private ArrayList<BufferData> bufferDataset=new ArrayList<BufferData>();
	private BufferData allBufferData=new BufferData("All");


	public void reset(){
		allBufferData.reset();
		for(int i=0;i<bufferDataset.size();i++)
		{
			bufferDataset.get(i).reset();
		}
	}
	
	public void stat(){
		double sum=0;
		int minSize=999999999;
		int maxBufferSize=0;
		for(int i=0;i<bufferDataset.size();i++)
		{
			bufferDataset.get(i).stat();
			sum+=bufferDataset.get(i).getAvgBufferSize();
			if(maxBufferSize<bufferDataset.get(i).getMaxBufferSize()){
				maxBufferSize=bufferDataset.get(i).getMaxBufferSize();
			}
			if(bufferDataset.get(i).getBufferSizebyDay().size()<minSize)
				minSize=bufferDataset.get(i).getBufferSizebyDay().size();
		}
		allBufferData.setAvgBufferSize(1.0*sum/bufferDataset.size());
		allBufferData.setMaxBufferSize(maxBufferSize);
		
		for(int i=0;i<minSize;i++){
			sum=0;
			for(int j=0;j<bufferDataset.size();j++){
				sum+=bufferDataset.get(j).getBufferSizebyDay().get(i);
			}
			allBufferData.getBufferSizebyDay().add(sum);
		}
		
	}
	public void add(BufferData data)
	{
		bufferDataset.add(data);
	}
	public BufferData get(int index){
		return bufferDataset.get(index);
	}
	public int size(){
		return bufferDataset.size();
	}
	public BufferData getBufferData(String name){
		for(int i=0;i<bufferDataset.size();i++){
			if(bufferDataset.get(i).getBufferName().equals(name))
				return bufferDataset.get(i);
		}
		return null;
	}
	public void save(){
		for(BufferData td:bufferDataset)
			td.save();
	}
	public void recover(){
		for(BufferData td:bufferDataset)
			td.recover();
	}
}
