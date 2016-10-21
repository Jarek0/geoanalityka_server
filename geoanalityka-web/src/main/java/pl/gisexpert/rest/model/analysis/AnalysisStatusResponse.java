package pl.gisexpert.rest.model.analysis;

import pl.gisexpert.cms.model.analysis.AnalysisStatus;
import pl.gisexpert.cms.model.analysis.AnalysisStatusCode;
import pl.gisexpert.rest.model.BaseResponse;

public class AnalysisStatusResponse extends BaseResponse {
	
	private AnalysisStatus status;
	private AnalysisStatusCode statusCode;
	
	public AnalysisStatus getStatus() {
		return status;
	}
	public void setStatus(AnalysisStatus status) {
		this.status = status;
	}
	public AnalysisStatusCode getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(AnalysisStatusCode statusCode) {
		this.statusCode = statusCode;
	}
}
