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
				
				Log.d(tag, "job completed", time);
				

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
		} else {
			product.stopRelease();
		}

		// event.setTime(+product.getSimulation().getCurrentTime());

	}

	public boolean startJob(SimulationEventList eventList, IJob job, long time) {

		if (job.canNextStepAcceptMe()) {
			Log.d(tag, job.getName() + "started");
			moveJobToNextStep(eventList, job, time);
			return true;
		} else {
			Log.d(tag, job.getName() + "started");
			return false;
		}

	}

	private List<IJob> splitJob(IResourceGroup rg, IJob job) {
		IStep step = job.getCurrentStep();
		ReorganizeJobConfig sconfig = step.getReorganizeConfig();
		int size = sconfig.getSplitSize();
		int num = job.getChildren().size() / size;
		List<IJob> jobs = new ArrayList<IJob>();

		IJob njob = job.newInstance();
		jobs.add(njob);
		for (int i = 0; i < job.getChildren().size(); i++) {
			if (i % size == 0) {
				njob = job.newInstance();
				jobs.add(njob);
			}
			njob.getChildren().add(job.getChildren().get(i));
		}
		return jobs;
	}

	public void onJobArrive(SimulationEventList eventList, IResourceGroup rg, IJob job, long time) {

		// if the resourcegroup has no front queue, what to do?

		if (!(job instanceof IProductJob)) {
			if (rg.hasFrontQueue()) {
				rg.getFrontQueue().add(job);
				
				seizeResources(eventList, rg, job, time);// );
			} else {
				if (!seizeResources(eventList, rg, job, time)) {
					Log.e(tag, "Something goes wrong");
				}
			}
			return;
		}
		// product job
		IStep pstep = job.getPreviousStep();
		IStep cstep = job.getCurrentStep();

		if (cstep.getReorganizeConfig() == null
				|| pstep != null && pstep.getReorganizeConfig() == cstep.getReorganizeConfig()) {
			if (rg.hasFrontQueue()) {
				rg.getFrontQueue().add(job);
				seizeResources(eventList, rg, job, time);// );
			} else {
				if (!seizeResources(eventList, rg, job, time)) {
					Log.e(tag, "Something goes wrong");
				}
			}
		} else {

			reorganizeJob(eventList, rg, job, time);
		}

	}

	private void reorganizeJob(SimulationEventList eventList, IResourceGroup rg, IJob job, long time) {
	
		ReorganizeJobConfig config = job.getCurrentStep().getReorganizeConfig();

		if (config.getReorganizeJobType() == ReorganizeJobType.complete_splitting) {
			rg.getFrontQueue().addAll(job.getChildren());
			seizeResources(eventList, rg, job.getChildren(), time);// );
		} else if (config.getReorganizeJobType() == ReorganizeJobType.complete_combining) {
			rg.getFrontReorganQueue().add(job);
			batching(eventList, rg, time);

		} else if (config.getReorganizeJobType() == ReorganizeJobType.partial_combining) {
			rg.getFrontReorganQueue().addAll(job.getChildren());
			batching(eventList, rg, time);

		} else if (config.getReorganizeJobType() == ReorganizeJobType.partial_splitting) {
			List<IJob> subJobs = splitJob(rg, job);
			rg.getFrontQueue().addAll(subJobs);
			seizeResources(eventList, rg, subJobs, time);
		}

	}



	private void seizeResources(SimulationEventList eventList, IResourceGroup rg, List<IJob> jobs, long time) {
		for (IJob job : jobs) {
			if (!rg.hasIdleResource()) {
				break;
			}
			
			seizeResources(eventList, rg, job, time);
		}
		return;

	}

	private void batching(SimulationEventList eventList, IResourceGroup rg, long time) {
		
		if (!rg.hasIdleResource()) {
			return;
		}

		if (rg.getFrontReorganQueue().isEmpty() && rg.getFrontQueue().isEmpty()) {
			return;
		}

		List<IJob> batches = new ArrayList<IJob>();

		batches.addAll(rg.getFrontQueue());

		for (IJob wafer : rg.getFrontReorganQueue()) {
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

			batch = new Job();

			ReorganizeJobConfig bconfig = wafer.getCurrentStep().getReorganizeConfig();

			batch.setReorganizeJobConfig(bconfig);
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
			// batch.setPartialCombining(partial);
			// batch.setCompleteCombining(!partial);
			tool.seize();
			
			seizedResource(eventList, tool, batch, time);// );
			
			// seizedResources(eventList, rg, batch, time);// );

			// batch.addAssignedResource(batch.getCurrentStep(), tool);

		}
		return;

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

	private void removeJobFromFrontQueue(SimulationEventList eventList, IResourceGroup rg, IJob job, long time) {
		
		if (job.getCurrentStep().getReorganizeConfig() == null
				|| job.getCurrentStep().getReorganizeConfig()
						.getReorganizeJobType() == ReorganizeJobType.partial_splitting
				|| job.getCurrentStep().getReorganizeConfig()
						.getReorganizeJobType() == ReorganizeJobType.complete_splitting) {
			rg.getFrontQueue().remove(job);
		} else if (job.getCurrentStep().getReorganizeConfig()
				.getReorganizeJobType() == ReorganizeJobType.complete_combining
				|| job.getCurrentStep().getReorganizeConfig()
						.getReorganizeJobType() == ReorganizeJobType.partial_combining) {
			rg.getFrontQueue().remove(job);
			if (job.isReorganizedAtCurrentStep()) {

				rg.getFrontReorganQueue().removeAll(job.getChildren());
			}

		} else {
			Log.e(tag, "remove from queue error");
		}

		if (rg.getPreResourceGroups() != null) {
			for (IResourceGroup prerg : rg.getPreResourceGroups()) {
				onSuccResourceGroupQueueAvailable(eventList, prerg, rg, time);
				if (rg.isFrontQueueFull()) {
					break;
				}
			}
		} else if (job instanceof IProductJob) {
			for (IProduct product : ((IProductJob) job).getProduct().getAllProducts()) {
				if (rg.isFrontQueueFull()) {
					break;
				}
				for (int i = 0; i < product.getReleaseQueue().size(); i++) {
					if (rg.isFrontQueueFull()) {
						break;
					}
					IJob pjob = product.getReleaseQueue().get(i);
					if (startJob(eventList, pjob, time)) {
						product.getReleaseQueue().remove(i);
						i--;
					}

				}
				if (product.isReleaseStopped() && !product.isReleaseQueueFull()) {
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

		return;
	}

	/*
	 * private void seizedResources(SimulationEventList eventList,
	 * IResourceGroup rg, IJob job, long time) { 
	 * removeJobFromQueue(eventList, rg, job, time);
	 * job.setCurrentStep(job.getCurrentStep()); //
	 * removeJobFromQueuesOfOtherAlternativeResourceGroup(rg,job); return;//
	 * events; }
	 */

	private boolean seizeResources(SimulationEventList eventList, IResourceGroup rg, IJob job, long time) {
		
		List<IResource> iress = new ArrayList<IResource>();
		for (IResource res : rg.getResources()) {

			if (!res.isSeized() && res.canProcessJob(job)) {
				iress.add(res);

				double priority = ResourcePriorityUtil.getResourcePriority(res, job);
				res.setPriority(priority);

			}
		}

		// IStep step=job.getCurrentStep();
		int requiredResourceNum = job.getCurrentStep().getRequiredResourceNum();

		if (iress.size() < requiredResourceNum) {
			return false;
		}

		ResourcePriorityUtil.sort(iress);

		
		// seizedResources(eventList, rg, job, time);// );
		for (int i = 0; i < requiredResourceNum; i++) {
			IResource selRes = iress.get(i);
			selRes.seize();

			selRes.setCurrentJob(job);
			// job.addAssignedResource(job.getCurrentStep(), selRes);
			
			seizedResource(eventList, selRes, job, time);// );

		}

		return true;

	}



	private void seizedResource(SimulationEventList eventList, IResource res, IJob job, long time) {
		
		if (res != null && res.hasPrepareJob()) {
			IJob prepareJob = res.getPrepareJob();
			prepareJob.addJobFinishListener(new JobFinishListener() {
				@Override
				public void onJobFinish(SimulationEventList eventList, IJob ijob, long time) {
					
					job.oneResourceReady();
					if (job.isAllResourcesReady()) {
						
						startProcess(eventList, job, res.getResourceGroup(), time);// );
					}
					return;

				}
			});
			
			startJob(eventList, prepareJob, time);// );
		} else {
			job.oneResourceReady();
			if (job.isAllResourcesReady()) {
				
				startProcess(eventList, job, res.getResourceGroup(), time);// );
			}
		}
		return;

	}

	public void oneResourceIdle(SimulationEventList eventList, IResource res, long time) {
		
		// TODO if no front queue, deal with blocked job
		if (res.getResourceGroup().hasFrontQueue()) {
			if (res.getProcessType() != ProcessType.Batch) {
				
				oneResourceIdleNoOrg(eventList, res, time);// );
			} else {
				
				batching(eventList, res.getResourceGroup(), time);// );
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
		return;
	}

	public void oneResourceIdleNoOrg(SimulationEventList eventList, IResource res, long time) {
		
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

			int requiredResourceNum = job.getCurrentStep().getRequiredResourceNum();

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

				
				seizedResource(eventList, selRes, job, time);// );

			}

		}
		return;

	}

	public void releasedResource(SimulationEventList eventList, IResource res, long time) {
		
		if (res.hasInterruptionJob()) {
			IJob job = res.getInterruptionJob();
			
			startInterruptionJob(eventList, res, job, time);// );

		} else {
			
			oneResourceIdle(eventList, res, time);// );
		}
		return;
	}

	public void releaseResource(SimulationEventList eventList, IResource res, long time) {
		
		if (res.hasCleanUpJob()) {
			IJob job = res.getCleanUpJob();
			job.addJobFinishListener(new JobFinishListener() {

				@Override
				public void onJobFinish(SimulationEventList eventList, IJob job, long time) {
					
					res.release();
					
					releasedResource(eventList, res, time);// );
					return;
				}
			});
			
			startJob(eventList, job, time);// );
		} else {
			res.release();
			
			releasedResource(eventList, res, time);// );
		}
		return;
	}

	public void releaseResources(SimulationEventList eventList, IJob job, long time) {
		
		List<IResource> ress = job.getAssignedResources(job.getCurrentStep());
		if (ress == null) {
			return;
		}
		for (IResource res : ress) {
			
			releaseResource(eventList, res, time);// );
		}
		return;
	}

	/*
	 * private void removeJobFromQueue(SimulationEventList eventList, IJob job,
	 * IResourceGroup rg) { // IResourceGroup rg =
	 * job.getCurrentStep().getRequiredResourceGroup(); assert rg != null :
	 * "resource group is null"; if (job.getType() == rg.getJobTypeInQueue()) {
	 * rg.getFrontQueue().remove(job); } else if
	 * (job.getType().isCollectionOf(rg.getJobTypeInQueue())) {
	 * rg.getFrontQueue().removeAll(job.getChildren()); } else { Log.e(tag,
	 * "job type error"); return; } }
	 */

	private void releaseAllResourcesSeizedByJob(SimulationEventList eventList, IJob job, long time) {
		// TODO
	}

	private Random random = new Random();

	private void moveJobToNextStep(SimulationEventList eventList, IJob job, long time) {
		
		releaseHeldResourcesByJob(eventList, job, time);
		if (job instanceof IProductJob) {
			if (random.nextDouble() < job.getCurrentStep().getSampleRatio()) {
				// scrap

				if (random.nextDouble() < job.getCurrentStep().getScrapRatio()) {
					// scrap lot
					releaseAllResourcesSeizedByJob(eventList, job, time);
					return;

				} else {
					int scrapUnitNum = (int) (job.getChildren().size() * job.getCurrentStep().getUnitScrapRatio());
					for (int i = 0; i < scrapUnitNum; i++) {
						job.getChildren().remove(i);
					}

				}

				// reworking
				if (random.nextDouble() < job.getCurrentStep().getReworkingRatio()) {
					// scrap lot
					releaseAllResourcesSeizedByJob(eventList, job, time);
					moveJobToStep(eventList, job, job.getCurrentStep().getReworkingStep(), time);
					return;

				} else {
					int scrapUnitNum = (int) (job.getChildren().size() * job.getCurrentStep().getUnitScrapRatio());
					IJob njob = new Job();
					// TODO config job
					for (int i = 0; i < scrapUnitNum; i++) {
						job.getChildren().remove(i);
						njob.getChildren().add(job.getChildren().get(i));
					}
					moveJobToStep(eventList, njob, job.getCurrentStep().getReworkingStep(), time);
				}
			}
		}

		if (job.goToNextStep()) {
			Log.d(tag, job.getName() + "starts step");
			IStep step = StepUtils.select(job.getCurrentSteps());
			// for (IStep step : job.getCurrentSteps()) {
			if (step.getRequiredResourceGroup() == null) {
				job.setCurrentStep(step);
				
				startProcess(eventList, job, null, time);// );
			} else {
				
				onJobArrive(eventList, step.getRequiredResourceGroup(), job, time);// );
			}

			// }

		} else {
			for (JobFinishListener lis : job.getFinishedListeners()) {
				
				lis.onJobFinish(eventList, job, time);// );
			}
		}
		return;
	}

	private void releaseHeldResourcesByJob(SimulationEventList eventList, IJob job, long time) {
		// TODO Auto-generated method stub

	}

	private void moveJobToStep(SimulationEventList eventList, IJob job, IStep iStep, long time) {
		job.setCurrentStep(iStep);
		// for (IStep step : job.getCurrentSteps()) {
		if (iStep.getRequiredResourceGroup() == null) {
			job.setCurrentStep(iStep);
			
			startProcess(eventList, job, null, time);// );
		} else {
			
			onJobArrive(eventList, iStep.getRequiredResourceGroup(), job, time);// );
		}

		// }

	}

	private void reorganizeJobBefore() {
		// TODO

	}

	private void reorganizeJobAfter() {
		// TODO
	}

	/*
	 * private void endProcess(SimulationEventList eventList, IJob job, long
	 * time) {  IResourceGroup rg =
	 * job.getCurrentStep().getRequiredResourceGroup();
	 * 
	 * // otherr jobs, like prepare job if (job.getType() == null) { //
	 * events.addAll( moveJobToNextStep(eventList, job, time);// ); return;//
	 * events; }
	 * 
	 * // single if (job.getType().isOriginalType()) { 
	 * moveJobToNextStep(eventList, job, time);// ); }
	 * 
	 * // unit else if (job.getType() == rg.getJobTypeInQueue()) {
	 * job.getFather().oneChildrenDone(); if (job.getFather().allChildrenDone())
	 * { // job.getFather().goToNextStep(); 
	 * moveJobToNextStep(eventList, job.getFather(), time);// ); } } // batch
	 * else { for (IJob ijob : job.getChildren()) {
	 * ijob.getFather().oneChildrenDone(); if
	 * (ijob.getFather().allChildrenDone()) { 
	 * moveJobToNextStep(eventList, ijob.getFather(), time);// ); } } }
	 * return; }
	 */

	protected void startProcess(SimulationEventList eventList, IJob job, IResourceGroup rg, long time) {
		if (rg != null) {
			removeJobFromFrontQueue(eventList, rg, job, time);
		}

		
		if (job.getCurrentStep().hasResourceJob()) {
			IJob resourceProcessJob = job.getCurrentStep().getResourceJob();
			resourceProcessJob.addJobFinishListener(new JobFinishListener() {

				@Override
				public void onJobFinish(SimulationEventList eventList, IJob resourceProcessJob, long time) {
					onJobFinishStep(eventList, job, time);

				}
			});
			//

			startJob(eventList, resourceProcessJob, time);// );

		} else {
			long processTime = job.getCurrentStep().getProcessTime();
			AbstractEvent event = new AbstractEvent() {

				// @Override
				public void response(SimulationEventList eventList, long currentTime) {
					
					onJobFinishStep(eventList, job, currentTime);

					return;
				}

			};
			// event.setTime(processTime +
			// job.getSimulation().getCurrentTime());
			eventList.add(event, processTime);
		}
		return;
	}

	private void onJobFinishStep(SimulationEventList eventList, IJob job, long time) {
		IResourceGroup rg = job.getCurrentStep().getRequiredResourceGroup();
		if (job.canNextStepAcceptMe()) {
			moveJobToNextStep(eventList, job, time);
			if (job.canReleaseResourcesNow()) {
				releaseResources(eventList, job, time);// );
			}
		} else if (rg != null && !rg.hasRearQueue() && !rg.isRearQueueFull()) {
			rg.addJobToRearQueue(job);
			onJobEnterRearQueue(eventList, job, time);// );
		} else {
			for (IResource res : job.getAssignedResources(job.getCurrentStep())) {
				res.block();
			}

			//
			// releaseResources(eventList, job, currentTime);// );
			//
			// endProcess(eventList, job, currentTime);// );
		}
	}

	private void onJobExitRearQueue(SimulationEventList eventList, IJob job, long time) {
		
		IResourceGroup rg = job.getCurrentStep().getRequiredResourceGroup();
		rg.removeJobFromRearQueue(job);
		for (IResource res : rg.getResources()) {
			if (rg.isRearQueueFull()) {
				break;
			}
			if (res.isBlocked()) {
				res.unblock();
				rg.addJobToRearQueue(res.getCurrentJob());
				//

				onJobEnterRearQueue(eventList, res.getCurrentJob(), time);// );
				if (job.canReleaseResourcesNow()) {
					//

					releaseResources(eventList, job, time);// );
				}
			}
		}
		
	}

	private void onJobEnterRearQueue(SimulationEventList eventList, IJob job, long time) {
		if (!(job instanceof IProductJob)) {
			return;
		}
		IResourceGroup rg = job.getCurrentStep().getRequiredResourceGroup();
		IStep nstep = job.getNextStep();
		IStep cstep = job.getCurrentStep();
		if (cstep.getReorganizeConfig() == null
				|| nstep != null && nstep.getReorganizeConfig() == cstep.getReorganizeConfig()) {
			return;
		}
		recoverOrganizedJob(eventList, job, time);

	}

	private void recoverOrganizedJob(SimulationEventList eventList, IJob job, long time) {

		ReorganizeJobConfig reorgconfig = job.getCurrentStep().getReorganizeConfig();
		IResourceGroup rg = job.getCurrentStep().getRequiredResourceGroup();
		ReorganizeJobType type = reorgconfig.getReorganizeJobType();
		if (type == ReorganizeJobType.complete_splitting) {
			job.getFather().oneChildrenDone();
			if (job.getFather().allChildrenDone()) {
				// job.getFather().goToNextStep();
				//
				// moveJobToNextStep(eventList, job.getFather(), time);// );
				if (job.getFather().canNextStepAcceptMe()) {
					//

					moveJobToNextStep(eventList, job.getFather(), time);// );
					for (IJob child : job.getFather().getChildren()) {
						//

						onJobExitRearQueue(eventList, child, time);// );
					}

				} else {
					rg.addJobToRearMergeQueue(job);
					for (IJob child : job.getFather().getChildren()) {
						//
						rg.getRearQueue().remove(child);
						// onJobExitRearQueue(eventList, child, time);// );
					}
				}

			}
		} else if (type == ReorganizeJobType.complete_combining) {
			for (IJob child : job.getChildren()) {
				if (child.canNextStepAcceptMe()) {
					//

					moveJobToNextStep(eventList, child, time);// );
					// onJobExitRearQueue(eventList, child, time);// );

				} else {
					rg.addJobToRearMergeQueue(job);

				}
			}
			rg.getRearQueue().remove(job);
		} else if (type == ReorganizeJobType.partial_splitting || type == ReorganizeJobType.partial_combining) {
			for (IJob child : job.getChildren()) {
				child.getFather().oneChildrenDone();
				// job.getChildren().remove(child);
				if (child.getFather().allChildrenDone()) {
					if (child.getFather().canNextStepAcceptMe()) {

						moveJobToNextStep(eventList, child.getFather(), time);// );

					} else {
						rg.addJobToRearMergeQueue(child.getFather());
					}
					for (IJob ichild : child.getFather().getChildren()) {
						//
						ichild.getCurrentFather().oneChildrenDone();
						if (ichild.getCurrentFather().allChildrenDone()) {
							onJobExitRearQueue(eventList, ichild.getCurrentFather(), time);// );
						}

					}
				}
			}
		}

	}

	public void startInterruptionJob(SimulationEventList eventList, IResource res, IJob job, long time) {
		
		job.addJobFinishListener(new JobFinishListener() {

			@Override
			public void onJobFinish(SimulationEventList eventList, IJob job, long time) {
				
				//

				oneResourceIdle(eventList, res, time);// );
				long nextTime = res.getNextInterruptionTime();
				AbstractEvent event = new AbstractEvent() {

					public void response(SimulationEventList eventList, long currentTime) {
						
						//

						resourceInterrupted(eventList, res, time);// );
						
					}

				};
				// event.setTime(nextTime +
				// job.getSimulation().getCurrentTime());
				eventList.add(event, nextTime);
				

			}
		});

		

		startJob(eventList, job, time);// );
		
	}

	public void resourceInterrupted(SimulationEventList eventList, IResource res, long time) {
		
		if (res.hasInterruptionJob()) {
			return;
		}
		if (!res.isSeized()) {

			startInterruptionJob(eventList, res, res.getInterruptionJob().clone(), time);

		} else {
			res.setInterruptionJob(res.getInterruptionJob().clone());
		}
		
	}

}
