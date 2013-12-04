package com.kmeans;

import com.kmeans.algorithm.KMeans;

public class MainApp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		KMeans kMeans = new KMeans(3, 200);
		kMeans.execute();
	}

}
