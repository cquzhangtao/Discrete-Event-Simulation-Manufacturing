package ui;

import java.util.List;

import common.IEntity;


public interface IGraphicActivity extends IEntity{

	void setState(GraphicActivityState state);
	
	GraphicActivityState getState();
	int startable();

	void setCol(int col);

	int getCol();

	void setRow(int row);

	int getRow();

	List<IGraphicActivity> getPredecessors();

	List<IGraphicActivity> getSuccessors();
	
	int getJobIndex();

}
