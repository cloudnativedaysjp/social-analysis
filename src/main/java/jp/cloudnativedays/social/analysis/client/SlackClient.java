package jp.cloudnativedays.social.analysis.client;

import com.slack.api.bolt.context.builtin.EventContext;
import com.slack.api.bolt.handler.BoltEventHandler;
import com.slack.api.model.event.MessageEvent;
import org.springframework.cloud.sleuth.annotation.NewSpan;

public interface SlackClient {

	@NewSpan
	String getTeamNameFromEvent(MessageEvent event, EventContext ctx);

	@NewSpan
	String getChannelNameFromEvent(MessageEvent event, EventContext ctx);

	@NewSpan
	String getUserNameFromEvent(MessageEvent event, EventContext ctx);

	void startSocketModeApp() throws Exception;

	void setAppMessageEvent(BoltEventHandler<MessageEvent> handler);

}
