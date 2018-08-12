package com.tao.fab.sim.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import simulation.core.event.ISimulationEvent;
import simulation.core.event.SimulationEventType;

public class EventHandler {
	private static String tag = EventHandler.class.getName();

	public void releaseJob(SimulationEventList eventList, IProduct product, long time) {

		IProductJob pjob = product.getJob().clone();
		pjob.setProduct(product);

		pjob.addJobFinishListener(new JobFinishListener() {

			@Override
			public void onJobFinish(SimulationEventList eventList, IJob job, long time) {
				//// void events=new Arrayvoid();
				Log.d(tag, "job completed", time);
				// return;// events;

			}
		});


		if (!startJob(eventList, pjob, time)) {
			product.addJobToReleaseQueue(pjob);
		}

		if (!product.isReleaseQueueFull()) {

			long nextime = product.getTimeToNextRelease();
			AbstractEvent event = new AbstractEvent() {

				@Override
				public void response(SimulationEventList eventList, long currentTime) {
					releaseJob(eventList, product, time);
				}

			};
			eventList.add(event, nextime);
		}else{
			product.stopRelease();
		}

		// event.setTime(+product.getSimulation().getCurrentTime());

	}

	public boolean startJob(SimulationEventList eventList, IJob job, long time) {

		// return
		if (job.canNextStepAcceptMe()) {
			Log.d(tag, job.getName() + "started");
			moveJobToNextStep(eventList, job, time);
			return true;
		} else {
			Log.d(tag, job.getName() + "started");
			return false;
		}
		/*
		 * for (IStep step : job.getCurrentSteps()) {
		 * if(step.getRequiredResourceGroup()==null){
		 * Log.w(tag,"no resources specified at the step");
		 * job.setCurrentStep(step); AbstractEvent event = new AbstractEvent(){
		 * 
		 * @Override public void response(long currentTime) { Log.d(tag,
		 * "finish step",time); if(!job.goToNextStep()){ onJo }else{
		 * startJob(job,currentTime); }
		 * 
		 * 
		 * return null; }};
		 * event.setTime(job.getSimulation().getCurrentTime()+step.
		 * getProcessTime()); job.getSimulation().addEvent(event);
		 * 
		 * 
		 * } else{ jobArrive(step.getRequiredResourceGroup(), job, time); } }
		 */
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
				// njob.setCompleteSplitting(false);
				jobs.add(njob);
			}
			njob.getChildren().add(job.getChildren().get(i));
		}
		return jobs;
	}

	public void jobArrive(SimulationEventList eventList, IResourceGroup rg, IJob job, long time) {
		Log.d(tag, "job arrives at resourcegroup ");
		// void events=new Arrayvoid();
		if (job instanceof IProductJob) {
			// single

			IStep pstep = job.getPreviousStep();
			IStep cstep = job.getCurrentStep(rg);

			if (cstep.getSplittingConfig() == null && cstep.getBatchingConfig() == null) {
				job.setReorganizedJobACurrentStep(false);
				if (cstep.getRequiredResourceGroup() == null) {
					// events.addAll(
					seizedResource(eventList, null, job, time);// );
				} else {
					rg.getFrontQueue().add(job);
					// events.addAll(
					seizeResources(eventList, rg, job, time);// );
				}
				return;// events;

			} else if (pstep != null && (cstep.getSplittingConfig() == pstep.getSplittingConfig()
					|| cstep.getBatchingConfig() == pstep.getBatchingConfig())) {
				job.setReorganizedJobACurrentStep(false);
				// if (!) {
				if (rg.getJobTypeInQueue() == rg.getJobType()) {
					rg.getFrontQueue().add(job);
				} else {
					rg.getFrontQueueWithOrganizedJobs().add(job);
				}
				// events.addAll(
				seizeResources(eventList, rg, job, time);// );
				// }
				return;// events;
			}
			job.setReorganizedJobACurrentStep(true);

			// complete combining
			if (rg.getJobType().isCollectionOf(job.getType())) {
				rg.getFrontQueue().add(job);
				// events.addAll(
				batching(eventList, rg, false, time);// );
			}
			// complete Splitting
			else if (job.getType().isCollectionOf(rg.getJobType())) {
				List<IJob> subJobs = job.getChildren();
				for (IJob ijob : subJobs) {
					ijob.setPartialSplitting(false);
				}
				rg.getFrontQueue().addAll(subJobs);
				// events.addAll(
				seizeResources(eventList, rg, subJobs, time);// );
			}

			else if (rg.getJobType().getChildType() == job.getType().getChildType()) {
				// partial splitting
				if (rg.getJobTypeInQueue() == job.getType()) {
					List<IJob> subJobs = splitJob(rg, job);
					rg.getFrontQueue().addAll(subJobs);

					// events.addAll(
					seizeResources(eventList, rg, subJobs, time);// );
				}
				// partial combining
				else if (rg.getJobTypeInQueue() == job.getType().getChildType()) {
					rg.getFrontQueue().addAll(job.getChildren());
					// events.addAll(
					batching(eventList, rg, true, time);// );
				} else {
					Log.e(tag, "cannot process the job");
					return;// events;
				}

			} else {
				Log.e(tag, "cannot process the job");
				return;// events;
			}
		} else

		{
			// if (!) {
			rg.getFrontQueue().add(job);
			// events.addAll(
			seizeResources(eventList, rg, job, time);// );
			// }
		}
		return;// events;

	}

	private void seizeResources(SimulationEventList eventList, IResourceGroup rg, List<IJob> jobs, long time) {
		// void events=new Arrayvoid();
		for (IJob job : jobs) {
			if (!rg.hasIdleResource()) {
				break;
			}
			// events.addAll(
			seizeResources(eventList, rg, job, time);// );
		}
		return;// events;

	}

	private void batching(SimulationEventList eventList, IResourceGroup rg, boolean partial, long time) {
		// void events=new Arrayvoid();
		if (!rg.hasIdleResource()) {
			return;// events;
		}

		if (rg.getFrontQueue().isEmpty()) {
			return;// events;
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

			IJob batch = null;
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
			return;// events;
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
			// batch.setCompleteCombining(!partial);
			tool.seize();
			// events.addAll(
			seizedResource(eventList, tool, batch, time);// );
			// events.addAll(
			seizedResources(eventList, rg, batch, time);// );

			// batch.addAssignedResource(batch.getCurrentStep(), tool);

		}
		return;// events;

	}

	// reject job conditions
	// 1) buffer is full and in free resources no one can process job
	// 2)for transportation, see the destination

	public void onSuccResourceGroupQueueAvailable(SimulationEventList eventList, IResourceGroup rg,
			IResourceGroup sucrg, long time) {
		// TODO
		if (rg.hasRearQueue()) {
			if (rg.getRearMergQueue() != null) {
				for (IJob job : rg.getRearMergQueue()) {
					if (job.canNextStepAcceptMe()) {
						moveJobToNextStep(eventList, job, time);
						for (IJob ijob : job.getChildren()) {
							onJobExitRearQueue(eventList, ijob, time);
						}
					}
					if (sucrg.isFrontQueueFull()) {
						break;
					}
				}
			} else {
				for (IJob job : rg.getRearQueue()) {
					if (job.canNextStepAcceptMe()) {
						moveJobToNextStep(eventList, job, time);
						onJobExitRearQueue(eventList, job, time);
					}
					if (sucrg.isFrontQueueFull()) {
						break;
					}
				}
			}
		} else {
			for (IResource res : rg.getResources()) {

				if (!res.isBlocked()) {
					continue;
				}

				if (res.getCurrentJob().canNextStepAcceptMe()) {
					moveJobToNextStep(eventList, res.getCurrentJob(), time);
					res.unblock();
					if (res.getCurrentJob().canReleaseResourcesNow()) {
						releaseResources(eventList, res.getCurrentJob(), time);
					}
				}

				if (sucrg.isFrontQueueFull()) {
					break;
				}
			}

		}

	}

	private void removeJobFromQueue(SimulationEventList eventList, IResourceGroup rg, IJob job, long time) {
		// void events=new Arrayvoid();
		if (rg.getJobType() == rg.getJobTypeInQueue()) {
			rg.getFrontQueue().remove(job);
		} else if (rg.getJobType().isCollectionOf(rg.getJobTypeInQueue())) {

			if (!rg.getFrontQueueWithOrganizedJobs().remove(job)) {
				rg.getFrontQueue().remove(job.getChildren());
			}
		} else {
			Log.e(tag, "remove from queue error");
		}

		if(rg.getPreResourceGroups()!=null){
			for (IResourceGroup prerg : rg.getPreResourceGroups()) {
				onSuccResourceGroupQueueAvailable(eventList, prerg, rg,time);
				if (rg.isFrontQueueFull()) {
					break;
				}
			}
		}else if(job instanceof IProductJob){
			for(IProduct product:((IProductJob) job).getProduct().getAllProducts()){
				if (rg.isFrontQueueFull()) {
					break;
				}
				for(int i=0;i<product.getReleaseQueue().size();i++){
					if (rg.isFrontQueueFull()) {
						break;
					}
					IJob pjob=product.getReleaseQueue().get(i);
					if(startJob(eventList,pjob,time)){
						product.getReleaseQueue().remove(i);
						i--;
					}

				}
				if (product.isReleaseStopped()&&!product.isReleaseQueueFull()) {
					product.continueReleaseJob();
					long nextime = product.getTimeToNextRelease();
					AbstractEvent event = new AbstractEvent() {

						@Override
						public void response(SimulationEventList eventList, long currentTime) {
							releaseJob(eventList, product, time);
						}

					};
					eventList.add(event, nextime);
				}
				
			}
		}

		return;// events;
	}

	private void seizedResources(SimulationEventList eventList, IResourceGroup rg, IJob job, long time) {
		// void events=new Arrayvoid();
		removeJobFromQueue(eventList, rg, job, time);
		job.setCurrentStep(job.getCurrentStep(rg));
		// removeJobFromQueuesOfOtherAlternativeResourceGroup(rg,job);
		return;// events;
	}

	private void seizeResources(SimulationEventList eventList, IResourceGroup rg, IJob job, long time) {
		// void events=new Arrayvoid();
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
			return;// events;
		}

		ResourcePriorityUtil.sort(iress);

		// events.addAll(
		seizedResources(eventList, rg, job, time);// );
		for (int i = 0; i < requiredResourceNum; i++) {
			IResource selRes = iress.get(i);
			selRes.seize();

			selRes.setCurrentJob(job);
			// job.addAssignedResource(job.getCurrentStep(), selRes);
			// events.addAll(
			seizedResource(eventList, selRes, job, time);// );

		}

		return;// events;

	}

	private void removeJobFromQueuesOfOtherAlternativeResourceGroup(IResourceGroup rg, IJob job) {
		// TODO
		if (!job.isReorganizedJobInCurrentStep()) {
			removeJobFromQueue(job, job);

		} else if (job.getType().isCollectionOf(rg.getJobTypeInQueue())) {
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
		} else if (job.getType() == rg.getJobTypeInQueue()) {
			if (job.fromPartialSplitting()) {
				IJob enterJob = job.getFatherAtCurrentStep();
				removeJobFromQueue(enterJob, job);

			} else {
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

	private void seizedResource(SimulationEventList eventList, IResource res, IJob job, long time) {
		// void events=new Arrayvoid();
		if (res != null && res.hasPrepareJob()) {
			IJob prepareJob = res.getPrepareJob();
			prepareJob.addJobFinishListener(new JobFinishListener() {
				@Override
				public void onJobFinish(SimulationEventList eventList, IJob ijob, long time) {
					// void events=new Arrayvoid();
					job.oneResourceReady();
					if (job.isAllResourcesReady(res.getResourceGroup())) {
						// events.addAll(
						startProcess(eventList, job, res.getResourceGroup(), time);// );
					}
					return;// events;

				}
			});
			// events.addAll(
			startJob(eventList, prepareJob, time);// );
		} else {
			job.oneResourceReady();
			if (job.isAllResourcesReady(res.getResourceGroup())) {
				// events.addAll(
				startProcess(eventList, job, res.getResourceGroup(), time);// );
			}
		}
		return;// events;

	}

	public void oneResourceIdle(SimulationEventList eventList, IResource res, long time) {
		// void events=new Arrayvoid();
		// TODO if no front queue, deal with blocked job
		if (res.getResourceGroup().hasFrontQueue()) {
			if (res.getResourceGroup().getJobType() == res.getResourceGroup().getJobTypeInQueue()) {
				// events.addAll(
				oneResourceIdleNoOrg(eventList, res, time);// );
			} else {
				// events.addAll(
				batching(eventList, res.getResourceGroup(), false, time);// );
			}
		} else {

			for (IResourceGroup prerg : res.getResourceGroup().getPreResourceGroups()) {

				for (IResource ress : prerg.getResources()) {

					if (!ress.isBlocked()) {
						continue;
					}

					if (ress.getCurrentJob().canNextStepAcceptMe()) {
						moveJobToNextStep(eventList, ress.getCurrentJob(), time);
						ress.unblock();
						if (ress.getCurrentJob().canReleaseResourcesNow()) {
							releaseResources(eventList, ress.getCurrentJob(), time);
						}
					}

					if (res.getResourceGroup().isFrontQueueFull()) {
						break;
					}
				}
			}
		}
		return;// events;
	}

	public void oneResourceIdleNoOrg(SimulationEventList eventList, IResource res, long time) {
		// void events=new Arrayvoid();
		List<IJob> queue = res.getResourceGroup().getFrontQueue();
		if (queue.isEmpty()) {
			return;// events;
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
			return;// events;
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

				// events.addAll(
				seizedResource(eventList, selRes, job, time);// );

			}

		}
		return;// events;

	}

	public void releasedResource(SimulationEventList eventList, IResource res, long time) {
		// void events=new Arrayvoid();
		if (res.hasInterruptionJob()) {
			IJob job = res.getInterruptionJob();
			// events.addAll(
			startInterruptionJob(eventList, res, job, time);// );

		} else {
			// events.addAll(
			oneResourceIdle(eventList, res, time);// );
		}
		return;// events;
	}

	public void releaseResource(SimulationEventList eventList, IResource res, long time) {
		// void events=new Arrayvoid();
		if (res.hasCleanUpJob()) {
			IJob job = res.getCleanUpJob();
			job.addJobFinishListener(new JobFinishListener() {

				@Override
				public void onJobFinish(SimulationEventList eventList, IJob job, long time) {
					// void events=new Arrayvoid();
					res.release();
					// events.addAll(
					releasedResource(eventList, res, time);// );
					return;// events;
				}
			});
			// events.addAll(
			startJob(eventList, job, time);// );
		} else {
			res.release();
			// events.addAll(
			releasedResource(eventList, res, time);// );
		}
		return;// events;
	}

	public void releaseResources(SimulationEventList eventList, IJob job, long time) {
		// void events=new Arrayvoid();
		List<IResource> ress = job.getAssignedResources(job.getCurrentStep());
		if (ress == null) {
			return;// events;
		}
		for (IResource res : ress) {
			// events.addAll(
			releaseResource(eventList, res, time);// );
		}
		return;// events;
	}

	private void removeJobFromQueue(SimulationEventList eventList, IJob job, IResourceGroup rg) {
		// IResourceGroup rg = job.getCurrentStep().getRequiredResourceGroup();
		assert rg != null : "resource group is null";
		if (job.getType() == rg.getJobTypeInQueue()) {
			rg.getFrontQueue().remove(job);
		} else if (job.getType().isCollectionOf(rg.getJobTypeInQueue())) {
			rg.getFrontQueue().removeAll(job.getChildren());
		} else {
			Log.e(tag, "job type error");
			return;
		}
	}
	
	private void releaseAllResourcesSeizedByJob(SimulationEventList eventList, IJob job, long time){
		//TODO
	}

	private Random random=new Random();
	private void moveJobToNextStep(SimulationEventList eventList, IJob job, long time) {
		// void events=new Arrayvoid();
		releaseHeldResourcesByJob(eventList,job,time);
		if(job instanceof IProductJob){
			if(random.nextDouble()<job.getCurrentStep().getSampleRatio()){
				//scrap
				
				if(random.nextDouble()<job.getCurrentStep().getScrapRatio()){
					//scrap lot
					releaseAllResourcesSeizedByJob(eventList,job,time);
					return;
					
				}else{
					int scrapUnitNum=(int) (job.getChildren().size()*job.getCurrentStep().getUnitScrapRatio());
					for(int i=0;i<scrapUnitNum;i++){
						job.getChildren().remove(i);
					}
					
				}
				
				//reworking
				if(random.nextDouble()<job.getCurrentStep().getReworkingRatio()){
					//scrap lot
					releaseAllResourcesSeizedByJob(eventList,job,time);
					moveJobToStep(eventList,job,job.getCurrentStep().getReworkingStep(),time);
					return;
					
				}else{
					int scrapUnitNum=(int) (job.getChildren().size()*job.getCurrentStep().getUnitScrapRatio());
					IJob njob=new Job();
					//TODO config job
					for(int i=0;i<scrapUnitNum;i++){
						job.getChildren().remove(i);
						njob.getChildren().add(job.getChildren().get(i));
					}
					moveJobToStep(eventList,njob,job.getCurrentStep().getReworkingStep(),time);
				}
			}
		}

		if (job.goToNextStep()) {
			Log.d(tag, job.getName() + "starts step");
			for (IStep step : job.getCurrentSteps()) {
				if (step.getRequiredResourceGroup() == null) {
					job.setCurrentStep(step);
					// events.addAll(
					startProcess(eventList, job, null, time);// );
				} else {
					// events.addAll(
					jobArrive(eventList, step.getRequiredResourceGroup(), job, time);// );
				}

			}

		} else {
			for (JobFinishListener lis : job.getFinishedListeners()) {
				// events.addAll(
				lis.onJobFinish(eventList, job, time);// );
			}
		}
		return;// events;
	}

	private void releaseHeldResourcesByJob(SimulationEventList eventList, IJob job, long time) {
		// TODO Auto-generated method stub
		
	}

	private void moveJobToStep(SimulationEventList eventList, IJob job, IStep iStep, long time) {
		job.setCurrentStep(iStep);
		//for (IStep step : job.getCurrentSteps()) {
			if (iStep.getRequiredResourceGroup() == null) {
				job.setCurrentStep(iStep);
				// events.addAll(
				startProcess(eventList, job, null, time);// );
			} else {
				// events.addAll(
				jobArrive(eventList, iStep.getRequiredResourceGroup(), job, time);// );
			}

		//}

		
	}

	private void reorganizeJobBefore() {
		// TODO

	}

	private void reorganizeJobAfter() {
		// TODO
	}

	/*
	 * private void endProcess(SimulationEventList eventList, IJob job, long
	 * time) { // void events=new Arrayvoid(); IResourceGroup rg =
	 * job.getCurrentStep().getRequiredResourceGroup();
	 * 
	 * // otherr jobs, like prepare job if (job.getType() == null) { //
	 * events.addAll( moveJobToNextStep(eventList, job, time);// ); return;//
	 * events; }
	 * 
	 * // single if (job.getType().isOriginalType()) { // events.addAll(
	 * moveJobToNextStep(eventList, job, time);// ); }
	 * 
	 * // unit else if (job.getType() == rg.getJobTypeInQueue()) {
	 * job.getFather().oneChildrenDone(); if (job.getFather().allChildrenDone())
	 * { // job.getFather().goToNextStep(); // events.addAll(
	 * moveJobToNextStep(eventList, job.getFather(), time);// ); } } // batch
	 * else { for (IJob ijob : job.getChildren()) {
	 * ijob.getFather().oneChildrenDone(); if
	 * (ijob.getFather().allChildrenDone()) { // events.addAll(
	 * moveJobToNextStep(eventList, ijob.getFather(), time);// ); } } }
	 * return;// events; }
	 */

	protected void startProcess(SimulationEventList eventList, IJob job, IResourceGroup rg, long time) {
		if (rg != null) {
			removeJobFromQueue(eventList, job, rg);
		}

		//// void events=new Arrayvoid();
		if (job.getCurrentStep().hasResourceJob()) {
			IJob resourceProcessJob = job.getCurrentStep().getResourceJob();
			resourceProcessJob.addJobFinishListener(new JobFinishListener() {

				@Override
				public void onJobFinish(SimulationEventList eventList, IJob resourceProcessJob, long time) {
					onJobFinishStep(eventList, job, time);

				}
			});
			//// events.addAll(

			startJob(eventList, resourceProcessJob, time);// );

		} else {
			long processTime = job.getCurrentStep().getProcessTime();
			AbstractEvent event = new AbstractEvent() {

				// @Override
				public void response(SimulationEventList eventList, long currentTime) {
					//// void events=new Arrayvoid();
					onJobFinishStep(eventList, job, currentTime);

					return;// events;
				}

			};
			// event.setTime(processTime +
			// job.getSimulation().getCurrentTime());
			eventList.add(event, processTime);
		}
		return;// events;
	}

	private void onJobFinishStep(SimulationEventList eventList, IJob job, long time) {

		if (job.canNextStepAcceptMe()) {
			moveJobToNextStep(eventList, job, time);
			if (job.canReleaseResourcesNow()) {
				// events.addAll(
				releaseResources(eventList, job, time);// );
			}
		} else if (!job.getCurrentStep().getRequiredResourceGroup().isRearQueueFull()) {
			job.getCurrentStep().getRequiredResourceGroup().addJobToRearQueue(job);
			// events.addAll(
			onJobEnterRearQueue(eventList, job, time);// );
		} else {
			for (IResource res : job.getAssignedResources(job.getCurrentStep())) {
				res.block();
			}

			//// events.addAll(
			// releaseResources(eventList, job, currentTime);// );
			//// events.addAll(
			// endProcess(eventList, job, currentTime);// );
		}
	}

	private void onJobExitRearQueue(SimulationEventList eventList, IJob job, long time) {
		//// void events=new Arrayvoid();
		IResourceGroup rg = job.getCurrentStep().getRequiredResourceGroup();
		rg.removeJobFromRearQueue(job);
		for (IResource res : rg.getResources()) {
			if (rg.isRearQueueFull()) {
				break;
			}
			if (res.isBlocked()) {
				res.unblock();
				rg.addJobToRearQueue(res.getCurrentJob());
				//// events.addAll(

				onJobEnterRearQueue(eventList, res.getCurrentJob(), time);// );
				if (job.canReleaseResourcesNow()) {
					//// events.addAll(

					releaseResources(eventList, job, time);// );
				}
			}
		}
		// return;// events;
	}

	private void onJobEnterRearQueue(SimulationEventList eventList, IJob job, long time) {
		// TODO Auto-generated method stub

		//// void events=new Arrayvoid();
		IResourceGroup rg = job.getCurrentStep().getRequiredResourceGroup();

		// otherr jobs, like prepare job
		if (job.getType() == null) {
			//// events.addAll(
			// moveJobToNextStep(eventList, job, time);// );
			return;// events;
		}

		// single
		if (job.getType().isOriginalType()) {
			//// events.addAll(
			// moveJobToNextStep(eventList, job, time);// );
		}

		// unit
		else if (job.getType() == rg.getJobTypeInQueue()) {
			job.getFather().oneChildrenDone();
			if (job.getFather().allChildrenDone()) {
				// job.getFather().goToNextStep();
				//// events.addAll(
				// moveJobToNextStep(eventList, job.getFather(), time);// );
				if (job.getFather().canNextStepAcceptMe()) {
					//// events.addAll(

					moveJobToNextStep(eventList, job.getFather(), time);// );
					for (IJob child : job.getFather().getChildren()) {
						//// events.addAll(

						onJobExitRearQueue(eventList, child, time);// );
					}

				} else {
					rg.addJobToRearMergeQueue(job);
					for (IJob child : job.getFather().getChildren()) {
						//// events.addAll(
						rg.getRearQueue().remove(child);
						// onJobExitRearQueue(eventList, child, time);// );
					}
				}

			}
		}
		// batch
		else {
			for (IJob ijob : job.getChildren()) {
				ijob.getFather().oneChildrenDone();
				if (ijob.getFather().allChildrenDone()) {
					//// events.addAll(
					// moveJobToNextStep(eventList, ijob.getFather(), time);//
					//// );
					if (ijob.getFather().canNextStepAcceptMe()) {
						//// events.addAll(

						moveJobToNextStep(eventList, ijob.getFather(), time);// );
						for (IJob child : ijob.getFather().getChildren()) {
							//// events.addAll(

							onJobExitRearQueue(eventList, child, time);// );
						}
					} else {
						rg.addJobToRearMergeQueue(job);
						for (IJob child : job.getFather().getChildren()) {
							//// events.addAll(
							rg.getRearQueue().remove(child);
							// onJobExitRearQueue(eventList, child, time);// );
						}
					}

				}
			}
		}

		// return;// events;
	}

	public void startInterruptionJob(SimulationEventList eventList, IResource res, IJob job, long time) {
		//// void events=new Arrayvoid();
		job.addJobFinishListener(new JobFinishListener() {

			@Override
			public void onJobFinish(SimulationEventList eventList, IJob job, long time) {
				//// void events=new Arrayvoid();
				//// events.addAll(

				oneResourceIdle(eventList, res, time);// );
				long nextTime = res.getNextInterruptionTime();
				AbstractEvent event = new AbstractEvent() {

					public void response(SimulationEventList eventList, long currentTime) {
						//// void events=new Arrayvoid();
						//// events.addAll(

						resourceInterrupted(eventList, res, time);// );
						// return;// events;
					}

				};
				// event.setTime(nextTime +
				// job.getSimulation().getCurrentTime());
				eventList.add(event, nextTime);
				// return;// events;

			}
		});

		// //events.addAll(

		startJob(eventList, job, time);// );
		// return;// events;
	}

	public void resourceInterrupted(SimulationEventList eventList, IResource res, long time) {
		//// void events=new Arrayvoid();
		if (res.hasInterruptionJob()) {
			return;// events;
		}
		if (!res.isSeized()) {

			startInterruptionJob(eventList, res, res.getInterruptionJob().clone(), time);

		} else {
			res.setInterruptionJob(res.getInterruptionJob().clone());
		}
		// return;// events;
	}

}
