package jp.cloudnativedays.social.analysis.system;

import brave.handler.MutableSpan;
import com.slack.api.app_backend.events.payload.Authorization;
import com.slack.api.app_backend.events.payload.EventsApiPayload;
import com.slack.api.bolt.context.builtin.EventContext;
import com.slack.api.model.event.MessageEvent;
import jp.cloudnativedays.social.analysis.service.SlackService;
import jp.cloudnativedays.social.analysis.service.TwitterService;
import jp.cloudnativedays.social.analysis.system.utils.TestSpanHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import twitter4j.TwitterException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = { "twitter.enabled=false", "slack.app.token=dummy", "slack.bot.token=dummy",
		"sentiment.file=data/test-data.trim", "slack.enabled=true" })
class WfSpanCustomizerTest {

	@Autowired
	private TwitterService twitterService;

	@Autowired
	private SlackService slackService;

	@Autowired
	private TestSpanHolder testSpanHolder;

	List<MutableSpan> spans;

	// https://gitter.im/spring-cloud/spring-cloud-sleuth?at=5e9584d8e24b4d6c440898ef
	@BeforeEach
	void setUp() {
		spans = testSpanHolder.getSpans();
	}

	@Test
	void addTwitterSpanTags() throws TwitterException {

		twitterService.searchTwitterAndSetMetrics();

		assertTrue(spans.size() > 0);

		boolean spanFound = false;

		for (MutableSpan span : spans) {
			try {
				if (span.tag("class").startsWith("TestTwitterClient") && span.tag("method").equals("search")) {
					spanFound = true;
					assertEquals("Twitter API", span.tag("_outboundExternalService"));
					assertEquals("twitter-api", span.tag("_externalComponent"));
					assertNotNull(span.tag("_externalApplication"));
				}
			}
			catch (Exception ignored) {
			}
		}
		assertTrue(spanFound);
	}

	@Test
	void addSlackSpanTags() {

		EventsApiPayload<MessageEvent> payload = new EventsApiPayload<>() {
			@Override
			public String getToken() {
				return null;
			}

			@Override
			public void setToken(String s) {

			}

			@Override
			public String getTeamId() {
				return null;
			}

			@Override
			public void setTeamId(String s) {

			}

			@Override
			public String getApiAppId() {
				return null;
			}

			@Override
			public void setApiAppId(String s) {

			}

			@Override
			public MessageEvent getEvent() {
				return null;
			}

			@Override
			public void setEvent(MessageEvent event) {

			}

			@Override
			public String getType() {
				return null;
			}

			@Override
			public void setType(String s) {

			}

			@Override
			public String getEventId() {
				return null;
			}

			@Override
			public void setEventId(String s) {

			}

			@Override
			public Integer getEventTime() {
				return null;
			}

			@Override
			public void setEventTime(Integer integer) {

			}

			@Override
			public String getEventContext() {
				return null;
			}

			@Override
			public void setEventContext(String s) {

			}

			@Override
			public boolean isExtSharedChannel() {
				return false;
			}

			@Override
			public void setExtSharedChannel(boolean b) {

			}

			@Override
			public List<String> getAuthedUsers() {
				return null;
			}

			@Override
			public void setAuthedUsers(List<String> list) {

			}

			@Override
			public List<String> getAuthedTeams() {
				return null;
			}

			@Override
			public void setAuthedTeams(List<String> list) {

			}

			@Override
			public List<Authorization> getAuthorizations() {
				return null;
			}

			@Override
			public void setAuthorizations(List<Authorization> list) {

			}

			@Override
			public String getEnterpriseId() {
				return null;
			}

			@Override
			public void setEnterpriseId(String s) {

			}
		};
		EventContext context = new EventContext();

		slackService.eventHandler(payload, context);

		assertTrue(spans.size() > 0);

		boolean spanFound = false;

		for (MutableSpan span : spans) {
			try {
				if (span.tag("class").startsWith("TestSlackClient") && span.tag("method").startsWith("get")) {
					spanFound = true;
					assertEquals("Slack API", span.tag("_outboundExternalService"));
					assertEquals("slack-api", span.tag("_externalComponent"));
					assertNotNull(span.tag("_externalApplication"));
				}
			}
			catch (Exception ignored) {
			}
		}
		assertTrue(spanFound);
	}

}
