package vrp.test;

import vrp.Lns;
import vrp.VrpProblem;
import vrp.VrpReader;
import vrp.VrpSolution;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Test {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		VrpProblem problem = VrpReader.readDataInstance();

		List<List<Integer>> routes = new ArrayList<>();
		routes.add(new ArrayList<Integer>());
		routes.add(new ArrayList<Integer>());
		routes.add(new ArrayList<Integer>());
		routes.get(0).add(0);
		routes.get(0).add(1);
		routes.get(0).add(2);
		routes.get(0).add(3);
		routes.get(0).add(4);
		routes.get(0).add(0);
		routes.get(1).add(0);
		routes.get(1).add(5);
		routes.get(1).add(6);
		routes.get(1).add(7);
		routes.get(1).add(8);
		routes.get(1).add(9);
		routes.get(1).add(0);
		routes.get(2).add(0);
		routes.get(2).add(10);
		routes.get(2).add(11);
		routes.get(2).add(12);
		routes.get(2).add(13);
		routes.get(2).add(14);
		routes.get(2).add(0);
		
		VrpSolution sol = new VrpSolution(routes, problem);
		Random rand = new Random();
		Lns lns = new Lns(rand);
		
		double cost = 0.;
		for(List<Integer> route : sol.getRoutes()) {
			for(int i = 0; i < route.size()-1; i++) {
				int now = route.get(i);
				int next = route.get(i+1);
				cost += problem.calDis(now, next);
			}
		}
		System.out.println("총 거리 비용 : " + cost);

		sol = lns.remove(sol, 5);
		System.out.print("removedId : ");
		for(Integer removedId : sol.getUnrouted()){
			System.out.print(removedId + " ");
		}
		System.out.println();
		sol = lns.repair(sol);
//		lns.repair(sol);

		cost = 0.;
		for(List<Integer> route : sol.getRoutes()) {
			for(int i = 0; i < route.size()-1; i++) {
				int now = route.get(i);
				int next = route.get(i+1);
				cost += problem.calDis(now, next);
			}
		}
		System.out.println("변경 후 총 거리 비용 : " + cost);
		
		for(List<Integer> route : sol.getRoutes()) {
			for(Integer customerId : route) {
				System.out.print(customerId + " ");
			}
			System.out.println();
		}
		
	}


}
