package ui;
import java.awt.Color;
import java.awt.Paint;
import java.util.Date;

import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.XYTaskDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.TextAnchor;

import enterprise.materialflow.model.product.activity.IProcessActivity;

@SuppressWarnings("serial")
public class GanntChartRenderer extends XYBarRenderer {

	public GanntChartRenderer() {
		this.setBarPainter(new StandardXYBarPainter());
		this.setUseYInterval(true);
		setShadowVisible(false);
		this.setDrawBarOutline(true);
		setBaseToolTipGenerator(new XYToolTipGenerator() {

			@Override
			public String generateToolTip(XYDataset dataset, int series,
					int item) {
				XYTaskDataset dataset1 = (XYTaskDataset) dataset;
				TaskEx task = (TaskEx) dataset1.getTasks().getSeries(series)
						.get(item);
				Date start = task.getDuration().getStart();
				Date end = task.getDuration().getEnd();
				if (task.getActivity() == null) {
					return "B"+ "("
							+ (end.getTime() - start.getTime()) + ")["
							+ start.getTime() + "," + end.getTime() + "]";
				}
				IProcessActivity act=(IProcessActivity) task.getActivity();
				return act.getJob().getOrder().getName()+"_"+task.getActivity().getName() + "("
						+ (end.getTime() - start.getTime()) + ")["
						+ start.getTime() + "," + end.getTime() + "]";

			}
		});
		this.setBaseItemLabelsVisible(true);
		this.setBasePositiveItemLabelPosition(new ItemLabelPosition(
				ItemLabelAnchor.CENTER, TextAnchor.CENTER, TextAnchor.CENTER, 0));

		this.setBaseItemLabelGenerator(new XYItemLabelGenerator() {

			@Override
			public String generateLabel(XYDataset dataset, int series, int item) {
				XYTaskDataset dataset1 = (XYTaskDataset) dataset;
				TaskEx task = (TaskEx) dataset1.getTasks().getSeries(series)
						.get(item);
				if(task.getActivity()==null){
					return "B";
				}
				return task.getActivity().getName();
			}
		});
	}

	@Override
	public Paint getItemPaint(int r, int c) {
		Task task = ((XYTaskDataset) this.getPlot().getDataset()).getTasks()
				.getSeries(r).get(c);
		if (task instanceof TaskEx) {
			TaskEx taskE=(TaskEx) task;
			if(taskE.isCurrentActivity()){
				return Color.blue;
			}
			if(taskE.isPrecedenceActivity()){
				return Color.red;
			}
			if(taskE.isSucessorActivity()){
				return Color.magenta;
			}
			if(taskE.isSameJob()){
				return Color.pink;
			}
			if (taskE.isAlternativePosition()) {
				return new Color(176,196,222);
			}
		
				return Color.lightGray;
			
		}
	
		return null;

	}

	@Override
	public Paint getItemLabelPaint(int row, int column) {

		Task task = ((XYTaskDataset) this.getPlot().getDataset()).getTasks()
				.getSeries(row).get(column);
		if (task instanceof TaskEx) {
			TaskEx t = ((TaskEx) task);
			if (t.isAlternativePosition()||t.isPrecedenceActivity()||t.isCurrentActivity()) {
				return Color.white;
			}
		}

		Paint result = getSeriesItemLabelPaint(row);
		if (result == null) {
			result = getBaseItemLabelPaint();
		}

		return result;
	}

}
