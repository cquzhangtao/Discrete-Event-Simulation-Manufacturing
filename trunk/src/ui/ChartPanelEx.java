package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JButton;

import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.XYTaskDataset;

import enterprise.materialflow.model.IEnterpriseModel;
import enterprise.materialflow.model.product.activity.IProcessActivity;
import enterprise.materialflow.model.product.activity.ProcessActivity;
import enterprise.materialflow.simulation.model.IEnterpriseSimulationModel;
import simulation.model.activity.IActivity;

public class ChartPanelEx extends ChartPanel {
	private Rectangle rect = new Rectangle(0, 0, 200, 200);
	private boolean isDraggingBar = false;
	private ChartMouseListener listener;
	private JButton adjustButton;
	private IGanntDataset model;

	public ChartPanelEx(JFreeChart chart, IGanntDataset model) {
		super(chart);
		this.setModel(model);
		this.setLayout(null);
		chart.getXYPlot().setBackgroundPaint(Color.white);
		this.setMaximumDrawHeight(Integer.MAX_VALUE);
		this.setMaximumDrawWidth(Integer.MAX_VALUE);
		this.setDomainZoomable(false);
		this.setRangeZoomable(false);
		listener = new ChartMouseListener() {

			@Override
			public void chartMouseClicked(ChartMouseEvent event) {

			}

			@Override
			public void chartMouseMoved(ChartMouseEvent event) {

				ChartEntity entity = event.getEntity();
				if (entity != null && entity instanceof XYItemEntity) {
					XYItemEntity ent = (XYItemEntity) entity;

					XYTaskDataset dataset = (XYTaskDataset) ent.getDataset();
					TaskEx task = (TaskEx) dataset.getTasks()
							.getSeries(ent.getSeriesIndex()).get(ent.getItem());
					ProcessActivity act = (ProcessActivity) task.getActivity();
					task.setCurrentActivity(true);

					for (int i = 0; i < dataset.getSeriesCount(); i++) {
						TaskSeries series = dataset.getTasks().getSeries(i);
						boolean alternative = act
								.canBeProcessedOn((String) series.getKey());
						for (int j = 0; j < series.getItemCount(); j++) {
							TaskEx t = (TaskEx) series.get(j);
							IProcessActivity act2 = (IProcessActivity) t.getActivity();
							if (task != t) {
								t.setCurrentActivity(false);
							}
							t.setAlternativePosition(alternative);
							t.setPrecedenceActivity(act.isPredecessor(t
									.getActivity()));
							t.setSucessorActivity(act.isSuccessor(t
									.getActivity()));
							t.setSameJob(act.getJob()==act2.getJob());
						}
					}
					event.getChart().fireChartChanged();

				}
			}

		};
		addChartMouseListener(listener);

		this.setLayout(null);
		adjustButton = new JButton("Adjust");
		adjustButton.setSize(100, 30);
		add(adjustButton);
		// ScheduleModifier modefier=new ScheduleModifier(model);
		// adjustButton.addActionListener(new ActionListener(){
		//
		// @Override
		// public void actionPerformed(ActionEvent arg0) {
		//
		// SchedulingUtilities.backupSchedule(model);
		// label.setText("Makespan: "+modefier.adjustStepbyStep());
		// getChart().getXYPlot().setDataset(new
		// XYTaskDataset(GanntChart.getDataset(model, null)));
		//
		// }});
		//
		// label=new
		// JLabel("Makespan: "+SchedulingUtilities.getLastCompleteActivity(model).getEndTime());
		// label.setSize(150, 25);
		// add(label);

	}

	@Override
	public void paintComponent(Graphics g) {

		int y = this.getBounds().y + 15;
		int x = this.getBounds().x + this.getBounds().width - 150;
		adjustButton.setLocation(x, y);
		// label.setLocation(x, y+50);

		super.paintComponent(g);

		if (isDraggingBar) {
			g.drawRect(rect.x, rect.y, rect.width, rect.height);
		}

	}

	public ChartMouseListener getListener() {
		return listener;
	}

	public void setListener(ChartMouseListener listener) {
		this.listener = listener;
	}

	public Rectangle getRect() {
		return rect;
	}

	public void setRect(Rectangle rect) {
		this.rect = rect;
	}

	public boolean isDraggingBar() {
		return isDraggingBar;
	}

	public void setDraggingBar(boolean isDraggingBar) {
		this.isDraggingBar = isDraggingBar;
	}

	public IGanntDataset getModel() {
		return model;
	}

	public void setModel(IGanntDataset model) {
		this.model = model;
	}


	// public void updateObjective(Objective obj) {
	// label.setText("Makespan: "+obj.getMakespan());
	//
	// }

}
