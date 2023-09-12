package vrp;

import java.util.List;

public class Routes {
    private int date;   // id == index
    private List<List<Integer>> routes;
    private double totalCost;
    private double travelTime;

    public Routes(int date, List<List<Integer>> routes){
        this.date = date;
        this.routes = routes;
    }

    public void setTotalCost(double totalCost){
        this.totalCost = totalCost;
    }
    public double getCost(){
        return this.totalCost;
    }
    public void setTravelTime(double travelTime){
        this.travelTime = travelTime;
    }
    public double getTravelTime(){
        return this.travelTime;
    }

    public List<List<Integer>> getRoutes (){
        return this.routes;
    }
    public void updateRoutes(List<List<Integer>> routes){
        this.routes = routes;
    }
    public void setDate(int date){
        this.date = date;
    }
    public int getDate(){
        return this.date;
    }



}
