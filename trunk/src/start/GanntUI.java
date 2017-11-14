package start;
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import ui.BarMovingTool;
import ui.ChartPanelEx;
import ui.GanntChart;
import ui.GraphicNetwork;
import ui.GraphicNetworkData;
import ui.IGanntDataset;


public class GanntUI extends JFrame{
	
	public GanntUI(IGanntDataset dataset){
		GraphicNetworkData network=new GraphicNetworkData(dataset.getGanntActivities());	
		GraphicNetwork networkGraph = new GraphicNetwork(network);
		ChartPanelEx chartPanel = GanntChart.draw(dataset);
		BarMovingTool tool=new BarMovingTool();
		tool.setMovingTool(chartPanel);
		JSplitPane pane=new JSplitPane(JSplitPane.VERTICAL_SPLIT,chartPanel,networkGraph);
		setPane(pane);
		setLayout(new BorderLayout());
		add(pane,BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
	}
	private static void setPane(JSplitPane pane) {
		pane.setDividerLocation(450);
		BasicSplitPaneUI ui = (BasicSplitPaneUI)pane.getUI();
		ui.getDivider().addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(pane.getDividerLocation()<450){
					pane.setDividerLocation(450);
				}else{
					pane.setDividerLocation(10);
				}
				
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

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}});
	}
//	private static void addChangeListener(IGanntDataset model, Network network,
//			 ChartPanelEx chartPanel, BarMovingTool tool,ScheduleModifier modefier) {
//		tool.setSeqChangeListener(new SequenceChangeListener() {
//			
//			@Override
//			public void sequenceChanged(IActivity activity, int newpos) {
//				SchedulingUtilities.backupSchedule(model);
//				SchedulingUtilities.changeSequence(model,activity, newpos);
//				if(!SchedulingUtilities.sequencing(model)){
//					SchedulingUtilities.rollBackSchedule(model);
//					System.out.println("Rollbacked");
//				}
//				network.forward();
//				updateChart(model, chartPanel, activity,SchedulingUtilities.getObjective(model));
//			}
//		});
//		tool.setAlloChangeListener(new AllocationChangeListener() {
//
//			@Override
//			public void allocationChanged(IResource resourceNew,
//					IActivity activity, int newpos) {
//				SchedulingUtilities.backupSchedule(model);
//				SchedulingUtilities.changeResource(model,resourceNew,activity, newpos);
//				if(!SchedulingUtilities.sequencing(model)){
//					SchedulingUtilities.rollBackSchedule(model);
//					System.out.println("Rollbacked");
//				}
//				network.forward();
//				
//				updateChart(model, chartPanel, activity,SchedulingUtilities.getObjective(model));
//			}
//
//			
//		});
//	}
//	private static void updateChart(IGanntDataset model, ChartPanelEx chartPanel,
//			IActivity activity,Objective obj) {
//		XYTaskDataset dataset = new XYTaskDataset(GanntChart.getDataset(model, null));				
//		Object[]tasks=dataset.getTasks().getSeries(activity.getAssignedResource().getName()).getTasks().toArray();
//		Arrays.sort(tasks);
//		Object task = tasks[activity.getPositionOnResource()];
//		int index=dataset.getTasks().getSeries(activity.getAssignedResource().getName()).getTasks().indexOf(task);
//	
//		XYItemEntity entity=new XYItemEntity(new  Rectangle(),
//				dataset, dataset.getTasks().getRowIndex(activity.getAssignedResource().getName()),index ,
//               null, null);
//		chartPanel.updateObjective(obj);
//		chartPanel.getListener().chartMouseMoved(new ChartMouseEvent(chartPanel.getChart(),null,entity));
//		chartPanel.getChart().getXYPlot().setDataset(dataset);
//	}


}
