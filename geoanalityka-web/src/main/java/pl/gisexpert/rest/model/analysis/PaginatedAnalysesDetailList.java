package pl.gisexpert.rest.model.analysis;

import java.util.List;

/**
 * Created by pkociuba on 2016-09-12.
 */
public class PaginatedAnalysesDetailList {

    private List<DemographicAnalysisDetails> analysesDetailsList;

    private int numberOfPages;

    private int pageSize;

    public int getNumberOfAnalyses() {
        return numberOfAnalyses;
    }

    public void setNumberOfAnalyses(int numberOfAnalyses) {
        this.numberOfAnalyses = numberOfAnalyses;
    }

    private int numberOfAnalyses;

    public PaginatedAnalysesDetailList(List<DemographicAnalysisDetails> analysesDetailsList, int numberOfPages, int pageSize, int numberOfAnalyses) {
        this.analysesDetailsList = analysesDetailsList;
        this.numberOfPages = numberOfPages;
        this.pageSize = pageSize;
        this.numberOfAnalyses = numberOfAnalyses;

    }

    public PaginatedAnalysesDetailList() {
    }


    public List<DemographicAnalysisDetails> getAnalysesDetailsList() {
        return analysesDetailsList;
    }

    public void setAnalysesDetailsList(List<DemographicAnalysisDetails> analysesDetailsList) {
        this.analysesDetailsList = analysesDetailsList;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
