package enterprise.materialflow.model.meta;

import common.Entity;


public class Skill extends Entity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6340598450403931854L;
	private static int count=0;
	public Skill(){
		super();
		setName("Skill"+(++count));
	}
}
