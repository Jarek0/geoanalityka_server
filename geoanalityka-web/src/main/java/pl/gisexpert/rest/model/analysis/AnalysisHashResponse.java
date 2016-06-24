package pl.gisexpert.rest.model.analysis;

import pl.gisexpert.rest.model.BaseResponse;

public class AnalysisHashResponse extends BaseResponse {
	
	private String hash;

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}
}
