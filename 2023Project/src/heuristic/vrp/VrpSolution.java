package heuristic.vrp;

import java.util.List;

public class VrpSolution {

	private List<List<Integer>> routes; 
	private List<Integer> unrouted;  
	private VrpProblem problem;

	public VrpSolution(List<List<Integer>> routes, VrpProblem problem) {
		this.routes = routes;
		this.problem = problem;
	}

	public VrpSolution(List<List<Integer>> routes, List<Integer> unroutedNodes, VrpProblem problem) {
		this(routes, problem);
		this.unrouted = unroutedNodes;
	}
	
	public VrpProblem getProblem() {
		return this.problem;
	}
	
	public List<List<Integer>> getRoutes(){
		return this.routes;
	}
	
	public List<Integer> insertCustomer(List<Integer> route, Integer minNodeId, Integer removedId) {
		// TODO : 삽입할때 유효성 검사 해야함 
		List<Integer> route_back = route;
		int index_b = route_back.indexOf(minNodeId);
		route_back.add(index_b+1, removedId);
		
		List<Integer> route_front = route;
		int index_f = route_front.indexOf(minNodeId);
		route_front.add(index_f+1, removedId);
		
		return compareRouteCost(route_back, route_front);
	}
	
	public List<Integer> compareRouteCost(List<Integer> route1, List<Integer> route2){
		
		double cost_b = 0.;
		for(int i = 0; i < route1.size()-1; i++) {
			Integer now = route1.get(i);
			Integer next = route1.get(i + 1);
			
			cost_b += problem.calDis(now, next);
		}
		
		double cost_f = 0.;
		
		for(int i = 0; i < route1.size()-1; i++) {
			Integer now = route1.get(i);
			Integer next = route1.get(i + 1);
			
			cost_f += problem.calDis(now, next);
		}
		
		return cost_b < cost_f ? route1 : route2;
	}

}
