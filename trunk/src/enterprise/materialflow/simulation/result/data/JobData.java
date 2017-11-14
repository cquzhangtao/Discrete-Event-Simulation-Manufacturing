package enterprise.materialflow.simulation.result.data;

import java.io.Serializable;


public class JobData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5241390702508694812L;
	private String jobType;
	private String jobName;
	private long releaseTime;
	private int processNum = 0;
	private long cycleTime;	
	private long firstTransportBeginTime;
	
	private JobSubData[] jobSubData;
	private int processIndex = 0;
	
	public JobData(int processNum) {
		this.processNum = processNum;
		jobSubData = new JobSubData[processNum];
		for (int i = 0; i < processNum; i++)
			jobSubData[i] = new JobSubData();
	}

	public JobData(String str) {
		String[] str1 = str.split("---");
		String[] str2 = str1[0].split("-");
		jobName = str2[0];
		jobType = str2[1];
		releaseTime = Long.valueOf(str2[2]);
		firstTransportBeginTime = Long.valueOf(str2[3]);
		processNum = Integer.valueOf(str2[4]);
		String[] str3 = str1[1].split("--");
		jobSubData = new JobSubData[str3.length];
		for (int i = 0; i < str3.length; i++) {
			jobSubData[i] = new JobSubData(str3[i]);
		}

	}

	public JobData() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		String str;
		str = String.format("%s-%s-%d-%d-%d---", jobName, jobType, releaseTime,
				firstTransportBeginTime, processNum);

		for (int i = 0; i < processNum; i++) {
			if (i == processNum - 1)
				str += jobSubData[i].toString();
			else
				str += jobSubData[i].toString() + "--";
		}
		return str;

	}

	public void transportBeginTime(long time) {
		jobSubData[processIndex].setTransportBeginTime(time);
	}

	public void waitBeginTime(long time) {
		jobSubData[processIndex].setWaitBeginTime(time);
	}

	public void processBeginTime(long time) {
		jobSubData[processIndex].setProcessBeginTime(time);
	}

	public void blockBeginTime(long time) {
		jobSubData[processIndex].setBlockBeginTime(time);
	}

	public void nextTransportBeginTime(long time) {
		jobSubData[processIndex - 1].setNextTransportBeginTime(time);
	}

	public void setToolName(String toolName) {
		jobSubData[processIndex].setResourceName(toolName);
	}
	public void currentStepFinished(){
		jobSubData[processIndex].setFinished(true);
	}
	
	public void previewStepFinished(){
		jobSubData[processIndex-1].setFinished(true);
	}
	public long getBlockTime1(){
		if(processIndex==0){
			return firstTransportBeginTime-releaseTime;
		}
		return jobSubData[processIndex-1].getNextTransportBeginTime()-jobSubData[processIndex-1].getBlockBeginTime();
	}
	
	public JobData clone(){
		JobData jobData=new JobData();
		jobData.jobType=jobType;
		jobData.jobName=jobName;
		jobData.releaseTime=releaseTime;
		jobData.processNum = processNum;
		jobData.cycleTime=cycleTime;	
		jobData.firstTransportBeginTime=firstTransportBeginTime;
		jobData.jobSubData=new JobSubData[jobSubData.length] ;
		for(int i=0;i<jobSubData.length;i++){
			jobData.jobSubData[i]=jobSubData[i].clone();
		}
		return jobData;
	}
	
}