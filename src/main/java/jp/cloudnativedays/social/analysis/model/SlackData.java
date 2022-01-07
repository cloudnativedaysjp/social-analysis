package jp.cloudnativedays.social.analysis.model;

public class SlackData {

	public String team;

	public String channel;

	public String user;

	public SlackData(String team, String channel, String user) {
		this.team = team;
		this.channel = channel;
		this.user = user;
	}

	public String getTeam() {
		return team;
	}

	public String getChannel() {
		return channel;
	}

	public String getUser() {
		return user;
	}

}
