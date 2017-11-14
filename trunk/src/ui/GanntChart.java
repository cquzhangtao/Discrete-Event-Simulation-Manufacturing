package ui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.gantt.XYTaskDataset;
import org.jfree.data.time.SimpleTimePeriod;

import enterprise.materialflow.model.IEnterpriseModel;
import enterprise.materialflow.model.ModelUtilities;
import enterprise.materialflow.model.plan.order.IManufactureOrder;
import enterprise.materialflow.model.plan.order.job.IJob;
import enterprise.materialflow.model.plant.resource.IResource;
import enterprise.materialflow.model.product.activity.ProcessActivity;
import enterprise.materialflow.simulation.model.IEnterpriseSimulationModel;
import simulation.model.activity.ActivityState;
import simulation.model.activity.IActivity;

public class GanntChart {

	public static ChartPanelEx draw(IGanntDataset model) {
		List<String> mNames = new ArrayList<String>();
		TaskSeriesCollection collection = getDataset(model, mNames);

		JFreeChart chart = ChartFactory.createXYBarChart("", // chart
				// title
				"Resource", // domain axis label
				false, "Time", // range axis label
				new XYTaskDataset(collection), // data
				PlotOrientation.HORIZONTAL, // the plot orientation
				false, // legend
				true, // tooltips
				false // urls
				);
		SymbolAxis axis = new SymbolAxis("Resource",
				mNames.toArray(new String[0]));
		chart.getXYPlot().setRenderer(new GanntChartRenderer());
		chart.getXYPlot().setDomainAxis(axis);
		chart.getXYPlot().setRangeAxis(new DateAxis());
		chart.getXYPlot().setBackgroundPaint(Color.white);
		ChartPanelEx chartPanel = new ChartPanelEx(chart, model);

		return chartPanel;

	}

	public static TaskSeriesCollection getDataset(IGanntDataset model,
			List<String> mNames) {
		TaskSeriesCollection collection = new TaskSeriesCollection();

		for (IResource resource : model.getGanntResources()) {
			if (mNames != null) {
				mNames.add(resource.getName());
			}
			TaskSeries series = new TaskSeries(resource.getName());
			collection.add(series);
		}
		
		for (IActivity act : model.getGanntActivities()) {
			ProcessActivity pact = (ProcessActivity) act;
			if (!(pact.getState()==ActivityState.finished)) {
				System.out.println("No resource allocated");
				continue;
				//System.exit(0);
			}
			String resName = pact.getAssignedResource().getName();
			
			TaskSeries series = collection.getSeries(resName);
			if (series == null) {
				series = new TaskSeries(resName);
				collection.add(series);

			}
			SimpleTimePeriod p = new SimpleTimePeriod(act.getStartTime(),
					act.getEndTime());
			String description = act.getName();
			TaskEx task = new TaskEx(description, p);
			task.setActivity(act);
			series.add(task);
		}
		return collection;
	}

}
