package enterprise.materialflow.model.product.activity;

import java.util.List;

import enterprise.materialflow.model.material.InvovedMaterialIn;
import enterprise.materialflow.model.material.InvovedMaterialOut;


public class MaterialInvolvedActivity extends ProcessActivity implements IMaterialInvolvedActivity{
	private List<InvovedMaterialIn> invovedMaterialIn;
	private List<InvovedMaterialOut> invovedMaterialOut;
	@Override
	public boolean hasEnoughMaterial() {
		// TODO Auto-generated method stub
		return false;
	}

}
