package org.hoongoin.interviewbank.common.discord;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

import org.hoongoin.interviewbank.inquiry.controller.request.InquiryRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.MessageCreateSpec;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class DiscordInquiryHandler {

	@Value("${discord.channelId}")
	private Long channelId;

	private final DiscordCreator discordCreator;

	public DiscordInquiryHandler(DiscordCreator discordCreator) {
		this.discordCreator = discordCreator;
	}

	public void send(InquiryRequest inquiryRequest, Optional<MultipartFile> file) throws IOException {
		MessageCreateSpec messageCreateSpec;

		if (file.isPresent()) {
			Path tempFile = Files.createTempFile("discord", null);
			file.get().transferTo(tempFile.toFile());
			messageCreateSpec = MessageCreateSpec.builder()
				.content(inquiryRequest.getTitle() + inquiryRequest.getContent() + inquiryRequest.getEmail())
				.addFile(Objects.requireNonNull(file.get().getOriginalFilename()), Files.newInputStream(tempFile))
				.build();
			Files.delete(tempFile);
		} else {
			messageCreateSpec = MessageCreateSpec.builder()
				.content(inquiryRequest.getTitle() + inquiryRequest.getContent() + inquiryRequest.getEmail())
				.build();
		}

		MessageChannel channel = (MessageChannel)discordCreator.getDiscordClient()
			.getChannelById(Snowflake.of(channelId));

		Mono<Message> sendMessage = channel.createMessage(messageCreateSpec);

		sendMessage.block();
	}
}
