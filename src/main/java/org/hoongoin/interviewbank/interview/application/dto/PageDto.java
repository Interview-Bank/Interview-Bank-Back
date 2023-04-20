package org.hoongoin.interviewbank.interview.application.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageDto<T>{

	private int totalPages;
	private long totalElements;
	private List<T> content;
}
