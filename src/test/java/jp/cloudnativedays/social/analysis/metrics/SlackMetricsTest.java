package jp.cloudnativedays.social.analysis.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import jp.cloudnativedays.social.analysis.model.SlackData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SlackMetricsTest {

	private final MeterRegistry registry = new SimpleMeterRegistry();

	private final SlackMetrics slackMetrics = new SlackMetrics(registry);

	@BeforeEach
	void setUp() {
		SlackData slackData1 = new SlackData("team", "channel", "user");
		SlackData slackData2 = new SlackData("team", "channel", "user");
		SlackData slackData3 = new SlackData("team", "channel", "tanaka");
		SlackData slackData4 = new SlackData("team", "dev", "user");

		slackMetrics.setMessageMetrics(slackData1);
		slackMetrics.setMessageMetrics(slackData2);
		slackMetrics.setMessageMetrics(slackData3);
		slackMetrics.setMessageMetrics(slackData4);

	}

	@Test
	void setMessageMetrics() {
		String metricsName = "social.slack.messages";

		Tags tags1 = Tags.of(Tag.of("workspaceName", "team"), Tag.of("channelName", "channel"),
				Tag.of("screenName", "user"));

		Tags tags3 = Tags.of(Tag.of("workspaceName", "team"), Tag.of("channelName", "channel"),
				Tag.of("screenName", "tanaka"));

		Tags tags4 = Tags.of(Tag.of("workspaceName", "team"), Tag.of("channelName", "dev"),
				Tag.of("screenName", "user"));

		assertEquals(1, registry.get(metricsName).tags(tags1).meters().size());
		assertEquals(1, registry.get(metricsName).tags(tags3).meters().size());
		assertEquals(1, registry.get(metricsName).tags(tags4).meters().size());
		assertEquals(2, registry.get(metricsName).tags(tags1).counter().count());
		assertEquals(1, registry.get(metricsName).tags(tags3).counter().count());
		assertEquals(1, registry.get(metricsName).tags(tags4).counter().count());

	}

}