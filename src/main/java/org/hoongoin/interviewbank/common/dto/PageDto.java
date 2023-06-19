package org.hoongoin.interviewbank.common.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageDto<T> {

	private int totalPages;
	private long totalElements;
	private int currentPage;
	private int currentElements;
	private List<T> content;
}
