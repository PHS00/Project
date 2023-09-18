package vrp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


public class InitialSolutionReader {
    private static String str;
    private static int n = 0;
    private static List<List<Integer>> routeDate;
    private static List<Date> routes = new ArrayList<>();
    private static int cnt = 0;
    public static List<Date> readInitialSolution() throws IOException {
        BufferedReader reader = new BufferedReader(
                new FileReader("/Users/hijieung/Desktop/initialtest.txt")
        );

        while ((str = reader.readLine()) != null) {
            if (str.length() == 1) {
                n = Integer.parseInt(str);
                routeDate = new ArrayList<>();
                cnt = 0;
            } else {
                routeDate.add(new ArrayList<>());
                StringTokenizer st = new StringTokenizer(str);
                while (st.hasMoreTokens()) {
                    routeDate.get(cnt).add(Integer.parseInt(st.nextToken()));
                }
                Date date = new Date(n, routeDate);
                routes.add(date);
            }
        }
        return routes;
    }
}
