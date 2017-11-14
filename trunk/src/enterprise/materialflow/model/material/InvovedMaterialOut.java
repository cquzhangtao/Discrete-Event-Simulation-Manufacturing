package enterprise.materialflow.model.material;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import enterprise.materialflow.model.plant.Storage;
import basic.unit.IUnit;
import basic.unit.LengthUnitEnum;
import basic.unit.QuatityUnitEnum;
import basic.unit.TimeUnitEnum;
import basic.unit.UnitSystem;
import basic.unit.WeightUnitEnum;
import basic.volume.RandomVolume;


public class InvovedMaterialOut implements Serializable{

	private Material material;
	private RandomVolume<? extends IUnit> amount;
	private List<Storage> places;
	
	public InvovedMaterialOut(){
		//material=new Material();
		places=new ArrayList<Storage>();

		amount=new RandomVolume<WeightUnitEnum>();
		((RandomVolume<WeightUnitEnum>)amount).setUnit(WeightUnitEnum.kg);

	}
	
	private void createAmount(Material material){
		UnitSystem unitSystem=material.getProperties().get(0).getUnitSystem();
		if (unitSystem == UnitSystem.Length) {
			amount=new RandomVolume<LengthUnitEnum>();
			((RandomVolume<LengthUnitEnum>)amount).setUnit(LengthUnitEnum.m);

		} else if (unitSystem == UnitSystem.Time) {
			amount=new RandomVolume<TimeUnitEnum>();
			((RandomVolume<TimeUnitEnum>)amount).setUnit(TimeUnitEnum.Hour);
	
		} else if (unitSystem == UnitSystem.Weight) {
			amount=new RandomVolume<WeightUnitEnum>();
			((RandomVolume<WeightUnitEnum>)amount).setUnit(WeightUnitEnum.kg);
	
		} else if (unitSystem == UnitSystem.Quautity) {
			amount=new RandomVolume<QuatityUnitEnum>();
			((RandomVolume<QuatityUnitEnum>)amount).setUnit(QuatityUnitEnum.Lot);
		}
	}

	
	public RandomVolume<? extends IUnit> getAmount() {
		return amount;
	}

	public List<Storage> getPlaces() {
		return places;
	}


	public void setAmount(RandomVolume<? extends IUnit> amount) {
		this.amount = amount;
	}

	public void setPlaces(List<Storage> places) {
		this.places = places;
	}

	public void setMaterial(Material material) {
		if(material==null)
			return;
		this.material = material;
		createAmount(material);
	}

	public Material getMaterial() {
		return material;
	}
}
