package com.payline.payment.oney.utils;

public class OneyConstants {

    private OneyConstants(){}

    //Message common with all payment method
    public static final String PAYMENT_METHOD_NAME = "paymentMethod.name";

    public static final String RELEASE_DATE_FORMAT = "dd/MM/yyyy";
    public static final String RELEASE_DATE = "release.date";
    public static final String RELEASE_VERSION = "release.version";
    public static final String RELEASE_PROPERTIES = "release.properties";

    public static final String I18N_SERVICE_DEFAULT_LOCALE ="en";
    public static final String RESOURCE_BUNDLE_BASE_NAME = "messages";

    //Headers
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_TYPE_VALUE = "application/json";
    public static final String AUTHORIZATION = "X-Oney-Authorization";
    public static final String SECRET_KEY = "X-Oney-Secret";
    public static final String SECRET_VALUE = "Method-body";
    public static final String COUNTRY_CODE_KEY = "X-Oney-Partner-Country-Code";
    public static final String COUNTRY_CODE_VALUE = "country.code.value";

    public static final String CONFIG_HTTP_CONNECT_TIMEOUT = "http.connectTimeout";
    public static final String CONFIG_HTTP_WRITE_TIMEOUT = "http.writeTimeout";
    public static final String CONFIG_HTTP_READ_TIMEOUT = "http.readTimeout";


    //Constant specific of this payment method
    public static final String AUTHORIZATION_VALUE = "oney.authorization";
    public static final String MERCHANT_GUID = "9813e3ff-c365-43f2-8dca-94b850befbf9";
    public static final String PSP_ID = "6ba2a5e2-df17-4ad7-8406-6a9fc488a60a";
    public static final String CHIFFREMENT_KEY = "66s581CG5W+RLEqZHAGQx+vskjy660Kt8x8rhtRpXtY=";

    public static final String X_ONEY_AUTHORIZATION_LABEL = "api.key.label";
    public static final String X_ONEY_AUTHORIZATION_KEY = "apiKey";
    public static final String X_ONEY_AUTHORIZATION_DESCRIPTION = "api.key.description";
    public static final String X_ONEY_AUTHORIZATION_MESSAGE_ERROR = "api.key.error.message";

    public static final String PSP_ID_LABEL = "psp.id.label";
    public static final String PSP_ID_KEY = "pspId";
    public static final String PSP_ID_DESCRIPTION = "psp.id.description";
    public static final String PSP_ID_MESSAGE_ERROR = "psp.id.error.message";

    public static final String MERCHANT_GUID_KEY = "merchantGuid";
    public static final String MERCHANT_GUID_LABEL = "merchant.guid.label";
    public static final String MERCHANT_GUID_DESCRIPTION = "merchant.guid.description";
    public static final String MERCHANT_GUID_MESSAGE_ERROR = "merchant.guid.error.message";

    public static final String PAYMENT_REQUEST_URL = "staging/payments/v1/purchase/facilypay_url";
    public static final String CONFIRM_REQUEST_URL = "";
    public static final String CANCEL_REQUEST_URL = "";
    public static final String STATUS_REQUEST_URL = "";

}
