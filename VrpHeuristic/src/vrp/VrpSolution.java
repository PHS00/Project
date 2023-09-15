package vrp;

import java.util.ArrayList;
import java.util.List;

public class VrpSolution {

//	private List<List<Integer>> routes;
private List<Integer> removedSites = new ArrayList<>();
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

	public void addRemovedSites(List<Integer> removedSites){
		for(Integer removedId : removedSites){
			this.removedSites.add(removedId);
		}
	}

	public void addRoutes(Routes routes){
		this.routes.add(routes);
	}

	public void addRoutesList(List<Routes> routesList){
		for(Routes r : routesList){
			this.routes.add(r);
		}
	}

	public double calTotalCost() {
		double cost = 0.;
		double perCost = 0.;
		for (Routes r : routes) {
			for (List<Integer> route : r.getRoutes()) {
				for (int i = 0; i < route.size() - 1; i++) {
					int now = route.get(i);
					int next = route.get(i + 1);
					cost += problem.getDis(now, next);
					perCost += problem.getDis(now, next);
				}
			}
			r.setTotalCost(perCost);
			perCost = 0.;
		}
		return cost;
	}

	public double calWorkingTime(List<Integer> route){
		double travelTime = 0.;
		for (int i = 0; i < route.size() - 1; i++) {
			int now = route.get(i);
			int next = route.get(i + 1);
			travelTime += problem.getTravelTime(now, next) + problem.getServiceTimeOfSite(next);
		}
		return travelTime;
	}

	public void showRoutes(){
		for (Routes r : routes) {
			System.out.println(r.getDate() + "일의 차량 경로");
			for (List<Integer> route : r.getRoutes()) {
				for (int i = 0; i < route.size(); i++) {
					System.out.print(route.get(i) + " ");
				}
				System.out.print("Working Time per route : " + calWorkingTime(route));
				System.out.println();
			}
		}
	}


}
