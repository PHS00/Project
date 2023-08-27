package heuristic.vrp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

public class Lns {

	private int randomnessMeasure;
	private double maxDist; // used for normalizing distances for relatedness measure
	private final Random rand;

	public Lns(int randomnessMeasure, double maxDist, Random rand) {
		this.randomnessMeasure = randomnessMeasure;
		this.maxDist = maxDist;
		this.rand = rand;
	}

	public VrpSolution remove(VrpSolution sol, int numToRemove, int firstToRemove) {
		VrpProblem problem = sol.getProblem();
		ArrayList<Integer> removedCustomers = new ArrayList<Integer>(numToRemove);
		HashSet<Integer> remainingCustomers = new HashSet<Integer>();

		int[] customerVehicles = new int[problem.getNumCustomers()];
		int vehicle = 0;
		// check each vehicle visited customer
		for (List<Integer> route : sol.getRoutes()) {
			for (int customerId : route) {
				customerVehicles[customerId] = vehicle;
			}
			vehicle++;
		}

		// choose first to remove
		if (firstToRemove == -1) {
			firstToRemove = (int) (rand.nextDouble() * problem.getNumCustomers());
		}
		for (int i = 0; i < problem.getNumCustomers(); i++) {
			if (i == firstToRemove) {
				removedCustomers.add(i);
			} else {
				remainingCustomers.add(i);
			}
		}
		// remove the rest
		for (int i = 1; i < numToRemove; i++) {
			// take a random removed node
			int removeCustomerId = removedCustomers.get((int) (rand.nextDouble() * removedCustomers.size()));
			int rank = (randomnessMeasure == Integer.MAX_VALUE) ? 0
					: (int) (Math.pow(rand.nextDouble(), randomnessMeasure) * remainingCustomers.size());
			int customerId = chooseByRankAndRelatedness(remainingCustomers, sol, rank, removeCustomerId,
					customerVehicles);
			remainingCustomers.remove(customerId);
			removedCustomers.add(customerId);
		}
		// build the new solution
		List<List<Integer>> newRoutes = buildRoutesWithoutRemovedCusts(sol.getRoutes(), removedCustomers);

		return new VrpSolution(newRoutes, removedCustomers, problem);
	}

	public List<List<Integer>> buildRoutesWithoutRemovedCusts(List<List<Integer>> routes, List<Integer> toRemove) {
		HashSet<Integer> removedCustomersSet = new HashSet<Integer>(toRemove);
		List<List<Integer>> newRoutes = new ArrayList<List<Integer>>(routes.size());
		for (List<Integer> oldRoute : routes) {
			List<Integer> newRoute = new ArrayList<Integer>();
			newRoutes.add(newRoute);
			for (Integer customerId : oldRoute) {
				if (!removedCustomersSet.contains(customerId)) {
					newRoute.add(customerId);
				}
			}
		}

		return newRoutes;
	}

	private int chooseByRankAndRelatedness(HashSet<Integer> remaining, VrpSolution sol, int rank, int cityId,
			int[] cityVehicles) {
		// the head is the least element, i.e. the one whose compareTo(any other
		// element) is less than 0
		// we want the head of the queue to be the least related customer
		// thus, we want compareTo to return negative if the argument is more related
		// than us
		PriorityQueue<CityRelatedness> heap = new PriorityQueue<CityRelatedness>(rank + 1);
		for (int remainingCityId : remaining) {
			double relatedness = relatedness(cityId, remainingCityId, sol, cityVehicles);
			if (heap.size() < rank + 1 || relatedness > heap.peek().relatedness) {
				if (heap.size() == rank + 1) {
					heap.remove();
				}
				heap.add(new CityRelatedness(remainingCityId, relatedness));
			}
		}
		return heap.peek().cityId;
	}

	private double relatedness(int nodeId1, int nodeId2, VrpSolution sol, int[] cityVehicles) {
		double dist = sol.getProblem().getDistances()[nodeId1][nodeId2];
		double denom = dist / maxDist;
		if (cityVehicles[nodeId1] == cityVehicles[nodeId2]) {
			denom += 1.0;
		}
		return 1 / denom;
	}
	
	private class CityRelatedness implements Comparable<CityRelatedness> {
	    public int cityId;
	    public double relatedness;

	    public CityRelatedness(int cityId, double relatedness) {
	      this.cityId = cityId;
	      this.relatedness = relatedness;
	    }
	    
	    @Override
	    public int compareTo(CityRelatedness other) {
	      return (int)Math.signum(this.relatedness - other.relatedness);
	    }
	  }

}
