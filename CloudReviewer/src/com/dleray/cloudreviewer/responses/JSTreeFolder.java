package com.dleray.cloudreviewer.responses;

import java.util.ArrayList;

public class JSTreeFolder {

	private String id;
	private String text;
	private ArrayList<String> children;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public ArrayList<String> getChildren() {
		return children;
	}
	public void setChildren(ArrayList<String> children) {
		this.children = children;
	}
	
}
