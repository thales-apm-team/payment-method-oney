package com.payline.payment.oney.bean.common;

import com.google.gson.annotations.SerializedName;
import com.payline.payment.oney.utils.Required;

public class NavigationData extends OneyBean {

    @Required
    @SerializedName("server_response_url")
    private String notificationUrl;

    @Required
    @SerializedName("success_url")
    private String successUrl;

    @Required
    @SerializedName("fail_url")
    private String failUrl;

    //alternative return url
    @SerializedName("alternative_return_url")
    private String pendingUrl;

    //Getter
    public String getNotificationUrl() {
        return notificationUrl;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public String getFailUrl() {
        return failUrl;
    }

    public String getPendingUrl() {
        return pendingUrl;
    }

    private NavigationData(NavigationData.Builder builder) {
        this.notificationUrl = builder.notificationUrl;
        this.successUrl = builder.successUrl;
        this.failUrl = builder.failUrl;
        this.pendingUrl = builder.pendingUrl;
    }

    private NavigationData() {
    }

    public static class Builder {
        private String notificationUrl;
        private String successUrl;
        private String failUrl;
        //alternative return url
        private String pendingUrl;

        public static NavigationData.Builder aNavigationDataBuilder() {
            return new NavigationData.Builder();
        }

        public NavigationData.Builder withSuccesUrl(String succesUrl) {
            this.successUrl = succesUrl;
            return this;
        }

        public NavigationData.Builder withFailUrl(String failUrl) {
            this.failUrl = failUrl;
            return this;
        }

        public NavigationData.Builder withNotificationUrl(String notificationUrl) {
            this.notificationUrl = notificationUrl;
            return this;
        }

        public NavigationData.Builder withPendingUrl(String pendingUrl) {
            this.pendingUrl = pendingUrl;
            return this;
        }

        public NavigationData build() {
            return new NavigationData(this);
        }

    }


}
