package vrp;

import java.util.*;

public class Lns {

	private final Random rand;

	public Lns(Random rand) {
		this.rand = rand;
	}

	public void remove(VrpSolution sol, int numToRemove, int date) {
		ArrayList<Integer> removedSites = new ArrayList<>();
		ArrayList<Integer> remainingSites = new ArrayList<>();
		int depot = 0;

		// 해당하는 날짜의 경로를 가져옴
		Routes routes = sol.getRouteOfDate(date);
		List<List<Integer>> consideringRoutes = routes.getRoutes();
		for (List<Integer> route : consideringRoutes) {
			for (Integer siteID : route) {
				if(siteID == depot) continue;
				remainingSites.add(siteID);
			}
		}

		// remove
		while(removedSites.size() != numToRemove){
			// take a random removed node
			// 해당 날짜에 경로에 포함되어 있는 sites 중 제거할 인덱스를 하나 추출해야함
			double random = rand.nextDouble();	// 0 ~ 1 사이 값
			int index = (int) (random * remainingSites.size());
			Integer removeSiteId = remainingSites.get(index);
			remainingSites.remove(removeSiteId);
			removedSites.add(removeSiteId);
		}
		// build the new solution
		List<List<Integer>> newRoutes = buildRoutesWithoutRemovedSites(consideringRoutes, removedSites);
		routes.updateRoutes(newRoutes);
		sol.addRemovedSites(removedSites);

	}

	public List<List<Integer>> buildRoutesWithoutRemovedSites(List<List<Integer>> routes, List<Integer> removedSites) {
		List<List<Integer>> newRoutes = new ArrayList<>(routes.size());
		for (List<Integer> oldRoute : routes) {
			List<Integer> newRoute = new ArrayList<>();
			newRoutes.add(newRoute);
			for (Integer siteID : oldRoute) {
				if (!removedSites.contains(siteID)) {
					newRoute.add(siteID);
				}
			}
		}
		return newRoutes;
	}

	public void repair(VrpSolution sol){

		List<Routes> RoutesList = sol.getRoutes();
		VrpProblem problem = sol.getProblem();
		List<Integer> removedSites = sol.getRemovedSites();

		ArrayList<Integer> assignedCustomers = new ArrayList<>();

		List<Site> sites = problem.getSites();

		while(!removedSites.isEmpty()) {
			Integer removedId = removedSites.get(0);	// 삽일할 노드
			// 노드에 저장된 방문가능한 날짜를 불러옴
			Site site = sites.get(removedId);
			int availableDate = site.getAvailableDate();

			for(Routes r : RoutesList){
				// 1. 가능한 날짜 이후의 경로만 고려
				if(r.getDate() > availableDate){
					for(List<Integer> route : r.getRoutes()) {
						// 2. 삽입할 노드의 서비스 타임 + 경로 총 소요시간 < 설정한 시간 이 조건을 만족하는 경로들만 탐색
						if(sol.calTravelTime(route) + site.getServiceTime() < problem.getWorkingTime()){
							for(Integer SiteId : route) {
								if(!assignedCustomers.contains(SiteId))
									assignedCustomers.add(SiteId);
							}
						}
					}
				}
			}

			double min = Double.MAX_VALUE;
			int minSiteId = 0;
			// find nearest customer from removedCustomer
			for(Integer assignedId : assignedCustomers) {
				double dis = problem.calDis(removedId, assignedId);
				if(dis < min) {
					min = dis;
					minSiteId = assignedId;
				}
			}

			// insert removed site to route including nearest site
			 a : for(Routes r : RoutesList){
				for(List<Integer> route : r.getRoutes()) {
					if(route.contains(minSiteId)) {
						sol.insertSite(route, minSiteId, removedId);
						break a;
					}
				}
			}

			removedSites.remove(removedId);

		}

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
