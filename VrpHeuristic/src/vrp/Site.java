package vrp;

public class Site {
//    private List<Integer> availableDays;
    private int availableDate;
    private boolean visitable;  // 방문할 수 있는지
    private int days;   // 방문하고 얼마나 지났는지에 대한 필드명 수정해야함

    public Site(int availableDate){
        this.availableDate = availableDate;
        this.visitable = true;
    }


    public void setAvailableDate(int date){
        this.availableDate = date;
    }
    public int getAvailableDate(){
        return this.availableDate;
    }
    public void visitSite(){
        this.visitable = false;
    }
}
