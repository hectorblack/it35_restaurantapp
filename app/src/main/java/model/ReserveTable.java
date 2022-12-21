package model;

public class ReserveTable {
    private String date;
    private String pax;
    private String phonenumber;
    private String Time;

    public ReserveTable() {
    }

    public ReserveTable(String date, String pax, String phonenumber, String time) {
        this.date = date;
        this.pax = pax;
        this.phonenumber = phonenumber;
        Time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPax() {
        return pax;
    }

    public void setPax(String pax) {
        this.pax = pax;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

}
