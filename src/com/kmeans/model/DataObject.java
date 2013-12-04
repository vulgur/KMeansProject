package com.kmeans.model;

import java.text.DecimalFormat;

public class DataObject {
	private String id;
	private String name;
	private int year;
	private double ratio1;
	private double ratio2;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public double getRatio1() {
		return ratio1;
	}

	public void setRatio1(double ratio1) {
		this.ratio1 = ratio1;
	}

	public double getRatio2() {
		return ratio2;
	}

	public void setRatio2(double ratio2) {
		this.ratio2 = ratio2;
	}

	@Override
	public String toString() {
		// return "DataObject [id=" + id + ", name=" + name + ", year=" + year
		// + ", ratio1=" + ratio1 + ", ratio2=" + ratio2 + "]";
		DecimalFormat df = new DecimalFormat("0.00");
		return "[" + df.format(ratio1) + "," + year + "," + df.format(ratio2) + "],";
	}
}
