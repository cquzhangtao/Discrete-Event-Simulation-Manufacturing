package ui;



import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.time.SimpleTimePeriod;

public interface ChartChangeListener {
	public void chartChanged(TaskSeries pointedTaskTaskSeries,int item,SimpleTimePeriod newValue);
}



