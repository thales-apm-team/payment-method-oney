package com.payline.payment.oney.common.bean;

import com.payline.payment.oney.bean.common.NavigationData;
import com.payline.payment.oney.exception.InvalidRequestException;
import com.payline.pmapi.bean.payment.Environment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NavigationDataTest {

    private NavigationData navigationData;

    @Test
    public void buildNavigationData() {
        navigationData = NavigationData.Builder.aNavigationDataBuilder()
                .withFailUrl("fail/url/")
                .withSuccesUrl("success/url/")
                .withNotificationUrl("notification/url/")
                .withPendingUrl("pending/url/")
                .build();

        Assertions.assertEquals("fail/url/", navigationData.getFailUrl());
        Assertions.assertEquals("success/url/", navigationData.getSuccessUrl());
        Assertions.assertEquals("notification/url/", navigationData.getNotificationUrl());
        Assertions.assertEquals("pending/url/", navigationData.getPendingUrl());

    }

    @Test
    public void buildNavigationDataFail() {

        Throwable exception = Assertions.assertThrows(IllegalStateException.class, () -> {
            navigationData = NavigationData.Builder.aNavigationDataBuilder()
                    .withFailUrl("fail/url/")
                    .withNotificationUrl("notification/url/")
                    .withPendingUrl("pending/url/")
                    .build();
        });
        Assertions.assertEquals("NavigationData must have a successUrl when built", exception.getMessage());


    }

    @Test
    public void buildNavigationDataFromPayline() throws InvalidRequestException {

        Environment environment = new Environment("notif", "successOrPending", "fail", true);
        navigationData = NavigationData.Builder.aNavigationDataBuilder()
                .fromEnvironment(environment)
                .build();

        Assertions.assertEquals("notif", navigationData.getNotificationUrl());
        Assertions.assertEquals("successOrPending", navigationData.getPendingUrl());
        Assertions.assertEquals("successOrPending", navigationData.getSuccessUrl());
        Assertions.assertEquals("fail", navigationData.getFailUrl());
    }


    @Test
    public void toStringTest() throws InvalidRequestException {
        Environment environment = new Environment("notif", "successOrPending", "fail", true);
        navigationData = NavigationData.Builder.aNavigationDataBuilder()
                .fromEnvironment(environment)
                .build();

        Assertions.assertTrue(navigationData.toString().contains("success_url"));
        Assertions.assertTrue(navigationData.toString().contains("fail_url"));
        Assertions.assertTrue(navigationData.toString().contains("server_response_url"));
        Assertions.assertTrue(navigationData.toString().contains("alternative_return_url"));

    }
}
