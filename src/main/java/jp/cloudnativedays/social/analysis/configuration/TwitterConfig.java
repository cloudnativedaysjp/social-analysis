package jp.cloudnativedays.social.analysis.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

@Configuration
public class TwitterConfig {

    @Value("${twitter.consumer.key}")
    String consumerKey;

    @Value("${twitter.consumer.secret}")
    String consumerSecret;

    @Value("${twitter.access.token}")
    String accessToken;

    @Value("${twitter.access.tokenSecret}")
    String accessTokenSecret;

    @Bean
    public TwitterTemplate twitter() {
        try {
            return new TwitterTemplate(
                    consumerKey,
                    consumerSecret,
                    accessToken,
                    accessTokenSecret);
        } catch (IllegalStateException ex) {
            throw new IllegalStateException("Unable to configure twitter. "
                    + "Check that your application-secrets.properties "
                    + "file is configured", ex);
        }
    }
}
