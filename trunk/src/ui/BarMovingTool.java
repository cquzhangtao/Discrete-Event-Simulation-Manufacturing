package ui;

import java.awt.Cursor;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.gantt.XYTaskDataset;
import org.jfree.data.time.SimpleTimePeriod;

import common.IEntity;

import enterprise.materialflow.model.plant.resource.IResource;
import enterprise.materialflow.model.product.activity.ProcessActivity;

public class BarMovingTool {

	private double preX, preY, initialY;
	private double barHeight,barWidth;
	private Task pointedTask;
	private Task selectedTask;
	private double scale;
	private XYTaskDataset dataset;
	private int state = 0;
	private TaskSeries pointedTaskTaskSeries;
	private int item = 0;
	private long oldstart;
	private long oldend;
	private long moved;
	private List<ChartChangeListener> chartlistener;
	private SequenceChangeListener seqChangeListener;
	private AllocationChangeListener alloChangeListener;
	private boolean deleteable = false;
	private boolean adjustable = false;
	protected int offsetx;
	protected int offsety;
	protected long start;
	protected long end;
	protected long oldstart1;
	protected long oldend1;
	protected boolean barChanged;

	public BarMovingTool() {
		this.chartlistener = new ArrayList<ChartChangeListener>();
	}

	public void addChartChangeListener(ChartChangeListener listener) {
		chartlistener.add(listener);
	}

	public Task getPointedTask() {
		return pointedTask;
	}

	public void setPointedTask(Task pointedTask) {
		this.pointedTask = pointedTask;
	}

	public TaskSeries getPointedTaskTaskSeries() {
		return pointedTaskTaskSeries;
	}

	public void setPointedTaskTaskSeries(TaskSeries pointedTaskTaskSeries) {
		this.pointedTaskTaskSeries = pointedTaskTaskSeries;
	}

	public int getItem() {
		return item;
	}

	public void setItem(int item) {
		this.item = item;
	}

	public void setMovingTool(final ChartPanelEx chartPanel) {

		chartPanel.setDomainZoomable(false);
		chartPanel.setRangeZoomable(false);

		final ChartMouseListener listener = new ChartMouseListener() {

			@Override
			public void chartMouseClicked(ChartMouseEvent arg0) {

				ChartEntity entity = (ChartEntity) arg0.getEntity();
				if (entity != null && (entity instanceof XYItemEntity)) {
					XYItemEntity item = (XYItemEntity) entity;
					TaskSeriesCollection collection = ((XYTaskDataset) dataset)
							.getTasks();
					pointedTaskTaskSeries = collection.getSeries(item
							.getSeriesIndex());
					selectedTask = collection.getSeries(item.getSeriesIndex())
							.get(item.getItem());
				}

			}

			@Override
			public void chartMouseMoved(ChartMouseEvent arg0) {

				ChartEntity entity = (ChartEntity) arg0.getEntity();
				if (entity != null && (entity instanceof XYItemEntity)) {
					XYItemEntity item = (XYItemEntity) entity;
					dataset = (XYTaskDataset) item.getDataset();
					TaskSeriesCollection collection = dataset.getTasks();
					pointedTaskTaskSeries = collection.getSeries(item
							.getSeriesIndex());
					BarMovingTool.this.item = item.getItem();
					pointedTask = pointedTaskTaskSeries.get(item.getItem());
					oldstart1 = pointedTask.getDuration().getStart()
							.getTime();
					oldend1 = pointedTask.getDuration().getEnd().getTime();

					double px = arg0.getTrigger().getPoint().getX();
					double py = arg0.getTrigger().getPoint().getY();
					Rectangle rect = item.getArea().getBounds2D().getBounds();

					double cs = chartPanel.getScaleX();
					double cy = chartPanel.getScaleY();
					barHeight = rect.getHeight() * cy;
					barWidth = rect.getWidth() * cy;
					offsetx=(int) (px-rect.x);
					offsety=(int) (py-rect.y);
					initialY = rect.y * cy + barHeight / 2;
					scale = cs
							* rect.getWidth()
							/ (pointedTask.getDuration().getEnd().getTime() - pointedTask
									.getDuration().getStart().getTime());
					if (px >= cs * rect.x && px < cs * rect.x + 5) {
						state = 1;
						chartPanel
								.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
					} else if (px <= cs * rect.x + cs * rect.width
							&& px > cs * rect.x + cs * rect.width - 5) {
						state = 2;
						chartPanel
								.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
					} else {
						state = 3;
						chartPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					}

					preX = px;
					preY = py;
					selectedTask = null;

				} else {
					selectedTask = null;
					pointedTask = null;
					chartPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}

			}

		};
		chartPanel.addChartMouseListener(listener);
		chartPanel.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				chartPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				chartPanel.setDraggingBar(false);
				chartPanel.repaint();
				if (pointedTask != null && state == 3 && adjustable) {
					adjustTask(pointedTask.getDuration().getStart().getTime(),
							pointedTask.getDuration().getEnd().getTime(),
							oldstart, oldend, moved);

				}
				if (pointedTask != null && state == 3&&barChanged) {
					barChanged=false;
					
					
					int newindex=getSeries(chartPanel, arg0.getPoint().getY());
					int oldindex=dataset.indexOf(pointedTaskTaskSeries
							.getKey());
					ProcessActivity act = (ProcessActivity) ((TaskEx) pointedTask).getActivity();
					if(newindex==-1){
						
					}
					else if (newindex!=oldindex) {
						
						TaskSeries newSeries = dataset.getTasks().getSeries(
								newindex);
						for (IResource resource : act.getAlternativeResources()) {
							if (((IEntity) resource).getName().equals(newSeries.getKey())) {
								pointedTask
								.setDuration(new SimpleTimePeriod(start, end));
								pointedTaskTaskSeries.remove(pointedTask);
								newSeries.add(pointedTask);
								alloChangeListener.allocationChanged(
										resource,act, getPos(newSeries,
										pointedTask));

							}
						}

					} else {
						pointedTask
						.setDuration(new SimpleTimePeriod(start, end));
						seqChangeListener.sequenceChanged(act,
								getPos(pointedTaskTaskSeries, pointedTask));
					}
					
				}
				pointedTask = null;
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		chartPanel.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				if (arg0.getID() == KeyEvent.VK_DELETE && selectedTask != null
						&& deleteable) {
					pointedTaskTaskSeries.remove(selectedTask);
					selectedTask = null;
					pointedTaskTaskSeries.fireSeriesChanged();
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		chartPanel.addMouseMotionListener(new MouseMotionListener() {

			

			@Override
			public void mouseDragged(MouseEvent arg0) {
				// TODO Auto-generated method stub
				if (pointedTask != null) {

					//long start = 0;
					//long end = 0;
					double px = arg0.getPoint().getX();
					double py = arg0.getPoint().getY();
					long newvalue = (long) ((px - preX) / scale);
			
					if (state == 3) {
						chartPanel.setCursor(new Cursor(Cursor.MOVE_CURSOR));
						start = oldstart1 + newvalue;
						end = oldend1 + newvalue;

					} else if (state == 1) {
						start = oldstart1 + newvalue;
						end = oldend1;

					} else {
						start = oldstart1;
						end = oldend1 + newvalue;
					}

					if (start < end) {
						barChanged=true;
				
						chartPanel.setDraggingBar(true);
						chartPanel.setRect(new Rectangle( arg0.getPoint().x-offsetx, arg0.getPoint().y-offsety,(int)barWidth,(int)barHeight));
						chartPanel.repaint();
						oldstart1=start;
						oldend1=end;
							//fireChartChange(start, end);
							//pointedTaskTaskSeries.fireSeriesChanged();
						

					}
					preX = px;
					preY = py;
				}
			}

			@Override
			public void mouseMoved(MouseEvent arg0) {
				// TODO Auto-generated method stub
				preX = arg0.getPoint().getX();
				preY = arg0.getPoint().getY();

			}
		});

	}

	private void fireChartChange(long start, long end) {
		for (ChartChangeListener listener : chartlistener) {
			listener.chartChanged(pointedTaskTaskSeries, pointedTaskTaskSeries
					.getTasks().indexOf(pointedTask), new SimpleTimePeriod(
					start, end));
		}

	}

	private void adjustTask(long start, long end, long oldstart, long oldend,
			long moved) {
		List<Task> addedTasks = new ArrayList<Task>();

		for (Object obj : pointedTaskTaskSeries.getTasks()) {
			Task task = (Task) obj;
			if (task == pointedTask) {
				continue;
			}
			long tstart = task.getDuration().getStart().getTime();
			long tend = task.getDuration().getEnd().getTime();
			if (state == 3) {
				if (moved < 0) {
					if (tstart >= start && tend <= oldend) {
						task.setDuration(new SimpleTimePeriod(tstart + end
								- start, tend + end - start));
					} else if (tstart > start && tstart < end && tend > end) {

						task.setDuration(new SimpleTimePeriod(tstart + end
								- start, tend + end - start));

					} else if (tend > start && tend < end && tstart < start) {
						task.setDuration(new SimpleTimePeriod(tstart, start));
						Task t = new Task(task.getDescription(), new Date(end),
								new Date(tend + end - start));
						addedTasks.add(t);
					} else if (tstart < start && tend > end) {
						task.setDuration(new SimpleTimePeriod(tstart, start));
						Task t = new Task(task.getDescription(), new Date(end),
								new Date(tend + end - start));
						addedTasks.add(t);
					} else {

					}
				} else {
					if (tstart >= oldstart && tend <= end) {
						task.setDuration(new SimpleTimePeriod(tstart - end
								+ start, tend - end + start));
					} else if (tstart > start && tstart < end && tend > end) {
						task.setDuration(new SimpleTimePeriod(end, tend));
						Task t = new Task(task.getDescription(), new Date(
								tstart - end + start), new Date(start));
						addedTasks.add(t);

					} else if (tend > start && tend < end && tstart < start) {
						task.setDuration(new SimpleTimePeriod(tstart - end
								+ start, tend - end + start));

					} else if (tstart < start && tend > end) {
						task.setDuration(new SimpleTimePeriod(end, tend));
						Task t = new Task(task.getDescription(), new Date(
								tstart - end + start), new Date(start));
						addedTasks.add(t);
					} else {

					}

				}
			} else if (state == 1) {
				if (tend <= oldstart) {
					task.setDuration(new SimpleTimePeriod(tstart + moved, tend
							+ moved));
				}
			} else if (state == 2) {
				if (tstart >= oldend) {
					task.setDuration(new SimpleTimePeriod(tstart + moved, tend
							+ moved));
				}
			}
		}

		for (Task task : addedTasks) {
			pointedTaskTaskSeries.add(task);
		}
		pointedTaskTaskSeries.fireSeriesChanged();

	}

	public boolean isDeleteable() {
		return deleteable;
	}

	public void setDeleteable(boolean deleteable) {
		this.deleteable = deleteable;
	}

	public SequenceChangeListener getSeqChangeListener() {
		return seqChangeListener;
	}

	public void setSeqChangeListener(SequenceChangeListener seqChangeListener) {
		this.seqChangeListener = seqChangeListener;
	}

	public AllocationChangeListener getAlloChangeListener() {
		return alloChangeListener;
	}

	public void setAlloChangeListener(
			AllocationChangeListener alloChangeListener) {
		this.alloChangeListener = alloChangeListener;
	}
	
	private int getPos(TaskSeries newseries,Task task){
		Object[] tasks = newseries.getTasks().toArray();
		Arrays.sort(tasks);
		for(int i=0;i<tasks.length;i++){
			if(tasks[i]==task){
				return i;
			}
		}
		return -1;
	}
	
	private int getSeries(ChartPanel panel,double y1){
        Insets insets = panel.getInsets();
        //int x = (int) ((x1 - insets.left) / panel.getScaleX());
        int y = (int) ((y1 - insets.top) / panel.getScaleY());

      
        if (panel.getChartRenderingInfo() != null) {
            EntityCollection entities = panel.getChartRenderingInfo().getEntityCollection();
            if (entities != null) {
            	
            	int entityCount = entities.getEntities().size();
            	
                for (int i = entityCount - 1; i >= 0; i--) {
                	ChartEntity entity = entities.getEntity(i);
                    double ey=entity.getArea().getBounds2D().getY();
                    double h=entity.getArea().getBounds2D().getHeight();
                    if (y>=ey&&y<ey+h) {
                    	if (entity instanceof XYItemEntity){
                    		XYItemEntity xyentity=(XYItemEntity) entity;
                    		return xyentity.getSeriesIndex();
                    	}
                    }
                }
            }
        }
        return -1;
		
	}

}