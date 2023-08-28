package vrp;

import java.util.*;

public class Lns {

	private final Random rand;

	public Lns(Random rand) {
		this.rand = rand;
	}

	public VrpSolution remove(VrpSolution sol, int numToRemove) {
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
		while(removedCustomers.size() != numToRemove){
			// take a random removed node
			int removeCustomerId = (int) (rand.nextDouble() * problem.getNumCustomers());
			if(removeCustomerId == 0 || removedCustomers.contains(removeCustomerId)) continue;
			remainingCustomers.remove(removeCustomerId);
			removedCustomers.add(removeCustomerId);
		}
		// build the new solution
		List<List<Integer>> newRoutes = buildRoutesWithoutRemovedCusts(sol.getRoutes(), removedCustomers);

		return new VrpSolution(newRoutes, removedCustomers, problem);
	}

	public List<List<Integer>> buildRoutesWithoutRemovedCusts(List<List<Integer>> routes, List<Integer> removedCustomers) {
		List<List<Integer>> newRoutes = new ArrayList<List<Integer>>(routes.size());
		for (List<Integer> oldRoute : routes) {
			List<Integer> newRoute = new ArrayList<Integer>();
			newRoutes.add(newRoute);
			for (Integer customerId : oldRoute) {
				if (!removedCustomers.contains(customerId)) {
					newRoute.add(customerId);
				}
			}
		}

		return newRoutes;
	}

	public VrpSolution repair(VrpSolution sol){

		List<List<Integer>> routes = sol.getRoutes();
		VrpProblem problem = sol.getProblem();
		List<Integer> removedCustomers = sol.getUnrouted();

		ArrayList<Integer> assignedCustomers = new ArrayList<>();
		for(List<Integer> route : routes) {
			for(Integer customerId : route) {
				assignedCustomers.add(customerId);
			}
		}

		List<List<Integer>> newRoutes = new ArrayList<List<Integer>>(routes.size());
		while(!removedCustomers.isEmpty()) {
			int removedId = removedCustomers.get(0);

			double min = Double.MAX_VALUE;
			int minNodeId = 0;
			// find nearest customer from removedCustomer
			for(Integer assignedId : assignedCustomers) {
				double dis = problem.calDis(removedId, assignedId);
				if(dis < min) {
					min = dis;
					minNodeId = assignedId;
				}
			}

			// insert removedCustomer to route including nearest customer
			for(List<Integer> route : routes) {
				if(route.contains(minNodeId)) {
					sol.insertCustomer(route, minNodeId, removedId);
					break;
				}
//				newRoutes.add(route);
			}

			removedCustomers.remove(Integer.valueOf(removedId));

		}

		return new VrpSolution(routes, problem);
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
