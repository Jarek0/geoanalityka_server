package pl.gisexpert.rest.model.analysis.demographic;

import pl.gisexpert.rest.model.BaseResponse;

public class SumAllInRadiusResponse extends BaseResponse {
	private Integer sum;

	public Integer getSum() {
		return sum;
	}

	public void setSum(Integer sum) {
		this.sum = sum;
	}
}
