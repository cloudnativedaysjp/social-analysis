package jp.cloudnativedays.social.analysis.system;

import jp.cloudnativedays.social.analysis.service.TwitterService;

import org.springframework.web.filter.OncePerRequestFilter;
import twitter4j.TwitterException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MetricsFilter extends OncePerRequestFilter {

	private final TwitterService twitterService;

	public MetricsFilter(TwitterService twitterService) {
		this.twitterService = twitterService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		try {
			twitterService.searchTwitterAndSetMetrics();
		}
		catch (TwitterException e) {
			e.printStackTrace();
		}
		filterChain.doFilter(request, response);
	}

}
