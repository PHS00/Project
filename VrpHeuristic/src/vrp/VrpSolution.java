package vrp;

import java.util.ArrayList;
import java.util.List;

public class VrpSolution {

	private List<List<Integer>> routes; 
	private List<Integer> unrouted;  
	private VrpProblem problem;

	public VrpSolution(List<List<Integer>> routes, VrpProblem problem) {
		this.routes = routes;
		this.problem = problem;
	}

	public VrpSolution(List<List<Integer>> routes, List<Integer> removedCustomers, VrpProblem problem) {
		this(routes, problem);
		this.unrouted = removedCustomers;
	}
	
	public VrpProblem getProblem() {
		return this.problem;
	}
	
	public List<List<Integer>> getRoutes(){
		return this.routes;
	}
	
	public List<Integer> getUnrouted(){
		return this.unrouted;
	}
	
	
	
	
	public void insertCustomer(List<Integer> route, Integer minNodeId, Integer insertId) {
		// TODO : 삽입할때 유효성 검사 해야함 
		
		if(minNodeId == 0){
			List<Integer> route_init = new ArrayList<>();
			List<Integer> route_end = new ArrayList<>();
			for(Integer customer : route){
				route_init.add(customer);
				route_end.add(customer);
			}
			route_init.add(1, insertId);
			route_end.add(route.size()-1,insertId);
			if(compareRouteCost(route_init, route_end)){
				// case insert init
				route.add(1, insertId);
			}else{
				// case insert end
				route.add(route.size()-1,insertId);
			}
			return;
		}

		int index = route.indexOf(minNodeId);

		List<Integer> route_back = new ArrayList<>();
		List<Integer> route_front = new ArrayList<>();

		for(Integer customer : route) {
			route_back.add(customer);
			route_front.add(customer);
		}
		route_back.add(index+1, insertId);
		route_front.add(index, insertId);

		if(compareRouteCost(route_back, route_front)){
			route.add(index+1, insertId);
		}else{
			route.add(index, insertId);
		}

//		int index = route.indexOf(minNodeId);
//		int pre = index - 1;
//		int next = index + 1;
//		double cost_b = problem.calDis(pre, minNodeId) + problem.calDis(insertId, next);
//		double cost_f = problem.calDis(pre, insertId) + problem.calDis(minNodeId, next);
//		if(cost_b < cost_f){
//			route.add(index+1, insertId);
//		}else {
//			route.add(index, insertId);
//		}
		
//		return compareRouteCost(route_back, route_front);

	}
	
	public boolean compareRouteCost(List<Integer> route1, List<Integer> route2){
		
		double cost_b = 0.;
		for(int i = 0; i < route1.size()-1; i++) {
			Integer now = route1.get(i);
			Integer next = route1.get(i + 1);
			cost_b += problem.calDis(now, next);
		}
		
		double cost_f = 0.;
		for(int i = 0; i < route2.size()-1; i++) {
			Integer now = route2.get(i);
			Integer next = route2.get(i + 1);
			cost_f += problem.calDis(now, next);
		}

		return cost_b < cost_f;
	}

}
