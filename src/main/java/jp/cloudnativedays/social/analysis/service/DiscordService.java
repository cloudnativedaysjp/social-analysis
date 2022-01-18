package jp.cloudnativedays.social.analysis.service;

import jp.cloudnativedays.social.analysis.client.DiscordLocalClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DiscordService {

    private static final Logger logger = LoggerFactory.getLogger(DiscordService.class);

    private DiscordLocalClient discordLocalClient;

    public DiscordService(DiscordLocalClient discordLocalClient) {
        this.discordLocalClient = discordLocalClient;
    }

    public void getVoiceUserAndSetMetrics(){
        discordLocalClient.getActiveChannelMemberName();
    }
}
