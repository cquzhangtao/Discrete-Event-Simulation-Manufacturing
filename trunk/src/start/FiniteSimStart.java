package start;
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import enterprise.materialflow.control.KeyPerformanceEnmu;
import enterprise.materialflow.model.IEnterpriseModel;
import enterprise.materialflow.model.ModelUtilities;
import enterprise.materialflow.model.test.ChainModel;
import enterprise.materialflow.model.test.TestModel;
import enterprise.materialflow.simulation.EnterpriseSimulation;
import enterprise.materialflow.simulation.IEnterpriseSimulation;
import enterprise.materialflow.simulation.model.EnterpriseSimulationModel;
import enterprise.materialflow.simulation.model.IEnterpriseSimulationModel;
import enterprise.materialflow.simulation.result.SimulationResultNew;
import ui.BarMovingTool;
import ui.ChartPanelEx;
import ui.GanntChart;
import ui.GraphicNetwork;
import ui.GraphicNetworkData;
import basic.unit.TimeUnitEnum;
import basic.volume.TimeVolume;


public class FiniteSimStart {

	public static void main(String[] args) {
		
		
		IEnterpriseModel enterprise=ChainModel.finiteModel();
		EnterpriseSimulationModel simModel=new EnterpriseSimulationModel(enterprise);
		IEnterpriseSimulation simulation=new EnterpriseSimulation(simModel);
		SimulationResultNew.init(simModel,new TimeVolume(7,TimeUnitEnum.Day));
		//ModelUtilities.setCONWIPJobReleases(simulation);
		ModelUtilities.setSTTDActivitySelector(simulation,KeyPerformanceEnmu.averageCT);
		ModelUtilities.setSTTDResourceSelector(simulation,KeyPerformanceEnmu.averageCT);

		long start = System.currentTimeMillis();
		simulation.run();
		System.out.println((System.currentTimeMillis()-start)/1000);
	
		SimulationResultNew.stat();
		System.out.println(SimulationResultNew.getProductResultString());
		System.out.println(simulation.getCurrentTime());
		
		GanntUI ui=new GanntUI(simModel);		
		ui.pack();
		ui.setVisible(true);

	}
	

}
