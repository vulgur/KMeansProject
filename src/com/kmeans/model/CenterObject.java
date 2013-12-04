package com.kmeans.model;

import java.util.ArrayList;

public class CenterObject {
	private ArrayList<DataObject> objs;
	private float fitness;

	
	public float getFitness() {
		return fitness;
	}

	public void setFitness(float fitness) {
		this.fitness = fitness;
	}

	public CenterObject() {
		objs = new ArrayList<DataObject>();
	}

	public ArrayList<DataObject> getObjs() {
		return objs;
	}

	public void setObjs(ArrayList<DataObject> objs) {
		this.objs = objs;
	}

}
