package jp.cloudnativedays.social.analysis.client;

import org.springframework.cloud.sleuth.annotation.NewSpan;

public interface DiscordLocalClient {

    @NewSpan
	void getActiveChannelMemberName();

}
