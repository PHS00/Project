package vrp.test;

import vrp.*;

import java.io.IOException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Test {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		VrpProblem problem = VrpReader.readDataInstance();

		List<Routes> routes = buildRoutes();    // build routes

		VrpSolution sol = new VrpSolution(routes, problem);	// create solution

		Random rand = new Random();
		Lns lns = new Lns(rand);

		System.out.println("변경 전 총 거리 비용 : " + calTotalCost(sol));

		showRoutes(sol);	// 변경 전 경로 출력

		// 1일부터 4일까지 반복
		for (int i = 1; i < 4; i++) {
			lns.remove(sol, 2, i);
			System.out.print("removedId : ");
			for (Integer removedId : sol.getRemovedSites()) {
				System.out.print(removedId + " ");
			}
			System.out.println();

			lns.repair(sol, i);
		}

		System.out.println("복구된 차량 경로");
		showRoutes(sol);

		System.out.println("변경 후 총 거리 비용 : " + calTotalCost(sol));

	}

	public static double calTotalCost(VrpSolution sol) {
		VrpProblem problem = sol.getProblem();
		double cost = 0.;
		for (Routes r : sol.getRoutes()) {
			for (List<Integer> route : r.getRoutes()) {
				for (int i = 0; i < route.size() - 1; i++) {
					int now = route.get(i);
					int next = route.get(i + 1);
					cost += problem.calDis(now, next);
				}
			}
		}
		return cost;
	}

	public static void showRoutes(VrpSolution sol){
		for (Routes r : sol.getRoutes()) {
			System.out.println(r.getDate() + "일의 차량 경로");
			for (List<Integer> route : r.getRoutes()) {
				for (int i = 0; i < route.size(); i++) {
					System.out.print(route.get(i) + " ");
				}
				System.out.println();
			}
		}
	}

	public static List<Routes> buildRoutes(){
		List<Routes> routes = new ArrayList<>();    // solution

		List<List<Integer>> routeDate1 = new ArrayList<>();    // route of date1
		routeDate1.add(new ArrayList<>());
		routeDate1.add(new ArrayList<>());
		// vehicle 0
		routeDate1.get(0).add(0);
		routeDate1.get(0).add(1);
		routeDate1.get(0).add(2);
		routeDate1.get(0).add(3);
		routeDate1.get(0).add(0);
		// vehicle 1
		routeDate1.get(1).add(0);
		routeDate1.get(1).add(4);
		routeDate1.get(1).add(5);
		routeDate1.get(1).add(6);
		routeDate1.get(1).add(0);
		Routes r1 = new Routes(1, routeDate1);
		routes.add(r1);

		List<List<Integer>> routeDate2 = new ArrayList<>();    // route of date1
		routeDate2.add(new ArrayList<>());
		routeDate2.add(new ArrayList<>());
		routeDate2.get(0).add(0);
		routeDate2.get(0).add(7);
		routeDate2.get(0).add(8);
		routeDate2.get(0).add(9);
		routeDate2.get(0).add(0);
		routeDate2.get(1).add(0);
		routeDate2.get(1).add(10);
		routeDate2.get(1).add(11);
		routeDate2.get(1).add(12);
		routeDate2.get(1).add(0);
		Routes r2 = new Routes(2, routeDate2);
		routes.add(r2);

		List<List<Integer>> routeDate3 = new ArrayList<>();    // route of date1
		routeDate3.add(new ArrayList<>());
		routeDate3.add(new ArrayList<>());
		routeDate3.get(0).add(0);
		routeDate3.get(0).add(13);
		routeDate3.get(0).add(14);
		routeDate3.get(0).add(15);
		routeDate3.get(0).add(0);
		routeDate3.get(1).add(0);
		routeDate3.get(1).add(16);
		routeDate3.get(1).add(17);
		routeDate3.get(1).add(18);
		routeDate3.get(1).add(0);
		Routes r3 = new Routes(3, routeDate3);
		routes.add(r3);

		return routes;
	}

}
