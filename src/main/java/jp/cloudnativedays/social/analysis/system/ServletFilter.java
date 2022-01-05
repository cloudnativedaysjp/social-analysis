package jp.cloudnativedays.social.analysis.system;

import jp.cloudnativedays.social.analysis.service.TwitterService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class ServletFilter {

    private TwitterService twitterService;

    public ServletFilter(TwitterService twitterService) {
        this.twitterService = twitterService;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new MetricsFilter(twitterService));
        registrationBean.setUrlPatterns(Arrays.asList("/actuator/prometheus"));

        return registrationBean;
    }
}
