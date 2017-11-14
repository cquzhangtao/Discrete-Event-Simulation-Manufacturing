package start;
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import enterprise.materialflow.model.IEnterpriseModel;
import enterprise.materialflow.model.ModelUtilities;
import enterprise.materialflow.model.test.TestModel;
import enterprise.materialflow.simulation.EnterpriseSimulation;
import enterprise.materialflow.simulation.IEnterpriseSimulation;
import enterprise.materialflow.simulation.model.EnterpriseSimulationModel;
import enterprise.materialflow.simulation.model.IEnterpriseSimulationModel;
import enterprise.materialflow.simulation.result.SimulationResultNew;
import simulation.core.others.SimulationDuration;
import ui.BarMovingTool;
import ui.ChartPanelEx;
import ui.GanntChart;
import ui.GraphicNetwork;
import ui.GraphicNetworkData;
import basic.unit.TimeUnitEnum;
import basic.volume.TimeVolume;


public class InfiniteSimStart {

	public static void main(String[] args) {
		
		long start=System.currentTimeMillis();
		IEnterpriseModel enterprise=TestModel.infiniteModel();
		EnterpriseSimulationModel simModel=new EnterpriseSimulationModel(enterprise);
		SimulationResultNew.init(simModel,new TimeVolume(7,TimeUnitEnum.Day));
		IEnterpriseSimulation simulation=new EnterpriseSimulation(simModel);
		//ModelUtilities.setCONWIPJobReleases(simulation);
		//ModelUtilities.setSTTDActivitySelector(simulation);
		//ModelUtilities.setSTTDResourceSelector(simulation);
		simulation.addEndCondition(new SimulationDuration(new TimeVolume(300,TimeUnitEnum.Day)));
		System.out.println((System.currentTimeMillis()-start)/1000);
		start=System.currentTimeMillis();
		simulation.run();
		System.out.println((System.currentTimeMillis()-start)/1000);
		start=System.currentTimeMillis();
		SimulationResultNew.stat();
		System.out.println(SimulationResultNew.getProductResultString());
		System.out.println((System.currentTimeMillis()-start)/1000);
		System.out.println(simulation.getCurrentTime());
		
	}
	

}
