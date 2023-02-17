package org.hoongoin.interviewbank.common.discord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import discord4j.core.DiscordClient;

@Component
public class DiscordCreator {

	private final DiscordClient discordClient;

	@Autowired
	public DiscordCreator(@Value("${discord.token}") String discordToken) {
		this.discordClient = DiscordClient.create(discordToken);
		discordClient.login().block();
	}

	public DiscordClient getDiscordClient() {
		return discordClient;
	}
}
