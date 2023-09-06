package vrp;

import java.util.ArrayList;
import java.util.List;

public class VrpSolution {

//	private List<List<Integer>> routes;
	private List<Integer> removedSites;
	private VrpProblem problem;
//	private int date;

	private List<Routes> routes;


//	public VrpSolution(List<List<Integer>> routes, VrpProblem problem) {
//		this.routes = routes;
//		this.problem = problem;
//	}
	public VrpSolution(List<Routes> routes, VrpProblem problem){
		this.routes = routes;
		this.problem = problem;
	}

//	public VrpSolution(List<List<Integer>> routes, List<Integer> removedCustomers, VrpProblem problem) {
//		this(routes, problem);
//		this.unrouted = removedCustomers;
//	}
	public VrpSolution(List<Routes> routes, List<Integer> removedSites, VrpProblem problem) {
		this(routes, problem);
		this.removedSites = removedSites;
	}
	
	public VrpProblem getProblem() {
		return this.problem;
	}
	
//	public List<List<Integer>> getRoutes(){
//		return this.routes;
//	}
	public List<Routes> getRoutes(){
		return this.routes;
	}
	public Routes getRouteOfDate(int date){
		for(Routes r : this.routes){
			if(date == r.getDate()){
				return r;
			}
		}
		return null;
	}


	public List<Integer> getRemovedSites(){
		return this.removedSites;
	}

	public void setRemovedSites(List<Integer> removedSites){
		this.removedSites = removedSites;
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


	public double calCost(List<Integer> route1){
		double cost = 0.;
		for(int i = 0; i < route1.size()-1; i++) {
			Integer now = route1.get(i);
			Integer next = route1.get(i + 1);
			cost += problem.calDis(now, next);
		}
		return cost;
	}

}
