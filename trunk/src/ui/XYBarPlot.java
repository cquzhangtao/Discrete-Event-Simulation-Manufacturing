package ui;


import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.SeriesRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYDataset;

public class XYBarPlot extends XYPlot{
	
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public XYBarPlot(XYDataset dataset,
              ValueAxis domainAxis,
              ValueAxis rangeAxis,
              XYItemRenderer renderer) 
	  {
		  super(dataset,domainAxis,rangeAxis,renderer);
	  }

	@Override
	 public boolean render(Graphics2D g2, Rectangle2D dataArea, int index,
	            PlotRenderingInfo info, CrosshairState crosshairState) {

	        boolean foundData = false;
	        XYDataset dataset = getDataset(index);
	        if (!DatasetUtilities.isEmptyOrNull(dataset)) {
	            foundData = true;
	            ValueAxis xAxis = getDomainAxisForDataset(index);
	            ValueAxis yAxis = getRangeAxisForDataset(index);
	            if (xAxis == null || yAxis == null) {
	                return foundData;  // can't render anything without axes
	            }
	            XYBarRendererWithLink renderer = (XYBarRendererWithLink) getRenderer(index);
	            if (renderer == null) {
	                renderer = (XYBarRendererWithLink) getRenderer();
	                if (renderer == null) { // no default renderer available
	                    return foundData;
	                }
	            }
	            renderer.init();

	            XYItemRendererState state = renderer.initialise(g2, dataArea, this,
	                    dataset, info);
	            int passCount = renderer.getPassCount();

	            SeriesRenderingOrder seriesOrder = getSeriesRenderingOrder();
	            if (seriesOrder == SeriesRenderingOrder.REVERSE) {
	                //render series in reverse order
	                for (int pass = 0; pass < passCount; pass++) {
	                    int seriesCount = dataset.getSeriesCount();
	                    for (int series = seriesCount - 1; series >= 0; series--) {
	                        int firstItem = 0;
	                        int lastItem = dataset.getItemCount(series) - 1;
	                        if (lastItem == -1) {
	                            continue;
	                        }
//	                        if (state.getProcessVisibleItemsOnly()) {
//	                            int[] itemBounds = RendererUtilities.findLiveItems(
//	                                    dataset, series, xAxis.getLowerBound(),
//	                                    xAxis.getUpperBound());
//	                            firstItem = Math.max(itemBounds[0] - 1, 0);
//	                            lastItem = Math.min(itemBounds[1] + 1, lastItem);
//	                        }
	                        state.startSeriesPass(dataset, series, firstItem,
	                                lastItem, pass, passCount);
	                        for (int item = firstItem; item <= lastItem; item++) {
	                            renderer.drawItem(g2, state, dataArea, info,
	                                    this, xAxis, yAxis, dataset, series, item,
	                                    crosshairState, pass);
	                        }
	                        state.endSeriesPass(dataset, series, firstItem,
	                                lastItem, pass, passCount);
	                    }
	                }
	            }
	            else {
	                //render series in forward order
	                for (int pass = 0; pass < passCount; pass++) {
	                    int seriesCount = dataset.getSeriesCount();
	                    for (int series = 0; series < seriesCount; series++) {
	                        int firstItem = 0;
	                        int lastItem = dataset.getItemCount(series) - 1;
//	                        if (state.getProcessVisibleItemsOnly()) {
//	                            int[] itemBounds = RendererUtilities.findLiveItems(
//	                                    dataset, series, xAxis.getLowerBound(),
//	                                    xAxis.getUpperBound());
//	                            firstItem = Math.max(itemBounds[0] - 1, 0);
//	                            lastItem = Math.min(itemBounds[1] + 1, lastItem);
//	                        }
	                        state.startSeriesPass(dataset, series, firstItem,
	                                lastItem, pass, passCount);
	                        for (int item = firstItem; item <= lastItem; item++) {
	                            renderer.drawItem(g2, state, dataArea, info,
	                                    this, xAxis, yAxis, dataset, series, item,
	                                    crosshairState, pass);
	                        }
	                        state.endSeriesPass(dataset, series, firstItem,
	                                lastItem, pass, passCount);
	                    }
	                }
	            }
	        }
	        return foundData;
	    }
}
