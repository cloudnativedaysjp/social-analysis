package jp.cloudnativedays.social.analysis.system;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class WfSpanCustomizer {

	private final Tracer tracer;

	private final String appName;

	public WfSpanCustomizer(Tracer tracer, @Value("${wavefront.application.name}") String appName) {
		this.tracer = tracer;
		this.appName = appName;
	}

	@After("execution(* jp.cloudnativedays.social.analysis.client.TwitterClient.search(..))")
	public void addTwitterSpanTags() {
		Span span = tracer.currentSpan();
		if (span != null) {
			span.tag("_outboundExternalService", "Twitter API");
			span.tag("_externalApplication", appName);
			span.tag("_externalComponent", "twitter-api");
		}
	}

	@After("execution(* jp.cloudnativedays.social.analysis.client.SlackClient.get*(..))")
	public void addSlackSpanTags() {
		Span span = tracer.currentSpan();
		if (span != null) {
			span.tag("_outboundExternalService", "Slack API");
			span.tag("_externalApplication", appName);
			span.tag("_externalComponent", "slack-api");
		}
	}

}
