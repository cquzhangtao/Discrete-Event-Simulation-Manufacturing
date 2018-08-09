package com.tao.fab.sim.event;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import simulation.core.event.ISimulationEvent;
import simulation.core.event.SimulationEventType;

public class EventHandler {
	private static String tag = EventHandler.class.getName();

	public List<ISimulationEvent> releaseJob(IProduct product, long time) {

		IProductJob pjob = product.getJob().clone();
		pjob.setProduct(product);

		/*long nextime = product.getTimeToNextRelease();
		AbstractEvent event = new AbstractEvent() {


			@Override
			public List<ISimulationEvent> response(long currentTime) {
				releaseJob(product, time);
				return null;
			}

		};
		event.setTime(nextime+product.getSimulation().getCurrentTime());
		product.getSimulation().addEvent(event);;*/
	

		pjob.addJobFinishListener(new JobFinishListener() {

			@Override
			public List<ISimulationEvent>  onJobFinish(IJob job,long time) {
				List<ISimulationEvent> events=new ArrayList<ISimulationEvent>();
				Log.d(tag, "job completed",time);
				return events;

			}
		});
		Log.d(tag, "job started",time);
		return startJob(pjob, time);
	}

	public List<ISimulationEvent> startJob(IJob job, long time) {
		return moveJobToNextStep(job,time);
		/*for (IStep step : job.getCurrentSteps()) {
			if(step.getRequiredResourceGroup()==null){
				Log.w(tag,"no resources specified at the step");
				job.setCurrentStep(step);
				AbstractEvent event = new AbstractEvent(){

					@Override
					public List<ISimulationEvent> response(long currentTime) {
						Log.d(tag, "finish step",time);
						if(!job.goToNextStep()){
							onJo
						}else{
							startJob(job,currentTime);
						}
						
						
						return null;
					}};
					event.setTime(job.getSimulation().getCurrentTime()+step.getProcessTime());
				job.getSimulation().addEvent(event);

					
			}
			else{
				jobArrive(step.getRequiredResourceGroup(), job, time);
			}
		}*/
	}

	private List<IJob> splitJob(IResourceGroup rg, IJob job) {
		IStep step = job.getCurrentStep(rg);
		ISplittingConfig sconfig = step.getSplittingConfig();
		int size = sconfig.getSplitSize();
		int num = job.getChildren().size() / size;
		List<IJob> jobs = new ArrayList<IJob>();

		IJob njob = job.newInstance();
		jobs.add(njob);
		for (int i = 0; i < job.getChildren().size(); i++) {
			if (i % size == 0) {
				njob = job.newInstance();
				njob.setPartialSplitting(true);
				//njob.setCompleteSplitting(false);
				jobs.add(njob);
			}
			njob.getChildren().add(job.getChildren().get(i));
		}
		return jobs;
	}

	public List<ISimulationEvent> jobArrive(IResourceGroup rg, IJob job, long time) {
		List<ISimulationEvent> events=new ArrayList<ISimulationEvent>();
		if (job instanceof IProductJob) {
			// single

			IStep pstep = job.getPreviousStep();
			IStep cstep = job.getCurrentStep(rg);

			if (cstep.getSplittingConfig() == null && cstep.getBatchingConfig() == null) {
				job.setReorganizedJobACurrentStep(false);
				if(cstep.getRequiredResourceGroup()==null){
					events.addAll(seizedResource(null,job,time));
				}else{
					rg.getFrontQueue().add(job);
					events.addAll(seizeResources(rg, job, time));
				}
				return events;
				

			} else if (pstep != null && (cstep.getSplittingConfig() == pstep.getSplittingConfig()
					|| cstep.getBatchingConfig() == pstep.getBatchingConfig())) {
				job.setReorganizedJobACurrentStep(false);
				//if (!) {
					if(rg.getJobTypeInQueue()==rg.getJobType()){
						rg.getFrontQueue().add(job);
					}else{
						rg.getFrontQueueWithOrganizedJobs().add(job);
					}
					events.addAll(seizeResources(rg, job, time));
				//}
				return events;
			}
			job.setReorganizedJobACurrentStep(true);

			// complete combining
			if (rg.getJobType().isCollectionOf(job.getType())) {
				rg.getFrontQueue().add(job);
				events.addAll(batching(rg, false,time));
			}
			// complete Splitting
			else if (job.getType().isCollectionOf(rg.getJobType())) {
				List<IJob> subJobs = job.getChildren();
				for(IJob ijob:subJobs){
					ijob.setPartialSplitting(false);
				}
				rg.getFrontQueue().addAll(subJobs);
				events.addAll(seizeResources(rg, subJobs, time));
			}

			else if (rg.getJobType().getChildType() == job.getType().getChildType()) {
				// partial splitting
				if (rg.getJobTypeInQueue() == job.getType()) {
					List<IJob> subJobs = splitJob(rg, job);
					rg.getFrontQueue().addAll(subJobs);

					events.addAll(seizeResources(rg, subJobs, time));
				}
				// partial combining
				else if (rg.getJobTypeInQueue() == job.getType().getChildType()) {
					rg.getFrontQueue().addAll(job.getChildren());
					events.addAll(batching(rg,true, time));
				} else {
					Log.e(tag, "cannot process the job");
					return events;
				}

			} else {
				Log.e(tag, "cannot process the job");
				return events;
			}
		} else

		{
			//if (!) {
				rg.getFrontQueue().add(job);
				events.addAll(seizeResources(rg, job, time));
			//}
		}
		return events;

	}

	private List<ISimulationEvent> seizeResources(IResourceGroup rg, List<IJob> jobs, long time) {
		List<ISimulationEvent> events=new ArrayList<ISimulationEvent>();
		for (IJob job : jobs) {
			if (!rg.hasIdleResource()) {
				break;
			}
			events.addAll(seizeResources(rg, job, time));
		}
		return events;

	}

	private List<ISimulationEvent> batching(IResourceGroup rg,boolean partial, long time) {
		List<ISimulationEvent> events=new ArrayList<ISimulationEvent>();
		if (!rg.hasIdleResource()) {
			return events;
		}

		if (rg.getFrontQueue().isEmpty()) {
			return events;
		}

		List<IJob> batches = new ArrayList<IJob>();
		
	
		batches.addAll(rg.getFrontQueueWithOrganizedJobs());

		for (IJob wafer : rg.getFrontQueue()) {
			boolean batched = false;

			for (IJob batch : batches) {
				if (batch.canBatch(wafer)) {
					batch.getChildren().add(wafer);
					batched = true;
					// System.out.println("bbbbbbbbbbbbb");
					break;
				}
			}
			if (batched) {
				continue;
			}

			IJob batch=null;
			try {
				batch = rg.getJobType().getJobClass().newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			BatchingConfiguration bconfig = wafer.getCurrentStep(rg).getBatchingConfig();

			batch.setBatchConfig(bconfig);
			batch.getChildren().add(wafer);
			batches.add(batch);

		}

		if (batches.isEmpty()) {
			// logDebug("no batches are available,"+getName());
			return events;
		}

		// int num=0;
		List<IResource> sortedFreeTools = rg.getIdleResources();
		ResourcePriorityUtil.sort(sortedFreeTools);

		for (IResource tool : sortedFreeTools) {

			if (batches.isEmpty()) {
				break;
			}

			List<IJob> batchesCanGo = new ArrayList<IJob>();
			for (IJob batch : batches) {
				if (batch.batchReadyToGo(tool, time)) {
					batchesCanGo.add(batch);
				}

			}

			if (batchesCanGo.isEmpty()) {
				// logInfo("Batching","no batches can go,"+toolGroup.getName());
				continue;
			}

			// List<Batch> sortedBatchesCanGo=sortBatches();

			IJob batch = JobPriorityUtil.getJobWithHighestPrioirty(batchesCanGo, tool);

			batches.remove(batch);
			batch.setPartialCombining(partial);
			//batch.setCompleteCombining(!partial);
			tool.seize();
			events.addAll(seizedResource(tool, batch, time));
			events.addAll(seizedResources(rg, batch));

			// batch.addAssignedResource(batch.getCurrentStep(), tool);

		}
		return events;

	}
	
	private void removeJobFromQueue(IResourceGroup rg, IJob job){
		if(rg.getJobType()==rg.getJobTypeInQueue()){
			rg.getFrontQueue().remove(job);
		}else if(rg.getJobType().isCollectionOf(rg.getJobTypeInQueue())){
			
			if(!rg.getFrontQueueWithOrganizedJobs().remove(job)){
				rg.getFrontQueue().remove(job.getChildren());
			}
		}else{
			Log.e(tag, "remove from queue error");
		}
	}

	private List<ISimulationEvent> seizedResources(IResourceGroup rg, IJob job) {
		List<ISimulationEvent> events=new ArrayList<ISimulationEvent>();
		removeJobFromQueue(rg,job);
		job.setCurrentStep(job.getCurrentStep(rg));
		//removeJobFromQueuesOfOtherAlternativeResourceGroup(rg,job);
		return events;
	}

	private List<ISimulationEvent> seizeResources(IResourceGroup rg, IJob job, long time) {
		List<ISimulationEvent> events=new ArrayList<ISimulationEvent>();
		List<IResource> iress = new ArrayList<IResource>();
		for (IResource res : rg.getResources()) {

			if (!res.isSeized() && res.canProcessJob(job)) {
				iress.add(res);

				double priority = ResourcePriorityUtil.getResourcePriority(res, job);
				res.setPriority(priority);

			}
		}

		// IStep step=job.getCurrentStep();
		int requiredResourceNum = job.getCurrentStep(rg).getRequiredResourceNum();

		if (iress.size() < requiredResourceNum) {
			return events;
		}

		ResourcePriorityUtil.sort(iress);

		for (int i = 0; i < requiredResourceNum; i++) {
			IResource selRes = iress.get(i);
			selRes.seize();

			selRes.setCurrentJob(job);
			// job.addAssignedResource(job.getCurrentStep(), selRes);
			events.addAll(seizedResource(selRes, job, time));

		}
		events.addAll(seizedResources(rg, job));
		return events;

	}

	private void removeJobFromQueuesOfOtherAlternativeResourceGroup(IResourceGroup rg,IJob job) {
		// TODO
		if (!job.isReorganizedJobInCurrentStep()) {
			removeJobFromQueue(job, job);

		} else if(job.getType().isCollectionOf(rg.getJobTypeInQueue())){
			if (job.fromPartialCombining()) {
				Set<IJob> removed = new HashSet<IJob>();
				for (IJob ijob : job.getChildren()) {
					IJob enterJob = ijob.getFatherBeforeReorganizedAtCurrentStep();
					if (!removed.contains(enterJob)) {
						removeJobFromQueue(enterJob, job);
						removed.add(enterJob);
					}
				}

			} else {
				for (IJob ijob : job.getChildren()) {
					removeJobFromQueue(ijob, job);
				}
			} 
		}else if(job.getType()==rg.getJobTypeInQueue()){
			if (job.fromPartialSplitting()) {
				IJob enterJob = job.getFatherAtCurrentStep();
				removeJobFromQueue(enterJob, job);
				

			} else  {
				IJob enterJob = job.getFatherAtCurrentStep();
				removeJobFromQueue(enterJob, job);

			} 
		}
		
		
		 else {
			Log.e(tag, "error in job type from ...");
			return;
		}

	}

	private void removeJobFromQueue(IJob enterJob, IJob job) {
		for (IStep step : enterJob.getCurrentSteps()) {
			if (step == job.getCurrentStep()) {
				continue;
			}
			if (step.getRequiredResourceGroup().getJobTypeInQueue() == enterJob.getType()) {
				step.getRequiredResourceGroup().getFrontQueue().remove(enterJob);
			} else if (job.getType().isCollectionOf(step.getRequiredResourceGroup().getJobTypeInQueue())) {
				step.getRequiredResourceGroup().getFrontQueue().removeAll(enterJob.getChildren());
			} else {
				Log.e(tag, "error in job type from ...");
			}

		}
	}

	private List<ISimulationEvent> seizedResource(IResource res, IJob job, long time) {
		List<ISimulationEvent> events=new ArrayList<ISimulationEvent>();
		if (res!=null&&res.hasPrepareJob()) {
			IJob prepareJob = res.getPrepareJob();
			prepareJob.addJobFinishListener(new JobFinishListener() {
				@Override
				public List<ISimulationEvent> onJobFinish(IJob job,long time) {
					List<ISimulationEvent> events=new ArrayList<ISimulationEvent>();
					job.oneResourceReady();
					if (job.isAllResourcesReady()) {
						events.addAll(startProcess(job, time));
					}
					return events;

				}
			});
			events.addAll(startJob(prepareJob, time));
		} else {
			job.oneResourceReady();
			if (job.isAllResourcesReady()) {
				events.addAll(startProcess(job, time));
			}
		}
		return events;

	}
	
	public List<ISimulationEvent>  oneResourceIdle(IResource res, long time) {
		List<ISimulationEvent> events=new ArrayList<ISimulationEvent>();
		if(res.getResourceGroup().getJobType()==res.getResourceGroup().getJobTypeInQueue()){
			events.addAll(oneResourceIdleNoOrg(res,time));
		}else{
			events.addAll(batching(res.getResourceGroup(),false,time));
		}
		return events;
	}

	public List<ISimulationEvent> oneResourceIdleNoOrg(IResource res, long time) {
		List<ISimulationEvent> events=new ArrayList<ISimulationEvent>();
		List<IJob> queue = res.getResourceGroup().getFrontQueue();
		if (queue.isEmpty()) {
			return events;
		}

		// List<IGeneralJob> interruptionJobs=new ArrayList<IGeneralJob>();

		List<IJob> jobs = new ArrayList<IJob>();
		for (IJob job : queue) {
			if (res.canProcessJob(job)) {
				jobs.add(job);

				double priority = JobPriorityUtil.getJobPriority(job, res);
				job.setPriority(priority);

			}

		}

		if (jobs.isEmpty()) {
			return events;
		}

		JobPriorityUtil.sort(jobs);

		// System.out.println(job.getName()+","+job.getPriority());
		// System.out.println(jobs);
		// this.getEngine().pause();

		for (IJob job : jobs) {

			// resources are not seized
			List<IResource> ress = new ArrayList<IResource>();

			for (IResource ires : res.getResourceGroup().getResources()) {

				if (!ires.isSeized()) {
					ress.add(ires);
				}
			}

			if (ress.isEmpty()) {
				break;
			}

			// resources can process the job
			List<IResource> ressj = new ArrayList<IResource>();

			for (IResource ires : ress) {

				if (ires.canProcessJob(job)) {
					ressj.add(res);

					double priority = ResourcePriorityUtil.getResourcePriority(res, job);
					res.setPriority(priority);

				}
			}

			int requiredResourceNum = job.getCurrentStep(res.getResourceGroup()).getRequiredResourceNum();

			if (ressj.size() < requiredResourceNum) {
				continue;
			}

			// sort resources
			if (ressj.size() > requiredResourceNum) {

				ResourcePriorityUtil.sort(ressj);

			}

			// seize resources
			for (int i = 0; i < requiredResourceNum; i++) {
				IResource selRes = ressj.get(i);
				selRes.seize();
				selRes.setCurrentJob(job);
				// job.addAssignedResource(job.getCurrentStep(), selRes);

				events.addAll(seizedResource(selRes, job, time));

			}

		}
		return events;

	}

	public List<ISimulationEvent> releasedResource(IResource res, long time) {
		List<ISimulationEvent> events=new ArrayList<ISimulationEvent>();
		if (res.hasInterruptionJob()) {
			IJob job = res.getInterruptionJob();
			events.addAll(startInterruptionJob(res, job, time));

		} else {
			events.addAll(oneResourceIdle(res, time));
		}
		return events;
	}

	public List<ISimulationEvent>  releaseResource(IResource res, long time) {
		List<ISimulationEvent> events=new ArrayList<ISimulationEvent>();
		if (res.hasCleanUpJob()) {
			IJob job = res.getCleanUpJob();
			job.addJobFinishListener(new JobFinishListener() {

				@Override
				public List<ISimulationEvent> onJobFinish(IJob job, long time) {
					List<ISimulationEvent> events=new ArrayList<ISimulationEvent>();
					res.release();
					events.addAll(releasedResource(res, time));
					return events;
				}
			});
			events.addAll(startJob(job, time));
		} else {
			res.release();
			events.addAll(releasedResource(res, time));
		}
		return events;
	}

	public List<ISimulationEvent>  releaseResources(IJob job, long time) {
		List<ISimulationEvent> events=new ArrayList<ISimulationEvent>();
		List<IResource> ress = job.getAssignedResources(job.getCurrentStep());
		if(ress==null){
			return events;
		}
		for (IResource res : ress) {
			events.addAll(releaseResource(res, time));
		}
		return events;
	}

	private void removeJobFromQueue(IJob job) {
		IResourceGroup rg = job.getCurrentStep().getRequiredResourceGroup();
		if(rg==null){
			return;
		}
		if (job.getType() == rg.getJobTypeInQueue()) {
			rg.getFrontQueue().remove(job);
		} else if (job.getType().isCollectionOf(rg.getJobTypeInQueue())) {
			rg.getFrontQueue().removeAll(job.getChildren());
		} else {
			Log.e(tag, "job type error");
			return;
		}
	}

	private List<ISimulationEvent> moveJobToNextStep(IJob job, long time) {
		List<ISimulationEvent> events=new ArrayList<ISimulationEvent>();
		Log.d(tag, "start step",time);
		if (job.goToNextStep()) {
			for (IStep step : job.getCurrentSteps()) {
				if(step.getRequiredResourceGroup()==null){
					job.setCurrentStep(step);
					events.addAll( startProcess(job,time));
				}else{
					events.addAll( jobArrive(step.getRequiredResourceGroup(), job, time));	
				}
				
			}

		} 
		else 
		{
			for (JobFinishListener lis : job.getFinishedListeners()) {
				events.addAll(lis.onJobFinish(job,time));
			}
		}
		return events;
	}

	private void reorganizeJobBefore() {
		// TODO

	}

	private void reorganizeJobAfter() {
		// TODO
	}

	private List<ISimulationEvent>  endProcess(IJob job, long time) {
		List<ISimulationEvent> events=new ArrayList<ISimulationEvent>();
		IResourceGroup rg = job.getCurrentStep().getRequiredResourceGroup();
		// single
		if (job.getType().isOriginalType()) {
			events.addAll(moveJobToNextStep(job, time));
		}

		// unit
		else if (job.getType() == rg.getJobTypeInQueue()) {
			job.getFather().oneChildrenDone();
			if (job.getFather().allChildrenDone()) {
				// job.getFather().goToNextStep();
				events.addAll(moveJobToNextStep(job.getFather(), time));
			}
		}
		// batch
		else {
			for (IJob ijob : job.getChildren()) {
				ijob.getFather().oneChildrenDone();
				if (ijob.getFather().allChildrenDone()) {
					events.addAll(moveJobToNextStep(ijob.getFather(), time));
				}
			}
		}
		return events;
	}

	protected List<ISimulationEvent> startProcess(IJob job, long time) {
		removeJobFromQueue(job);
		
		List<ISimulationEvent> events=new ArrayList<ISimulationEvent>();
		if (job.getCurrentStep().hasResourceJob()) {
			IJob resourceProcessJob = job.getCurrentStep().getResourceJob();
			resourceProcessJob.addJobFinishListener(new JobFinishListener() {

				@Override
				public List<ISimulationEvent>  onJobFinish(IJob resourceProcessJob, long time) {
					List<ISimulationEvent> events=new ArrayList<ISimulationEvent>();
					events.addAll(releaseResources(job, time));
					events.addAll(endProcess(job, time));
					return events;

				}
			});
			events.addAll(startJob(resourceProcessJob, time));

		} else {
			long processTime = job.getCurrentStep().getProcessTime();
			AbstractEvent event = new AbstractEvent() {

				@Override
				public List<ISimulationEvent> response(long currentTime) {
					List<ISimulationEvent> events=new ArrayList<ISimulationEvent>();
					events.addAll(releaseResources(job, currentTime));
					events.addAll(endProcess(job, currentTime));
					return events;
				}

			};
			event.setTime(processTime+job.getSimulation().getCurrentTime());
			events.add(event);
		}
		return events;
	}

	public List<ISimulationEvent>  startInterruptionJob(IResource res, IJob job, long time) {
		List<ISimulationEvent> events=new ArrayList<ISimulationEvent>();
		job.addJobFinishListener(new JobFinishListener() {

			@Override
			public List<ISimulationEvent> onJobFinish(IJob job, long time) {
				List<ISimulationEvent> events=new ArrayList<ISimulationEvent>();
				events.addAll(oneResourceIdle(res, time));
				long nextTime = res.getNextInterruptionTime();
				AbstractEvent event = new AbstractEvent() {


					@Override
					public List<ISimulationEvent> response(long currentTime) {
						List<ISimulationEvent> events=new ArrayList<ISimulationEvent>();
						events.addAll(resourceInterrupted(res, time));
						return events;
					}

				};
				event.setTime(nextTime+job.getSimulation().getCurrentTime());
				events.add(event);
				return events;

			}
		});

		events.addAll(startJob(job, time));
		return events;
	}

	public List<ISimulationEvent> resourceInterrupted(IResource res, long time) {
		List<ISimulationEvent> events=new ArrayList<ISimulationEvent>();
		if (res.hasInterruptionJob()) {
			return events;
		}
		if (!res.isSeized()) {

			events.addAll(startInterruptionJob(res, res.getInterruptionJob().clone(), time));

		} else {
			res.setInterruptionJob(res.getInterruptionJob().clone());
		}
		return events;
	}

	

}
