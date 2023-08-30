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
		ArrayList<Integer> remainingCustomers = new ArrayList<Integer>();

		int[] customerVehicles = new int[problem.getNumCustomers()];
		int vehicle = 0;
		// check each vehicle visited customer
		for (List<Integer> route : sol.getRoutes()) {
			for (int customerId : route) {
				customerVehicles[customerId] = vehicle;
				remainingCustomers.add(customerId);
			}
			vehicle++;
		}

		// choose first to remove
	
		// remove the rest
		for (int i = 0; i < numToRemove; i++) {
			// take a random removed node
			int removeCustomerId = (int) (rand.nextDouble() * problem.getNumCustomers());
			remainingCustomers.remove(removeCustomerId);
			removedCustomers.add(removeCustomerId);
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
	
	public List<List<Integer>> repair(VrpSolution sol, ArrayList<Integer> removedCustomers,
			ArrayList<Integer> remainingCustomers){
		
		List<List<Integer>> routes = sol.getRoutes();
		VrpProblem problem = sol.getProblem();
		
		
		while(!removedCustomers.isEmpty()) {
			int removedId = removedCustomers.get(0);
			
			double min = Double.MAX_VALUE;
			int minNodeId = 0;
			// find nearest customer from removedCustomer
			for(Integer remainingId : remainingCustomers) {
				double dis = problem.calDis(removedId, remainingId);
				if(dis < min) {
					min = dis;
					minNodeId = remainingId;
				}
			}
			
			// insert removedCustomer to route including nearest customer
			for(List<Integer> route : routes) {
				if(route.contains(minNodeId)) {
					route = sol.insertCustomer(route, minNodeId, removedId);
				}
			}
			
			removedCustomers.remove(removedId);
			
		}
		
		return null;
	}
	
	
	

//	private int chooseByRankAndRelatedness(HashSet<Integer> remaining, VrpSolution sol, int rank, int cityId,
//			int[] cityVehicles) {
//		// the head is the least element, i.e. the one whose compareTo(any other
//		// element) is less than 0
//		// we want the head of the queue to be the least related customer
//		// thus, we want compareTo to return negative if the argument is more related
//		// than us
//		PriorityQueue<CityRelatedness> heap = new PriorityQueue<CityRelatedness>(rank + 1);
//		for (int remainingCityId : remaining) {
//			double relatedness = relatedness(cityId, remainingCityId, sol, cityVehicles);
//			if (heap.size() < rank + 1 || relatedness > heap.peek().relatedness) {
//				if (heap.size() == rank + 1) {
//					heap.remove();
//				}
//				heap.add(new CityRelatedness(remainingCityId, relatedness));
//			}
//		}
//		return heap.peek().cityId;
//	}
//
//	private double relatedness(int nodeId1, int nodeId2, VrpSolution sol, int[] cityVehicles) {
//		double dist = sol.getProblem().getDistances()[nodeId1][nodeId2];
//		double denom = dist / maxDist;
//		if (cityVehicles[nodeId1] == cityVehicles[nodeId2]) {
//			denom += 1.0;
//		}
//		return 1 / denom;
//	}
//	
//	private class CityRelatedness implements Comparable<CityRelatedness> {
//	    public int cityId;
//	    public double relatedness;
//
//	    public CityRelatedness(int cityId, double relatedness) {
//	      this.cityId = cityId;
//	      this.relatedness = relatedness;
//	    }
//	    
//	    @Override
//	    public int compareTo(CityRelatedness other) {
//	      return (int)Math.signum(this.relatedness - other.relatedness);
//	    }
//	  }

}
