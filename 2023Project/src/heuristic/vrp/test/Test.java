package heuristic.vrp.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Test {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		
		TestVrpProblem problem = TestVrpReader.readDataInstance();

		List<List<Integer>> routes = new ArrayList<>();
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
		
		TestVrpSolution sol = new TestVrpSolution(routes, problem);
		Random rand = new Random();
		TestLns lns = new TestLns(rand);
		
		sol = lns.remove(sol, 5);
		sol = lns.repair(sol, sol.getUnrouted());
		
		for(List<Integer> route : sol.getRoutes()) {
			for(Integer customerId : route) {
				System.out.print(customerId + " ");
			}
			System.out.println();
		}
		
	}
	
	public static void start() {
		
	}

}
