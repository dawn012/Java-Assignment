package Report_Management;

import Driver.DateTime;

import java.time.LocalDate;

public abstract class Report {
    private String title;
    private LocalDate reportDate;
    private String purpose;
    private String conclusion;

    // Constructor
    public Report() {
    }

    public Report(String title, LocalDate reportDate, String purpose, String conclusion) {
        this.title = title;
        this.reportDate = reportDate;
        this.purpose = purpose;
        this.conclusion = conclusion;
    }

    public String toString() {
        return String.format("\nTitle: %s\t\t\t\tReport Date: %s\n\nPurpose: \n%s\n\nConclusion: \n%s\n\n", title, reportDate, purpose, conclusion);
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

    public void setReportDate(LocalDate reportDate) {
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

    public LocalDate getReportDate() {
        return reportDate;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getConclusion() {
        return conclusion;
    }
}
