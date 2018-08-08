package com.tao.fab.sim.event;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import simulation.core.event.ISimulationEvent;

public class EventHandler {
	private static String tag = EventHandler.class.getName();

	public void releaseJob(IProduct product, long time) {

		IProductJob pjob = product.getJob().clone();
		pjob.setProduct(product);

		long nextime = product.getTimeToNextRelease();
		AbstractEvent event = new AbstractEvent() {


			@Override
			public List<ISimulationEvent> response(long currentTime) {
				releaseJob(product, time);
				return null;
			}

		};
		event.setTime(nextime+product.getSimulation().getCurrentTime());
		product.getSimulation().addEvent(event);;
	

		pjob.addJobFinishListener(new JobFinishListener() {

			@Override
			public void onJobFinish(IJob job) {
				System.out.println("job complete");

			}
		});

		startJob(pjob, time);
	}

	public void startJob(IJob job, long time) {
		for (IStep step : job.getCurrentSteps()) {
			jobArrive(step.getRequiredResourceGroup(), job, time);
		}
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

	public void jobArrive(IResourceGroup rg, IJob job, long time) {
		if (job instanceof IProductJob) {
			// single

			IStep pstep = job.getPreviousStep();
			IStep cstep = job.getCurrentStep(rg);

			if (cstep.getSplittingConfig() == null && cstep.getBatchingConfig() == null) {
				job.setReorganizedJobACurrentStep(false);
				if(cstep.getRequiredResourceGroup()==null){
					seizedResource(null,job,time);
				}
				if (!seizeResources(rg, job, time)) {
					rg.getFrontQueue().add(job);
				}
				return;
			} else if (pstep != null && (cstep.getSplittingConfig() == pstep.getSplittingConfig()
					|| cstep.getBatchingConfig() == pstep.getBatchingConfig())) {
				job.setReorganizedJobACurrentStep(false);
				if (!seizeResources(rg, job, time)) {
					if(rg.getJobTypeInQueue()==rg.getJobType()){
						rg.getFrontQueue().add(job);
					}else{
						rg.getFrontQueueWithOrganizedJobs().add(job);
					}
				}
				return;
			}
			job.setReorganizedJobACurrentStep(true);

			// complete combining
			if (rg.getJobType().isCollectionOf(job.getType())) {
				rg.getFrontQueue().add(job);
				batching(rg, false,time);
			}
			// complete Splitting
			else if (job.getType().isCollectionOf(rg.getJobType())) {
				List<IJob> subJobs = job.getChildren();
				for(IJob ijob:subJobs){
					ijob.setPartialSplitting(false);
				}
				rg.getFrontQueue().addAll(subJobs);
				seizeResources(rg, subJobs, time);
			}

			else if (rg.getJobType().getChildType() == job.getType().getChildType()) {
				// partial splitting
				if (rg.getJobTypeInQueue() == job.getType()) {
					List<IJob> subJobs = splitJob(rg, job);
					rg.getFrontQueue().addAll(subJobs);

					seizeResources(rg, subJobs, time);
				}
				// partial combining
				else if (rg.getJobTypeInQueue() == job.getType().getChildType()) {
					rg.getFrontQueue().addAll(job.getChildren());
					batching(rg,true, time);
				} else {
					Log.e(tag, "cannot process the job");
					return;
				}

			} else {
				Log.e(tag, "cannot process the job");
				return;
			}
		} else

		{
			if (!seizeResources(rg, job, time)) {
				rg.getFrontQueue().add(job);
			}
		}

	}

	private void seizeResources(IResourceGroup rg, List<IJob> jobs, long time) {
		for (IJob job : jobs) {
			if (!rg.hasIdleResource()) {
				break;
			}
			seizeResources(rg, job, time);
		}

	}

	private void batching(IResourceGroup rg,boolean partial, long time) {

		if (!rg.hasIdleResource()) {
			return;
		}

		if (rg.getFrontQueue().isEmpty()) {
			return;
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
			return;
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
			seizedResource(tool, batch, time);
			seizedResources(rg, batch);

			// batch.addAssignedResource(batch.getCurrentStep(), tool);

		}

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

	private void seizedResources(IResourceGroup rg, IJob job) {
		removeJobFromQueue(rg,job);
		job.setCurrentStep(job.getCurrentStep(rg));
		//removeJobFromQueuesOfOtherAlternativeResourceGroup(rg,job);
	}

	private boolean seizeResources(IResourceGroup rg, IJob job, long time) {
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
			return false;
		}

		ResourcePriorityUtil.sort(iress);

		for (int i = 0; i < requiredResourceNum; i++) {
			IResource selRes = iress.get(i);
			selRes.seize();

			selRes.setCurrentJob(job);
			// job.addAssignedResource(job.getCurrentStep(), selRes);
			seizedResource(selRes, job, time);

		}
		seizedResources(rg, job);
		return true;

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

	private void seizedResource(IResource res, IJob job, long time) {
		if (res!=null&&res.hasPrepareJob()) {
			IJob prepareJob = res.getPrepareJob();
			prepareJob.addJobFinishListener(new JobFinishListener() {
				@Override
				public void onJobFinish(IJob job) {
					job.oneResourceReady();
					if (job.isAllResourcesReady()) {
						startProcess(job, time);
					}

				}
			});
			startJob(prepareJob, time);
		} else {
			job.oneResourceReady();
			if (job.isAllResourcesReady()) {
				startProcess(job, time);
			}
		}

	}
	
	public void oneResourceIdle(IResource res, long time) {
		if(res.getResourceGroup().getJobType()==res.getResourceGroup().getJobTypeInQueue()){
			oneResourceIdleNoOrg(res,time);
		}else{
			batching(res.getResourceGroup(),false,time);
		}
	}

	public void oneResourceIdleNoOrg(IResource res, long time) {
		List<IJob> queue = res.getResourceGroup().getFrontQueue();
		if (queue.isEmpty()) {
			return;
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
			return;
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

				seizedResource(selRes, job, time);

			}

		}

	}

	public void releasedResource(IResource res, long time) {
		if (res.hasInterruptionJob()) {
			IJob job = res.getInterruptionJob();
			startInterruptionJob(res, job, time);

		} else {
			oneResourceIdle(res, time);
		}
	}

	public void releaseResource(IResource res, long time) {
		if (res.hasCleanUpJob()) {
			IJob job = res.getCleanUpJob();
			job.addJobFinishListener(new JobFinishListener() {

				@Override
				public void onJobFinish(IJob job) {
					res.release();
					releasedResource(res, time);

				}
			});
			startJob(job, time);
		} else {
			res.release();
			releasedResource(res, time);
		}
	}

	public void releaseResources(IJob job, long time) {
		List<IResource> ress = job.getAssignedResources(job.getCurrentStep());
		for (IResource res : ress) {
			releaseResource(res, time);
		}
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

	private void moveJobToNextStep(IJob job, long time) {
		if (job.goToNextStep()) {
			for (IStep step : job.getCurrentSteps()) {
				jobArrive(step.getRequiredResourceGroup(), job, time);
			}

		} else {
			for (JobFinishListener lis : job.getFinishedListeners()) {
				lis.onJobFinish(job);
			}
		}
	}

	private void reorganizeJobBefore() {
		// TODO

	}

	private void reorganizeJobAfter() {
		// TODO
	}

	private void endProcess(IJob job, long time) {
		IResourceGroup rg = job.getCurrentStep().getRequiredResourceGroup();
		// single
		if (job.getType().isOriginalType()) {
			moveJobToNextStep(job, time);
		}

		// unit
		else if (job.getType() == rg.getJobTypeInQueue()) {
			job.getFather().oneChildrenDone();
			if (job.getFather().allChildrenDone()) {
				// job.getFather().goToNextStep();
				moveJobToNextStep(job.getFather(), time);
			}
		}
		// batch
		else {
			for (IJob ijob : job.getChildren()) {
				ijob.getFather().oneChildrenDone();
				if (ijob.getFather().allChildrenDone()) {
					moveJobToNextStep(ijob.getFather(), time);
				}
			}
		}
	}

	protected void startProcess(IJob job, long time) {
		removeJobFromQueue(job);

		if (job.getCurrentStep().hasResourceJob()) {
			IJob resourceProcessJob = job.getCurrentStep().getResourceJob();
			resourceProcessJob.addJobFinishListener(new JobFinishListener() {

				@Override
				public void onJobFinish(IJob resourceProcessJob) {
					releaseResources(job, time);
					endProcess(job, time);

				}
			});
			startJob(resourceProcessJob, time);

		} else {
			long processTime = job.getCurrentStep().getProcessTime();
			AbstractEvent event = new AbstractEvent() {

				@Override
				public List<ISimulationEvent> response(long currentTime) {
					releaseResources(job, time);
					endProcess(job, time);
					return null;
				}

			};
			event.setTime(processTime+job.getSimulation().getCurrentTime());
			job.getSimulation().addEvent(event);
		}
	}

	public void startInterruptionJob(IResource res, IJob job, long time) {
		job.addJobFinishListener(new JobFinishListener() {

			@Override
			public void onJobFinish(IJob job) {
				oneResourceIdle(res, time);
				long nextTime = res.getNextInterruptionTime();
				AbstractEvent event = new AbstractEvent() {


					@Override
					public List<ISimulationEvent> response(long currentTime) {
						resourceInterrupted(res, time);
						return null;
					}

				};
				event.setTime(nextTime+job.getSimulation().getCurrentTime());
				job.getSimulation().addEvent(event);

			}
		});

		startJob(job, time);
	}

	public void resourceInterrupted(IResource res, long time) {
		if (res.hasInterruptionJob()) {
			return;
		}
		if (!res.isSeized()) {

			startInterruptionJob(res, res.getInterruptionJob().clone(), time);

		} else {
			res.setInterruptionJob(res.getInterruptionJob().clone());
		}
	}

	

}
