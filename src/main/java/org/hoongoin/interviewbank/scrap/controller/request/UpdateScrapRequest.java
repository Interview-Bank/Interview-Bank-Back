package org.hoongoin.interviewbank.scrap.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateScrapRequest {

	private String title;
	private boolean isPublic;

	public boolean getIsPublic() {
		return this.isPublic;
	}
}
