package enterprise.materialflow.model.material;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import enterprise.materialflow.model.meta.Property;
import enterprise.materialflow.model.plant.Storage;
import basic.unit.IUnit;
import basic.unit.LengthUnitEnum;
import basic.unit.QuatityUnitEnum;
import basic.unit.TimeUnitEnum;
import basic.unit.UnitSystem;
import basic.unit.WeightUnitEnum;
import basic.volume.RandomVolume;


public class InvovedMaterialIn implements Serializable{
	private Property property;
	private RandomVolume<? extends IUnit> amount;
	private List<Storage> places;
	private List<MaterialGroup> groups;
	
	public InvovedMaterialIn(){
		//property=new Property(UnitSystem.Weight);
		amount=new RandomVolume<WeightUnitEnum>();
		((RandomVolume<WeightUnitEnum>)amount).setUnit(WeightUnitEnum.kg);
		places=new ArrayList<Storage>();
		setGroups(new ArrayList<MaterialGroup>());
	}

	public Property getProperty() {
		return property;
	}

	public RandomVolume<? extends IUnit> getAmount() {
		return amount;
	}

	public List<Storage> getPlaces() {
		return places;
	}

	public void setProperty(Property property) {
		if(property==null)
			return;
		this.property = property;
		UnitSystem unitSystem=property.getUnitSystem();
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

	public void setAmount(RandomVolume<? extends IUnit> amount) {
		this.amount = amount;
	}

	public void setPlaces(List<Storage> places) {
		this.places = places;
	}

	public void setGroups(List<MaterialGroup> groups) {
		this.groups = groups;
	}

	public List<MaterialGroup> getGroups() {
		return groups;
	}


}
