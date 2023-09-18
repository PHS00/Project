package vrp;

import java.util.ArrayList;
import java.util.List;

public class VrpSolution {

//	private List<List<Integer>> routes;
	private List<Integer> removedSites = new ArrayList<>();
	private VrpProblem problem;
//	private int date;

	private List<Date> dates;
	private List<Integer> haveRoutesDates;
	private List<Integer> noRoutesDates;


//	public VrpSolution(List<List<Integer>> routes, VrpProblem problem) {
//		this.routes = routes;
//		this.problem = problem;
//	}
	public VrpSolution(List<Date> dates, VrpProblem problem){
		this.dates = dates;
		this.problem = problem;
		buildHaveRoutesDates();
		buildNoRoutesDates();
	}

//	public VrpSolution(List<List<Integer>> routes, List<Integer> removedCustomers, VrpProblem problem) {
//		this(routes, problem);
//		this.unrouted = removedCustomers;
//	}
	public VrpSolution(List<Date> routes, List<Integer> removedSites, VrpProblem problem) {
		this(routes, problem);
		this.removedSites = removedSites;
	}

	public void buildHaveRoutesDates(){
		haveRoutesDates = new ArrayList<>();
		for(Date d : dates){
			haveRoutesDates.add(d.getDate());
		}
	}

	public void buildNoRoutesDates(){
		noRoutesDates = new ArrayList<>();
		int endDate = 31;
		for(int date = 1; date < endDate; date++){
			if(!haveRoutesDates.contains(date))
				noRoutesDates.add(date);
		}
	}


	public VrpProblem getProblem() {
		return this.problem;
	}

	public List<Integer> getHaveRoutesDates(){
		return this.haveRoutesDates;
	}
	public List<Integer> getNoRoutesDates(){
		return this.noRoutesDates;
	}
	public List<Date> getDates(){
		return this.dates;
	}

	public Date getRouteOfDate(int date){
		for(Date d : this.dates){
			if(date == d.getDate()){
				return d;
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

	public void addDate(Date date){
		this.dates.add(date);
	}

	public void addDatesList(List<Date> dateList){
		for(Date r : dateList){
			this.dates.add(r);
		}
	}

	public double calTotalCost() {
		double cost = 0.;
		double perCost = 0.;
		for (Date date : dates) {
			for (List<Integer> route : date.getRoutes()) {
				for (int i = 0; i < route.size() - 1; i++) {
					int now = route.get(i);
					int next = route.get(i + 1);
					cost += problem.getDis(now, next);
					perCost += problem.getDis(now, next);
				}
			}
			date.setTotalCost(perCost);
			perCost = 0.;
		}
		return cost;
	}

	public double calWorkingTime(List<Integer> route){
		double workingTime = 0.;
		for (int i = 0; i < route.size() - 1; i++) {
			int now = route.get(i);
			int next = route.get(i + 1);
			workingTime += problem.getTravelTime(now, next) + problem.getServiceTimeOfSite(next);
		}
		return workingTime;
	}

	public void updateWorkingTime(){

	}

	public void showRoutes(){
		for (Date d : dates) {
			System.out.println(d.getDate() + "일의 차량 경로");
			for (List<Integer> route : d.getRoutes()) {
				for (int i = 0; i < route.size(); i++) {
					System.out.print(route.get(i) + " ");
				}
				System.out.print("Working Time per route : " + calWorkingTime(route));
				System.out.println();
			}
		}
	}


}
