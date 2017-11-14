package enterprise.materialflow.model.material;

import java.util.ArrayList;
import java.util.List;

import common.Entity;

import enterprise.materialflow.model.meta.Property;
import basic.unit.UnitSystem;

public class Material extends Entity {

	private List<Property> properties;
	private static int count = 0;

	public Material() {
		super();
		setName("Material" + (++count));
		properties=new ArrayList<Property>();
		Property prop = new Property(UnitSystem.Weight);
		
		properties.add(prop);
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

}
