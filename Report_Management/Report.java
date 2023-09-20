package Report_Management;

import Driver.DateTime;

import java.time.LocalDate;

public class Report {
    private LocalDate dateGenerated;
    private String title;
    private DateTime reportDate;
    private String conclusion;

    // Constructor
    public Report() {
        dateGenerated = LocalDate.now();
    }

    public Report(String title, DateTime reportDate, String conclusion) {
        this();
        this.title = title;
        this.reportDate = reportDate;
        this.conclusion = conclusion;
    }

    //public abstract double calculate();
    @Override
    public String toString() {
        return String.format("\n\nReport Generated Date: %s\n\n", dateGenerated);
    }


    // Setter
    public void setTitle(String title) {
        this.title = title;
    }

    public void setReportDate(DateTime reportDate) {
        this.reportDate = reportDate;
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

    public String getConclusion() {
        return conclusion;
    }
}
