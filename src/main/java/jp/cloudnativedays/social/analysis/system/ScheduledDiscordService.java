package jp.cloudnativedays.social.analysis.system;

import jp.cloudnativedays.social.analysis.service.DiscordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "discord.enabled", havingValue = "true")
public class ScheduledDiscordService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledDiscordService.class);

    private final DiscordService discordService;

    public ScheduledDiscordService(DiscordService discordService) {
        this.discordService = discordService;
    }

    @Scheduled(fixedDelayString = "${discord.interval}")
    public void performDiscordSearch() {
        logger.info("Performing scheduled base search for discord");
        discordService.getVoiceUserAndSetMetrics();
    }
}
