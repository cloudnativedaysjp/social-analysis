package jp.cloudnativedays.social.analysis.configuration;

import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import com.slack.api.bolt.context.builtin.EventContext;
import com.slack.api.bolt.handler.BoltEventHandler;
import com.slack.api.bolt.socket_mode.SocketModeApp;
import com.slack.api.methods.request.conversations.ConversationsInfoRequest;
import com.slack.api.methods.request.team.TeamInfoRequest;
import com.slack.api.methods.request.users.UsersInfoRequest;
import com.slack.api.methods.response.conversations.ConversationsInfoResponse;
import com.slack.api.methods.response.team.TeamInfoResponse;
import com.slack.api.methods.response.users.UsersInfoResponse;
import com.slack.api.model.event.MessageEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "slack.enabled", havingValue = "true")
public class SlackInitializer {

	private final String slackAppToken;

	private final App app;

	private SocketModeApp socketModeApp;

	public SlackInitializer(@Value("${SLACK_APP_TOKEN}") String slackAppToken,
			@Value("${SLACK_BOT_TOKEN}") String slackBotToken) {

		this.slackAppToken = slackAppToken;
		AppConfig appConfig = AppConfig.builder().singleTeamBotToken(slackBotToken).build();
		this.app = new App(appConfig);

	}

	public void startSocketModeApp() throws Exception {
		socketModeApp = new SocketModeApp(slackAppToken, app);
		socketModeApp.startAsync();
	}

	public void stopSocketModeApp() throws Exception {
		socketModeApp.stop();
	}

	public void setAppMessageEvent(BoltEventHandler<MessageEvent> handler) {
		app.event(MessageEvent.class, handler);
	}

	public String getTeamNameFromEvent(MessageEvent event, EventContext ctx) {
		TeamInfoResponse teamInfoResponse;
		try {
			teamInfoResponse = ctx.client().teamInfo(TeamInfoRequest.builder().team(event.getTeam()).build());
		}
		catch (Exception e) {
			return "";
		}
		return teamInfoResponse.getTeam().getName();
	}

	public String getChannelNameFromEvent(MessageEvent event, EventContext ctx) {
		ConversationsInfoResponse channelInfoResponse;
		try {
			channelInfoResponse = ctx.client()
					.conversationsInfo(ConversationsInfoRequest.builder().channel(event.getChannel()).build());
		}
		catch (Exception e) {
			return "";
		}
		return channelInfoResponse.getChannel().getName();
	}

	public String getUserNameFromEvent(MessageEvent event, EventContext ctx) {
		UsersInfoResponse usersInfoResponse;
		try {
			usersInfoResponse = ctx.client().usersInfo(UsersInfoRequest.builder().user(event.getUser()).build());
		}
		catch (Exception e) {
			return "";
		}
		return usersInfoResponse.getUser().getName();
	}

}
