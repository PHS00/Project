package vrp;

import java.util.*;

public class Lns {

	private final Random rand;
	private VrpProblem problem;

	public Lns(VrpProblem problem, Random rand) {
		this.problem = problem;
		this.rand = rand;
	}

	public void remove(VrpSolution sol, int numToRemove, Integer date) {
		ArrayList<Integer> removedSites = new ArrayList<>();
		ArrayList<Integer> remainingSites = new ArrayList<>();
		int depot = 0;

		// 해당하는 날짜 객체를 가져옴
		Date d = sol.getRouteOfDate(date);
		List<List<Integer>> consideringRoutes = d.getRoutes();
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
			double random = rand.nextDouble();    // 0 ~ 1 사이 값
			int index = (int) (random * remainingSites.size());
			Integer removeSiteId = remainingSites.get(index);
			remainingSites.remove(removeSiteId);
			removedSites.add(removeSiteId);
		}
		// build the new solution
		List<List<Integer>> newRoutes = buildRoutesWithoutRemovedSites(consideringRoutes, removedSites);
		d.updateRoutes(newRoutes);
		// Date 객체의 경로 검사
		for(List<Integer> route : d.getRoutes()){
			if(route.size() == 2)
				route.clear();
		}
		if(d.getRoutes().isEmpty()){
			sol.getDates().remove(d);
			sol.getNoRoutesDates().add(date);
			sol.getHaveRoutesDates().remove(date);
		}
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

		List<Date> dateList = sol.getDates();
		VrpProblem problem = sol.getProblem();
		List<Integer> removedSites = sol.getRemovedSites();
		List<Integer> noRoutesDates = sol.getNoRoutesDates();
		List<Integer> haveRoutesDates = sol.getHaveRoutesDates();

		List<Integer> assignedSites = new ArrayList<>();
		List<List<Integer>> availableRoutes = new ArrayList<>();

		List<Site> sites = problem.getSites();

		while(!removedSites.isEmpty()) {
			Integer removedId = removedSites.get(0);    // 삽일할 노드
			// 노드에 저장된 방문가능한 날짜를 불러옴
			Site site = sites.get(removedId);
			int availableDate = site.getAvailableDate();

			availableRoutes.clear();
			assignedSites.clear();
			for(Date r : dateList){
				// 1. 가능한 날짜 이후의 경로만 고려
				if(r.getDate() > availableDate){
					for(List<Integer> route : r.getRoutes()) {
						// 2. 삽입할 노드의 서비스 타임 + 경로 총 소요시간 < 설정한 시간 이 조건을 만족하는 경로들만 탐색
						if(sol.calWorkingTime(route) + site.getServiceTime() < problem.getTimeLimit()){
							for(Integer SiteId : route) {
								// 삽입가능한 경로 리스트 추가
								availableRoutes.add(route);
								if(!assignedSites.contains(SiteId))
									assignedSites.add(SiteId);
							}
						}
					}
				}
			}
			// 기존 경로 중 아무 곳에도 삽입 될 수 없는 경우는??
			// 삽입될 수 있는 날짜 중 경로가 없는 date 가운데 랜덤으로 하나 뽑아서 경로 생성
			// 삽입할 수 있는 경로가 없다면
			if(availableRoutes.isEmpty()){
				// 어느 날짜에 삽입할지 랜덤으로 결정
				double random = rand.nextDouble();
				int index = (int) random * noRoutesDates.size();
				Integer date = noRoutesDates.get(index);

				// 해당 날짜에 경로 생성 후 삽입
				insertWithoutMinSite(date, removedId, sol);
				removedSites.remove(removedId);

				noRoutesDates.remove(date);
				haveRoutesDates.add(date);
				continue;
			}
			System.out.print("최소값을 찾기 위한 할당된 site id : ");
			for(Integer id : assignedSites){
				System.out.print(id + " ");
			}
			System.out.println();


			// find nearest customer from removedCustomer
			Integer minSiteId = findMinSite(problem, removedId, assignedSites);
			System.out.println("가장 가까운 현장 id : " + minSiteId);

			// insert removed site to route including nearest site
			for(List<Integer> route : availableRoutes) {
				if (route.contains(minSiteId)) {
					insertSite(route, minSiteId, removedId);
					removedSites.remove(removedId);
					break;
				}
			}

		}

	}

	public void insertWithoutMinSite(int date, Integer removedId, VrpSolution sol){
		// 새로운 Date 객체 생성
		Date D = new Date(date);
		// 새로운 경로 생성
		List<Integer> route = new ArrayList<>();

		// 출발지
		route.add(0);
		// 방문할 현장 추가
		route.add(removedId);
		// 도착지
		route.add(0);

		// 해당 날짜 경로에 추가
		D.addRoute(route);
		sol.addDate(D);

	}

	public Integer findMinSite(VrpProblem problem, Integer removedId, List<Integer> assignedSites){
		double min = Double.MAX_VALUE;
		int minSiteId = 0;
		for(Integer assignedId : assignedSites) {
			double dis = problem.getDis(removedId, assignedId);
			if(dis < min) {
				min = dis;
				minSiteId = assignedId;
			}
		}
		return minSiteId;
	}

	public void insertSite(List<Integer> route, Integer minId, Integer insertId) {
		// TODO : 삽입할때 유효성 검사 해야함

		if(minId == 0){
			List<Integer> route_init = copyRoute(route);
			List<Integer> route_end = copyRoute(route);

			route_init.add(1, insertId);
			route_end.add(route.size()-1,insertId);

			if(calCost(route_init) < calCost(route_end)){
				// case insert init
				route.add(1, insertId);
			}else{
				// case insert end
				route.add(route.size()-1,insertId);
			}
			return;
		}

		// 경로상 가장 가까운 현장의 위치를 찾는다
		int index = route.indexOf(minId);

		List<Integer> route_back = copyRoute(route);
		List<Integer> route_front = copyRoute(route);

		route_back.add(index+1, insertId);
		route_front.add(index, insertId);

		if(calCost(route_back) < calCost(route_front)){
			route.add(index+1, insertId);
		}else{
			route.add(index, insertId);
		}
	}

	public List<Integer> copyRoute(List<Integer> route){
		List<Integer> copyRoute = new ArrayList<>();
		for(Integer Site : route){
			copyRoute.add(Site);
		}
		return copyRoute;
	}


	public double calCost(List<Integer> route){
		double cost = 0.;
		for(int i = 0; i < route.size()-1; i++) {
			Integer now = route.get(i);
			Integer next = route.get(i + 1);
			cost += problem.getDis(now, next);
		}
		return cost;
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
