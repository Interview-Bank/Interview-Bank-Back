package org.hoongoin.interviewbank.scrap.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;

import org.hoongoin.interviewbank.scrap.ScrapMapper;
import org.hoongoin.interviewbank.scrap.application.entity.Scrap;
import org.hoongoin.interviewbank.scrap.controller.request.UpdateScrapRequest;
import org.hoongoin.interviewbank.scrap.controller.response.UpdateScrapResponse;
import org.hoongoin.interviewbank.scrap.domain.ScrapCommandService;
import org.hoongoin.interviewbank.scrap.domain.ScrapQueryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ScrapServiceTest {

	@InjectMocks
	private ScrapService scrapService;
	@Mock
	private ScrapQueryService scrapQueryService;
	@Mock
	private ScrapCommandService scrapCommandService;
	@Mock
	private ScrapMapper scrapMapper;

	@Test
	void updateScrapByRequestAndScrapId_Success() {
		//given
		long scrapId = 1;
		long requestingAccountId = 1;
		Scrap scrap = Scrap.builder()
			.scrapId(1)
			.accountId(1)
			.interviewId(1)
			.title("title")
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();
		UpdateScrapRequest updateScrapRequest = new UpdateScrapRequest("New Title");

		given(scrapQueryService.findScrapByScrapId(1)).willReturn(scrap);

		Scrap scrapToUpdate = Scrap.builder().title(updateScrapRequest.getTitle()).build();
		given(scrapMapper.updateScrapRequestToScrap(updateScrapRequest)).willReturn(scrapToUpdate);

		scrap.setTitle(updateScrapRequest.getTitle());
		given(scrapCommandService.updateScrap(scrapId, scrapToUpdate)).willReturn(scrap);

		given(scrapMapper.scrapToUpdateScrapResponse(scrap)).willReturn(new UpdateScrapResponse(scrap.getTitle()));

		//when
		UpdateScrapResponse updateScrapResponse = scrapService.updateScrapByRequestAndScrapId(updateScrapRequest,
			scrapId, requestingAccountId);

		//then
		assertThat(updateScrapResponse.getTitle()).isEqualTo(scrap.getTitle());
	}
}
