package jp.cloudnativedays.social.analysis.client;

import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.channel.GuildChannel;
import discord4j.gateway.intent.Intent;
import discord4j.gateway.intent.IntentSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class DiscordLocalClientImpl implements DiscordLocalClient {

	private final String discordServer;

	private final String discordToken;

	private final GatewayDiscordClient gatewayDiscordClient;

	private final Map<String, String> channelMember;

	public DiscordLocalClientImpl(@Value("${discord.token}") String discordToken,
			@Value("${discord.server}") String discordServer) {
		this.discordToken = discordToken;
		this.discordServer = discordServer;
		this.channelMember = new HashMap<>();
		DiscordClient discordClient = DiscordClient.create(discordToken);
		gatewayDiscordClient = discordClient.gateway().setEnabledIntents(IntentSet.of(Intent.GUILD_MEMBERS)).login().block();

	}

	@Override
	public void getActiveChannelMemberName() {

		List<Member> members = gatewayDiscordClient.getGuildMembers(Snowflake.of(discordServer))
				.filter(member -> !member.isBot()).collectList().block();


		List<GuildChannel> channels = gatewayDiscordClient.getGuildChannels(Snowflake.of(discordServer)).collectList().block();

		for(GuildChannel channel: channels){
			System.out.println(channel.getType().name());
		}

		assert members != null;
		for (Member member : members) {
			VoiceState voiceState = member.getVoiceState().blockOptional().orElse(null);
			if (voiceState != null) {
				System.out.println(member.getDisplayName());
				String channelName = Objects.requireNonNull(voiceState.getChannel().block()).getName();
				String memberName = member.getDisplayName();
				channelMember.put(channelName, memberName);
			}
		}
	}


}
