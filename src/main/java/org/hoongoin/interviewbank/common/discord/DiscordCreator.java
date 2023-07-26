package org.hoongoin.interviewbank.common.discord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;

@Component
public class DiscordCreator {

	private final DiscordClient discordClient;
	private final GatewayDiscordClient gatewayDiscordClient;

	@Autowired
	public DiscordCreator(@Value("${discord.token}") String discordToken) {
		this.discordClient = DiscordClient.create(discordToken);
		this.gatewayDiscordClient = discordClient.login().block();
	}

	public DiscordClient getDiscordClient() {
		return discordClient;
	}

	public GatewayDiscordClient getGatewayDiscordClient() {
		return gatewayDiscordClient;
	}
}
