package Report_Management;

import Driver.DateTime;

public abstract class Report {
    private String title;
    private DateTime reportDate;
    private String purpose;
    private String conclusion;

    // Constructor
    public Report() {
    }

    public Report(String title, DateTime reportDate, String purpose, String conclusion) {
        this.title = title;
        this.reportDate = reportDate;
        this.purpose = purpose;
        this.conclusion = conclusion;
    }

    //public abstract double calculate();

    public String toString() {
        return String.format("Title: %s\t\t\t\tReport Date: %s\n\nPurpose: \n%s\nConclusion: \n%s", title, reportDate.getDate(), purpose, conclusion);
    }

    public boolean equals(Object obj){
        if(obj instanceof Report) {
            Report anotherObj = (Report) obj;
            return title.equals(anotherObj.title);
        }
        else {
            return false;
        }
    }

    // Setter
    public void setTitle(String title) {
        this.title = title;
    }

    public void setReportDate(DateTime reportDate) {
        this.reportDate = reportDate;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    // Getter
    public String getTitle() {
        return title;
    }

    public DateTime getReportDate() {
        return reportDate;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getConclusion() {
        return conclusion;
    }
}
