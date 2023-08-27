package heuristic.vrp;

import java.util.ArrayList;
import java.util.List;

public class VrpProblem {

	private int numCustomers;
	private int numVehicles;
	private int[] startDay;
	private double[][] dists;
	private int[] serviceTimes;

	private int workingTime;
	private List<List<Integer>> availableDays = new ArrayList<>();
	private double[][] travelTimes;
//	private double[] distsFromDepot;

	public VrpProblem(int numCustomers, int[] startDay, double[][] dists, int[] serviceTimes) {
		this.numCustomers = numCustomers;
		setNumVehicles(2);
		this.startDay = startDay;
		this.dists = dists;
		this.serviceTimes = serviceTimes;

		setWorkingTime(240);
		buildAvaiableDays();
		buildTravelTimes();
	}

	public void setNumVehicles(int numVehicles) {
		this.numVehicles = numVehicles;
	}
	
	public void setWorkingTime(int workingTime) {
		this.workingTime = workingTime;
	}

	public void buildAvaiableDays() {
		// 6월기준 30일까지 
		int endDay = 31;
		for (int i = 0; i < n; i++) {
			availableDays.add(new ArrayList<>());
			for (int day = startDay[i]; day < endDay; day++) {
				availableDays.get(i).add(day);
//				System.out.println(T.get(i).get(t-1));
			}
		}
	}
	
	public void buildTravelTimes() {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (i == j)
					travelTimes[i][j] = 0;
				else
					travelTimes[i][j] = (dists[i][j]) / 1000.;
			}
		}
		// 시간 = 거리/속력
		// 60km/h = 1000m/m
	}
	
	public int getNumCustomers() {
		return this.numCustomers;
	}

	public double[][] getDistances() {
		// TODO Auto-generated method stub
		return this.dists;
	}

}
