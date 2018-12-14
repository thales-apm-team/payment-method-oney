package com.payline.payment.oney.common.bean;

import com.payline.payment.oney.bean.common.NavigationData;
import com.payline.payment.oney.exception.InvalidRequestException;
import com.payline.pmapi.bean.payment.Environment;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class NavigationDataTest {

    private NavigationData navigationData;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();


    @Test
    public void buildNavigationData() {
        navigationData = NavigationData.Builder.aNavigationDataBuilder()
                .withFailUrl("fail/url/")
                .withSuccesUrl("success/url/")
                .withNotificationUrl("notification/url/")
                .withPendingUrl("pending/url/")
                .build();

        Assert.assertEquals("fail/url/", navigationData.getFailUrl());
        Assert.assertEquals("success/url/", navigationData.getSuccessUrl());
        Assert.assertEquals("notification/url/", navigationData.getNotificationUrl());
        Assert.assertEquals("pending/url/", navigationData.getPendingUrl());

    }

    @Test
    public void buildNavigationDataFail() {
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("NavigationData must have a successUrl when built");

        navigationData = NavigationData.Builder.aNavigationDataBuilder()
                .withFailUrl("fail/url/")
                .withNotificationUrl("notification/url/")
                .withPendingUrl("pending/url/")
                .build();


    }

    @Test
    public void buildNavigationDataFromPayline() throws InvalidRequestException {

        Environment environment = new Environment("notif", "successOrPending", "fail", true);
        navigationData = NavigationData.Builder.aNavigationDataBuilder()
                .fromEnvironment(environment)
                .build();

        Assert.assertEquals("notif", navigationData.getNotificationUrl());
        Assert.assertEquals("successOrPending", navigationData.getPendingUrl());
        Assert.assertEquals("successOrPending", navigationData.getSuccessUrl());
        Assert.assertEquals("fail", navigationData.getFailUrl());
    }


    @Test
    public void toStringTest() throws InvalidRequestException {
        Environment environment = new Environment("notif", "successOrPending", "fail", true);
        navigationData = NavigationData.Builder.aNavigationDataBuilder()
                .fromEnvironment(environment)
                .build();

        Assert.assertTrue(navigationData.toString().contains("success_url"));
        Assert.assertTrue(navigationData.toString().contains("fail_url"));
        Assert.assertTrue(navigationData.toString().contains("server_response_url"));
        Assert.assertTrue(navigationData.toString().contains("alternative_return_url"));

    }
}
