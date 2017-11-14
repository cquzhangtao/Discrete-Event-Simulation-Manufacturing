package ui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.InputEvent;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.TransformerUtils;

import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode;
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.TranslatingGraphMousePlugin;

@SuppressWarnings("serial")
public class GraphicNetwork extends JPanel {

	public GraphicNetwork(GraphicNetworkData network) {
		//network.generatorCol();
		//network.generateRow();
		network.setPosition();
		Graph<IGraphicActivity, String> g2 = new SparseMultigraph<IGraphicActivity, String>();
		Map<IGraphicActivity, Point2D> map = new HashMap<IGraphicActivity, Point2D>();
		Map<IGraphicActivity, String> verticeNameMap = new HashMap<IGraphicActivity, String>();
		Map<String, String> edgeNameMap = new HashMap<String, String>();
		for (IGraphicActivity act : network.getActivities()) {
			g2.addVertex(act);
			map.put(act, new Point2D.Double(act.getCol()*200
					,act.getRow() ));
			verticeNameMap.put(act, String.valueOf(act.getName()));
			for (IGraphicActivity act1 : act.getSuccessors()) {
				g2.addEdge(act.getName() + "," + act1.getName(), act, (IGraphicActivity) act1,
						EdgeType.DIRECTED);
				edgeNameMap.put(act.getName() + "," + act1.getName(),
						act.getName() + "," + act1.getName());
			}
		}

		Transformer<IGraphicActivity, Point2D> vertexLocations = TransformerUtils
				.mapTransformer(map);
		Transformer<IGraphicActivity, String> vertexLabels = TransformerUtils
				.mapTransformer(verticeNameMap);
		Transformer<String, String> edgeLabels = TransformerUtils
				.mapTransformer(edgeNameMap);

		StaticLayout<IGraphicActivity, String> layout = new StaticLayout<IGraphicActivity, String>(
				g2, vertexLocations);

		VisualizationViewer<IGraphicActivity, String> viewer = new VisualizationViewer<IGraphicActivity, String>(
				layout);
		viewer.setBackground(Color.WHITE);
		viewer.setPreferredSize(new Dimension(1900, 1200));
		// mVizViewer.setMinimumSize(new Dimension(1200, 1200));

		viewer.getRenderContext().setVertexLabelTransformer(vertexLabels);
		//viewer.getRenderContext().setEdgeLabelTransformer(edgeLabels);
		viewer.getRenderContext().setLabelOffset(0);
		viewer.setAutoscrolls(true);

		DefaultModalGraphMouse<IGraphicActivity, String> mouse = new DefaultModalGraphMouse<IGraphicActivity, String>();
		mouse.add(new PickingGraphMousePlugin<IGraphicActivity, String>());
		mouse.add(new TranslatingGraphMousePlugin(InputEvent.BUTTON1_MASK
				| InputEvent.CTRL_MASK));
		mouse.setMode(Mode.PICKING);
		viewer.setGraphMouse(mouse);

		add(viewer);
	}
}
