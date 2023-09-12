package vrp;

public class Site {
//    private List<Integer> availableDays;
    private int availableDate;
    private boolean visitable;  // 방문할 수 있는지
    private int serviceTime;
    private int days;   // 방문하고 얼마나 지났는지에 대한 필드명 수정해야함

    public Site(int availableDate, int serviceTime){
        this.availableDate = availableDate;
        this.serviceTime = serviceTime;
        this.visitable = true;
    }


    public int getAvailableDate(){
        return this.availableDate;
    }
    public int getServiceTime(){
        return this.serviceTime;
    }
    public void visitSite(){
        this.visitable = false;
    }
}
