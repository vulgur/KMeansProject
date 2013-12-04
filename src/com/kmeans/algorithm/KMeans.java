package com.kmeans.algorithm;

import java.util.ArrayList;

import javax.swing.JCheckBox;

import com.kmeans.model.DataObject;

public class KMeans {
	private ArrayList<DataObject> objects = null;
	private ArrayList<DataObject> centers = null;
	private ArrayList<ArrayList<DataObject>> clusters = null;
	private ArrayList<Double> errors = null;
	private int k = 0; // numbers of clusters
	private int n = 0; // numbers of data objects
	private int m = 0; // numbers of iteration
	private final static double MAX_RATIO = 0.3;
	private final static int MAX_YEAR = 20;

	// initialize objects
	private void initDataObjects() {
		objects = new ArrayList<DataObject>();
		for (int i = 0; i < n; i++) {
			DataObject obj = new DataObject();
			int year = (int) (Math.random() * MAX_YEAR);
			double ratio1 = Math.random() % MAX_RATIO;
			double ratio2 = Math.random() % MAX_RATIO;
			obj.setYear(year);
			obj.setRatio1(ratio1);
			obj.setRatio2(ratio2);
			objects.add(obj);
		}
	}

	// initialize centers
	private void initCenters() {
		centers = new ArrayList<DataObject>();
		for (int i = 0; i < k; i++) {
			centers.add(objects.get(i));
		}
	}

	// initialize clusters
	private void initClusters() {
		clusters = new ArrayList<ArrayList<DataObject>>();
		for (int i = 0; i < k; i++) {
			clusters.add(new ArrayList<DataObject>());
		}
	}

	// get the distance between two data objects
	private double getDistance(DataObject o1, DataObject o2) {
		double distance = 0.0;
		distance = (o1.getYear() - o2.getYear())
				* (o1.getYear() - o2.getYear())
				+ (o1.getRatio1() - o2.getRatio1())
				* (o1.getRatio1() - o2.getRatio1())
				+ (o1.getRatio2() - o2.getRatio2())
				* (o1.getRatio2() - o2.getRatio2());
		return distance;
	}

	// get the nearest center data object
	private int getNearestCenterIndex(DataObject obj,
			ArrayList<DataObject> centers) {
		int centerIndex = 0;
		double minDistance = Double.MAX_VALUE;
		double distance = 0.0;
		for (int i = 0; i < centers.size(); i++) {
			DataObject dataObject = centers.get(i);
			distance = getDistance(obj, dataObject);
			if (distance < minDistance) {
				minDistance = distance;
				centerIndex = i;
			}
		}
		return centerIndex;
	}

	// update all centers
	private void updateAllCenters() {
		for (int i = 0; i < k; i++) {
			int num = clusters.get(i).size();
			if (num != 0) {
				int years = 0;
				double ratio1 = 0.0;
				double ratio2 = 0.0;
				// get sum of x and y of the cluster
				for (int j = 0; j < num; j++) {
					years += clusters.get(i).get(j).getYear();
					ratio1 += clusters.get(i).get(j).getRatio1();
					ratio2 += clusters.get(i).get(j).getRatio2();
				}
				// get average x and y and set them to the new center
				DataObject newCenter = new DataObject();
				newCenter.setYear(years / num);
				newCenter.setRatio1(ratio1 / num);
				newCenter.setRatio2(ratio2 / num);
				centers.set(i, newCenter);
			}
		}
	}

	// get the error square
	private double getErrorSquare(DataObject o1, DataObject o2) {
		return getDistance(o1, o2);
	}

	// count the errors
	private void countErrors() {
		double error = 0.0;
		for (int i = 0; i < clusters.size(); i++) {
			for (int j = 0; j < clusters.get(i).size(); j++) {
				error += getErrorSquare(clusters.get(i).get(j), centers.get(i));
			}
		}
		errors.add(error);
	}

	// cluster all data objects
	private void cluster() {
		int index = 0;
		for (DataObject obj : objects) {
			index = getNearestCenterIndex(obj, centers);
			clusters.get(index).add(obj);
		}
	}

	private void init() {
		m = 0;
		initDataObjects();
		initCenters();
		initClusters();
		errors = new ArrayList<>();
	}

	private void printClusters() {
		for (int i = 0; i < clusters.size(); i++) {
			System.out.println("Cluster " + i + " size: " + clusters.get(i).size());
			for (DataObject obj : clusters.get(i)) {
				System.out.println(obj);
			}
		}
	}

	public KMeans(int k, int n) {
		super();
		this.k = k;
		this.n = n;
	}

	public void execute() {
		init();
		long startTime = System.currentTimeMillis();
		while (true) {
			cluster();
			countErrors();
			if (m != 0) {
				if (errors.get(m) - errors.get(m - 1) == 0) {
					break;
				}
			}
			updateAllCenters();
			m++;
			clusters.clear();
			initClusters();
		}
		long endTime = System.currentTimeMillis();
		System.out.println("Running time = " + (endTime - startTime) + "ms");
		printClusters();
	}
	
	public ArrayList<ArrayList<DataObject>> getClusters() {
		execute();
		return clusters;
	}
}
