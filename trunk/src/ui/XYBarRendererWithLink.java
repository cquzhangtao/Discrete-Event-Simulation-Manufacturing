package ui;



import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;

import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.gantt.XYTaskDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleEdge;

public  class XYBarRendererWithLink extends XYBarRenderer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Map<LinkTask, Rectangle2D> bars;
	public XYBarRendererWithLink() {
	
		super();
		this.setUseYInterval(true);
	
		
	}
	
	public void init() {
	
		bars = new HashMap<LinkTask, Rectangle2D>();
	}
	
	@Override
	public void drawItem(Graphics2D g2, XYItemRendererState state, Rectangle2D dataArea, PlotRenderingInfo info, XYPlot plot, ValueAxis domainAxis,
			ValueAxis rangeAxis, XYDataset dataset, int series, int item, CrosshairState crosshairState, int pass) {
	
		if (!getItemVisible(series, item)) {
			// return;
		}
		// if (series == dataset.getSeriesCount() - 1
		// && item == dataset.getItemCount(series) - 1)
		// bars.clear();
		XYTaskDataset taskDataset = (XYTaskDataset) dataset;
		IntervalXYDataset intervalDataset = (IntervalXYDataset) dataset;
		
		double value0;
		double value1;
		if (super.getUseYInterval()) {
			value0 = intervalDataset.getStartYValue(series, item);
			value1 = intervalDataset.getEndYValue(series, item);
		}
		else {
			value0 = super.getBase();
			value1 = intervalDataset.getYValue(series, item);
		}
		if (Double.isNaN(value0) || Double.isNaN(value1)) {
			return;
		}
		boolean outofRange = false;
		if (value0 <= value1) {
			if (!rangeAxis.getRange().intersects(value0, value1)) {
				outofRange = true;
				// if (value0 < rangeAxis.getRange().getLowerBound()) {
				// value0 = rangeAxis.getRange().getLowerBound();
				// }
				// if (value1 > rangeAxis.getRange().getUpperBound()) {
				// value1 = rangeAxis.getRange().getUpperBound();
				// }
				// return;
			}
		}
		else {
			if (!rangeAxis.getRange().intersects(value1, value0)) {
				outofRange = true;
				// if (value1 < rangeAxis.getRange().getLowerBound()) {
				// value1 = rangeAxis.getRange().getLowerBound();
				// }
				// if (value0 > rangeAxis.getRange().getUpperBound()) {
				// value0 = rangeAxis.getRange().getUpperBound();
				// }
				// return;
			}
		}
		
		double translatedValue0 = rangeAxis.valueToJava2D(value0, dataArea, plot.getRangeAxisEdge());
		double translatedValue1 = rangeAxis.valueToJava2D(value1, dataArea, plot.getRangeAxisEdge());
		double bottom = Math.min(translatedValue0, translatedValue1);
		double top = Math.max(translatedValue0, translatedValue1);
		
		double startX = intervalDataset.getStartXValue(series, item);
		if (Double.isNaN(startX)) {
			return;
		}
		double endX = intervalDataset.getEndXValue(series, item);
		if (Double.isNaN(endX)) {
			return;
		}
		
		if (startX <= endX) {
			if (!domainAxis.getRange().intersects(startX, endX)) {
				outofRange = true;
				if (startX < domainAxis.getRange().getLowerBound()) {
					startX = domainAxis.getRange().getLowerBound();
				}
				if (endX > domainAxis.getRange().getUpperBound()) {
					endX = domainAxis.getRange().getUpperBound();
				}
				// return;
			}
		}
		else {
			if (!domainAxis.getRange().intersects(endX, startX)) {
				outofRange = true;
				if (endX < domainAxis.getRange().getLowerBound()) {
					endX = domainAxis.getRange().getLowerBound();
				}
				if (startX > domainAxis.getRange().getUpperBound()) {
					startX = domainAxis.getRange().getUpperBound();
				}
				// return;
			}
		}
		
		// is there an alignment adjustment to be made?
		if (super.getBarAlignmentFactor() >= 0.0 && super.getBarAlignmentFactor() <= 1.0) {
			double x = intervalDataset.getXValue(series, item);
			double interval = endX - startX;
			startX = x - interval * super.getBarAlignmentFactor();
			endX = startX + interval;
		}
		
		RectangleEdge location = plot.getDomainAxisEdge();
		double translatedStartX = domainAxis.valueToJava2D(startX, dataArea, location);
		double translatedEndX = domainAxis.valueToJava2D(endX, dataArea, location);
		
		double translatedWidth = Math.max(1, Math.abs(translatedEndX - translatedStartX));
		
		double left = Math.min(translatedStartX, translatedEndX);
		if (getMargin() > 0.0) {
			double cut = translatedWidth * getMargin();
			translatedWidth = translatedWidth - cut;
			left = left + cut / 2;
		}
		
		Rectangle2D bar = null;
		PlotOrientation orientation = plot.getOrientation();
		if (orientation == PlotOrientation.HORIZONTAL) {
			// clip left and right bounds to data area
			bottom = Math.max(bottom, dataArea.getMinX());
			top = Math.min(top, dataArea.getMaxX());
			bar = new Rectangle2D.Double(bottom, left, top - bottom, translatedWidth);
		}
		else if (orientation == PlotOrientation.VERTICAL) {
			// clip top and bottom bounds to data area
			bottom = Math.max(bottom, dataArea.getMinY());
			top = Math.min(top, dataArea.getMaxY());
			bar = new Rectangle2D.Double(left, bottom, translatedWidth, top - bottom);
		}
		Task task = taskDataset.getTasks().getSeries(series).get(item);
		if (task instanceof LinkTask) {
			bars.put((LinkTask) taskDataset.getTasks().getSeries(series).get(item), bar);
			drawLinks(g2, (LinkTask) taskDataset.getTasks().getSeries(series).get(item), bar);
		}
		
		if (outofRange) {
			return;
		}
		
		boolean positive = (value1 > 0.0);
		boolean inverted = rangeAxis.isInverted();
		RectangleEdge barBase;
		if (orientation == PlotOrientation.HORIZONTAL) {
			if (positive && inverted || !positive && !inverted) {
				barBase = RectangleEdge.RIGHT;
			}
			else {
				barBase = RectangleEdge.LEFT;
			}
		}
		else {
			if (positive && !inverted || !positive && inverted) {
				barBase = RectangleEdge.BOTTOM;
			}
			else {
				barBase = RectangleEdge.TOP;
			}
		}
		if (getShadowsVisible()) {
			super.getBarPainter().paintBarShadow(g2, this, series, item, bar, barBase, !super.getUseYInterval());
		}
		super.getBarPainter().paintBar(g2, this, series, item, bar, barBase);
		
		if (isItemLabelVisible(series, item)) {
			XYItemLabelGenerator generator = getItemLabelGenerator(series, item);
			drawItemLabel(g2, dataset, series, item, plot, generator, bar, value1 < 0.0);
		}
		
		// update the crosshair point
		double x1 = (startX + endX) / 2.0;
		double y1 = dataset.getYValue(series, item);
		double transX1 = domainAxis.valueToJava2D(x1, dataArea, location);
		double transY1 = rangeAxis.valueToJava2D(y1, dataArea, plot.getRangeAxisEdge());
		int domainAxisIndex = plot.getDomainAxisIndex(domainAxis);
		int rangeAxisIndex = plot.getRangeAxisIndex(rangeAxis);
		updateCrosshairValues(crosshairState, x1, y1, domainAxisIndex, rangeAxisIndex, transX1, transY1, plot.getOrientation());
		
		EntityCollection entities = state.getEntityCollection();
		if (entities != null) {
			addEntity(entities, bar, dataset, series, item, 0.0, 0.0);
		}
		
	}
	
	private void drawLinks(Graphics2D g2, LinkTask task, Rectangle2D bar) {
	
		for (LinkTask tr : task.getpredecessors()) {
			if (bars.get(tr) == null) {
				continue;
			}
			drawLink(g2, bars.get(tr), bar);
		}
		
		for (LinkTask tr : task.getSuccessors()) {
			if (bars.get(tr) == null) {
				continue;
			}
			drawLink(g2, bar, bars.get(tr));
		}
	}
	
	private void drawLink(Graphics2D g2, Rectangle2D startBar, Rectangle2D endBar) {
	
		if (startBar.getX() + startBar.getWidth() < endBar.getX() - 8) {
			Point2D.Double p1 = new Point2D.Double();
			Point2D.Double p2 = new Point2D.Double();
			Point2D.Double p3 = new Point2D.Double();
			Point2D.Double p4 = new Point2D.Double();
			Point2D.Double p5 = new Point2D.Double();
			Point2D.Double p6 = new Point2D.Double();
			
			p1.setLocation(startBar.getX() + startBar.getWidth(), startBar.getY() + startBar.getHeight() / 2);
			p2.setLocation((startBar.getX() + startBar.getWidth() + endBar.getX()) / 2, startBar.getY() + startBar.getHeight() / 2);
			p3.setLocation((startBar.getX() + startBar.getWidth() + endBar.getX()) / 2, endBar.getY() + endBar.getHeight() / 2);
			p4.setLocation(endBar.getX(), endBar.getY() + endBar.getHeight() / 2);
			p5.setLocation(p4.getX() - 4, p4.getY() - 3);
			p6.setLocation(p4.getX() - 4, p4.getY() + 3);
			
			Path2D.Double path = new Path2D.Double();
			path.moveTo(p1.getX(), p1.getY());
			path.lineTo(p2.getX(), p2.getY());
			path.lineTo(p3.getX(), p3.getY());
			path.lineTo(p4.getX(), p4.getY());
			path.moveTo(p5.getX(), p5.getY());
			path.lineTo(p4.getX(), p4.getY());
			path.lineTo(p6.getX(), p6.getY());
			// path.closePath();
			g2.setPaint(Color.blue);
			g2.setStroke(new BasicStroke(1f));
			g2.draw(path);
		}
		else {
			Point2D.Double p1 = new Point2D.Double();
			Point2D.Double p2 = new Point2D.Double();
			Point2D.Double p3 = new Point2D.Double();
			Point2D.Double p4 = new Point2D.Double();
			Point2D.Double p5 = new Point2D.Double();
			Point2D.Double p6 = new Point2D.Double();
			Point2D.Double p7 = new Point2D.Double();
			Point2D.Double p8 = new Point2D.Double();
			
			p1.setLocation(startBar.getX() + startBar.getWidth(), startBar.getY() + startBar.getHeight() / 2);
			p2.setLocation(startBar.getX() + startBar.getWidth() + 8, startBar.getY() + startBar.getHeight() / 2);
			p3.setLocation(startBar.getX() + startBar.getWidth() + 8, (startBar.getY() + startBar.getHeight() + endBar.getY()) / 2.0);
			p4.setLocation(endBar.getX() - 8, (startBar.getY() + startBar.getHeight() + endBar.getY()) / 2.0);
			p5.setLocation(endBar.getX() - 8, endBar.getY() + endBar.getHeight() / 2);
			p6.setLocation(endBar.getX(), endBar.getY() + endBar.getHeight() / 2);
			p7.setLocation(p6.getX() - 4, p6.getY() - 3);
			p8.setLocation(p6.getX() - 4, p6.getY() + 3);
			
			Path2D.Double path = new Path2D.Double();
			path.moveTo(p1.getX(), p1.getY());
			path.lineTo(p2.getX(), p2.getY());
			path.lineTo(p3.getX(), p3.getY());
			path.lineTo(p4.getX(), p4.getY());
			path.lineTo(p5.getX(), p5.getY());
			path.lineTo(p6.getX(), p6.getY());
			path.moveTo(p7.getX(), p7.getY());
			path.lineTo(p6.getX(), p6.getY());
			path.lineTo(p8.getX(), p8.getY());
			// path.closePath();
			g2.setPaint(Color.blue);
			g2.setStroke(new BasicStroke(1f));
			g2.draw(path);
		}
	}
	
	public Paint getItemPaint(int r, int c) {
		XYTaskDataset taskDataset = (XYTaskDataset) this.getPlot().getDataset();
		TaskSeriesCollection collection = taskDataset.getTasks();
	
		LinkTask task = (LinkTask) collection.getSeries(r).get(c);
//		if (task.getTimeSlice().getParameter("Color") != null) {
//			return (Paint) task.getTimeSlice().getParameter("Color");
//		}
		
		return task.getTimeSlice().getState().getColor();
	
	}
	
}
