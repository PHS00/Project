package vrp;

import java.util.List;

public class Routes {
    private int date;   // id == index
    private List<List<Integer>> routes;

    public Routes(int date, List<List<Integer>> routes){
        this.date = date;
        this.routes = routes;
    }

    public List<List<Integer>> getRoutes (){
        return this.routes;
    }
    public void setRoutes(List<List<Integer>> routes){
        this.routes = routes;
    }
    public void setDate(int date){
        this.date = date;
    }
    public int getDate(){
        return this.date;
    }

}
