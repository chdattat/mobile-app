package com.appsdeveloperblog.app.ws.ui.model.response;

import java.util.List;

public class UserEto {

	private long totalElements;
	private List<UserRest> userRests;

	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public List<UserRest> getUserRests() {
		return userRests;
	}

	public void setUserRests(List<UserRest> userRests) {
		this.userRests = userRests;
	}

}
