package ui.main;

public class Column {
    private String status;
    private String title;

    public Column(String status, String title) {
        this.status = status;
        this.title = title;
    }
    public String getStatus() { return status; }
    public String getTitle() { return title; }
}