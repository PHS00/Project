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

}
