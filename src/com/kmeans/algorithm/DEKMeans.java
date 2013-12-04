package com.kmeans.algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.kmeans.model.CenterObject;
import com.kmeans.model.DataObject;

public class DEKMeans {
	private static final float F_MAX = 0.9F;
	private static final float F_MIN = 0.4F;
	private static final float C_MAX = 0.9F;
	private static final float C_MIN = 0.3F;
	private static final int T_MAX = 5;
	private final static double MAX_RATIO = 0.3;
	private final static int MAX_YEAR = 20;

	private float f = 0.0F;
	private float c = 0.0F;
	private int t = 0;
	private int k = 0; // numbers of clusters
	private int n = 0; // numbers of data objects
	private int np = 0; // numbers of populations

	private ArrayList<DataObject> objects = null;
	private ArrayList<CenterObject> centers = null;
	private ArrayList<CenterObject> originalCenters = null;
	private ArrayList<CenterObject> vCenters = null;
	private ArrayList<CenterObject> uCenters = null;
	private ArrayList<ArrayList<CenterObject>> populations = null;
	// private ArrayList<Float> fits = null;
	private ArrayList<ArrayList<DataObject>> clusters = null;

	private float getFitness(CenterObject center) {
		float fitness = 0.0F;
		for (DataObject c : center.getObjs()) {
			for (DataObject obj : objects) {
				fitness += (obj.getYear() - c.getYear()) * (obj.getYear() - c.getYear())
						+ (obj.getRatio1() - c.getRatio1()) * (obj.getRatio1() - c.getRatio1())
						+ (obj.getRatio2() - c.getRatio2()) * (obj.getRatio2() - c.getRatio2());
			}
		}
		System.out.println("Fitness:" + fitness);
		return fitness;
	}

	public ArrayList<CenterObject> getCenters() {
		return centers;
	}

	public void setCenters(ArrayList<CenterObject> centers) {
		this.centers = centers;
	}

	private float getF() {
		return (F_MAX - F_MIN) * (t / T_MAX) * (t / T_MAX) - (F_MAX - F_MIN) * 2 * t / T_MAX + F_MAX;
	}

	private float getC() {
		return C_MIN + t * (C_MAX - C_MIN) / T_MAX;
	}

	private CenterObject getCross(CenterObject center, int index) {
		float c = getC();
		CenterObject cross = new CenterObject();
		for (int i = 0; i < k; i++) {
			Double rand = Math.random();
			boolean flag = rand > c;
			if (flag) {
				cross.getObjs().add(centers.get(index).getObjs().get(i));
			} else {
				cross.getObjs().add(vCenters.get(index).getObjs().get(i));
			}
		}
		return cross;
	}

	private CenterObject getMutant(CenterObject center, float f) {
		CenterObject mutant = new CenterObject();
		int a = (int) (Math.random() * n);
		int b = (int) (Math.random() * n);
		int c = (int) (Math.random() * n);
//		System.out.println("a b c:" + a + " " + b + " " + c);
		DataObject objA = objects.get(a);
		DataObject objB = objects.get(b);
		DataObject objC = objects.get(c);

		DataObject obj;
		for (int i = 0; i < k; i++) {
			int year = (int) (objA.getYear() + f * (objB.getYear() - objC.getYear()));
			double ratio1 = objA.getRatio1() + f * (objB.getRatio1() - objC.getRatio1());
			double ratio2 = objA.getRatio2() + f * (objB.getRatio2() - objC.getRatio2());
			obj = new DataObject();
			obj.setYear(year);
			obj.setRatio1(ratio1);
			obj.setRatio2(ratio2);
			mutant.getObjs().add(obj);
			System.out.println("Mutant " + i);
			printCenter(mutant);
		}

		return mutant;
	}

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
		System.out.println("Data Size:" + objects.size());
	}

	// initialize centers
	@SuppressWarnings("unchecked")
	private void initCentersList() {

		centers = new ArrayList<>();
		Set<DataObject> doSet = new HashSet<DataObject>();
		for (int i = 0; i < np; i++) {
			doSet.clear();
			// select 3 objs to form a center set
			while (doSet.size() < 3) {
				int index = (int) (Math.random() * n);
				doSet.add(objects.get(index));
			}
			CenterObject center = new CenterObject();
			center.getObjs().addAll(doSet);
			centers.add(center);
		}
		originalCenters = (ArrayList<CenterObject>) centers.clone();
		System.out.println("Original Centers Size:" + originalCenters.size());
	}

	public ArrayList<DataObject> getObjects() {
		return objects;
	}

	public void setObjects(ArrayList<DataObject> objects) {
		this.objects = objects;
	}

	private void init() {
		k = 3;
		n = 100;
		np = 30;
		initDataObjects();
		initCentersList();
		populations = new ArrayList<ArrayList<CenterObject>>();
		// put the first generation into the populations
		populations.add(centers);

		vCenters = new ArrayList<>();
		uCenters = new ArrayList<>();
	}

	private void execute() {
		// step 1
		System.out.println("step 1");
		init();
		while (t < T_MAX) {
			// step 2
			System.out.println("step 2");
			for (CenterObject center : centers) {
				center.setFitness(getFitness(center));
			}

			// step 3
			System.out.println("step 3");
			Float f = getF();
			System.out.println("F is: " + f);
			for (CenterObject center : centers) {
				vCenters.add(getMutant(center, f));
			}

			// step 4
			System.out.println("step 4");
			for (int index = 0; index < centers.size(); index++) {
				CenterObject center = centers.get(index);
				uCenters.add(getCross(center, index));
			}

			// step 5
			System.out.println("step 5");
			for (CenterObject uCenterObject : uCenters) {
				uCenterObject.setFitness(getFitness(uCenterObject));
			}
			for (int i = 0; i < centers.size(); i++) {
				if (uCenters.get(i).getFitness() < centers.get(i).getFitness()) {
					centers.set(i, uCenters.get(i));
				}
			}

			// step 6
			System.out.println("step 6");
			t++;
		}

		// step 7
		System.out.println("step 7");
		float minFit = Float.MAX_VALUE;
		int pos = 0;
		for (int i = 0; i < centers.size(); i++) {
			if (centers.get(i).getFitness() < minFit) {
				minFit = centers.get(i).getFitness();
				pos = i;
			}
		}
		System.out.println("Center Index:" + pos);
		
		CenterObject co = originalCenters.get(pos);
		for (DataObject obj : co.getObjs()) {
			System.out.println(obj);
		}
	}

	private void printCenter(CenterObject center) {
		for (DataObject obj : center.getObjs()) {
			System.out.println(obj);
		}
	}

	public static void main(String[] args) {
		DEKMeans de = new DEKMeans();
		de.execute();
	}
}
