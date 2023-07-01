package org.hoongoin.interviewbank.scrap.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UpdateScrapResponse {

	private String title;
	private boolean isPublic;

	public void setIsPublic(boolean isPublic){
		this.isPublic = isPublic;
	}

	public boolean getIsPublic(){
		return this.isPublic;
	}
}
