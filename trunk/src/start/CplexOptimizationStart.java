package start;
import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

import enterprise.materialflow.control.KeyPerformanceEnmu;
import enterprise.materialflow.model.IEnterpriseModel;
import enterprise.materialflow.model.test.ChainModel;
import enterprise.materialflow.model.test.TestModel;
import enterprise.materialflow.optimization.CPSolver;
import enterprise.materialflow.optimization.CplexSolver;
import enterprise.materialflow.optimization.model.IOptimizationModel;
import enterprise.materialflow.optimization.model.OptimizationModel;
import ilog.concert.IloException;
import ui.BarMovingTool;
import ui.ChartPanelEx;
import ui.GanntChart;
import ui.GraphicNetwork;
import ui.GraphicNetworkData;


public class CplexOptimizationStart {

	public static void main(String[] args) throws IloException {
		IEnterpriseModel enterprise=ChainModel.finiteModel();
		OptimizationModel model=new OptimizationModel(enterprise);
		model.setObjective(KeyPerformanceEnmu.averageCT);
		CplexSolver solver=new CplexSolver(model);
		if(solver.solve()){
			//System.out.println(solver.getObjective());
			System.out.println(model.getMakespan());
			System.out.println(model.getAverageCycleTime());
			GanntUI ui=new GanntUI(model);		
			ui.pack();
			ui.setVisible(true);
		}else{
			System.out.println("No Solution");
		}
	}

}
