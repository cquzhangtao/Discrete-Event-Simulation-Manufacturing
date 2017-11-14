package common;

import java.util.List;

public interface INetworkEntity<T> extends IEntity{
	public List<T> getPredecessors();
	public void setPredecessors(List<T> predecessors);
	public List<T> getSuccessors();
	public void setSuccessors(List<T> successors);
}
