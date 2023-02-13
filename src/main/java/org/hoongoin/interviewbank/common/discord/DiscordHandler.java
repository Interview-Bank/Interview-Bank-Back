package org.hoongoin.interviewbank.common.discord;

import javax.servlet.http.HttpServletRequest;

import org.hoongoin.interviewbank.utils.MessageFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import discord4j.common.util.Snowflake;
import lombok.extern.slf4j.Slf4j;

@Component
@Profile("local")
@Slf4j
public class DiscordHandler {

	@Value("${discord.channelId}")
	private Long channelId;

	private final DiscordCreator discordCreator;

	public DiscordHandler(DiscordCreator discordCreator) {
		this.discordCreator = discordCreator;
	}

	public void send(Exception exception, HttpServletRequest request) {
		try {
			String prefixMessage = MessageFactory.createHttpMessage(request);
			String bodyMessage = MessageFactory.createStackTraceMessage(exception);
			this.sendMessageToDiscord(prefixMessage + "\n" + bodyMessage);
		} catch (Exception messageException) {
			log.error(messageException.getMessage(), messageException);
		}
	}

	private void sendMessageToDiscord(String message) {
		discordCreator.getDiscordClient().getChannelById(Snowflake.of(channelId))
			.createMessage(message)
			.block();
	}
}
