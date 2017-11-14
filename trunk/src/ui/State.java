package ui;


import java.awt.Color;

public class State implements Comparable<State>{
	private String name;
	private boolean linkable;
	private int sequence;
	private boolean show;
	private Color color;
	private boolean changable;
	public State(String name2) {
		name=name2;
		linkable=false;
		show=false;
		changable=true;
	}
	public String getName() {
		return name;
	}
	public boolean isLinkable() {
		return linkable;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setLinkable(boolean linkable) {
		this.linkable = linkable;
	}
	@Override
	public int compareTo(State o) {
		// TODO Auto-generated method stub
		return Integer.compare(sequence, o.getSequence());
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	public boolean isShow() {
		return show;
	}
	public void setShow(boolean show) {
		this.show = show;
	}
	public Color getColor() {
	
		return color;
	}
	public void setColor(Color color) {
	
		this.color = color;
	}
	public boolean isChangable() {
	
		return changable;
	}
	public void setChangable(boolean changable) {
	
		this.changable = changable;
	}
}
