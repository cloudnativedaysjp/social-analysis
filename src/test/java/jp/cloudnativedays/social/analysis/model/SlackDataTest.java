package jp.cloudnativedays.social.analysis.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SlackDataTest {

	private SlackData slackData;

	@BeforeEach
	void setUp() {
		slackData = new SlackData("team", "channel", "user");
	}

	@Test
	void getTeam() {
		String ans = "team";
		assertEquals(ans, slackData.getTeam());
	}

	@Test
	void getChannel() {
		String ans = "channel";
		assertEquals(ans, slackData.getChannel());
	}

	@Test
	void getUser() {
		String ans = "user";
		assertEquals(ans, slackData.getUser());
	}

}