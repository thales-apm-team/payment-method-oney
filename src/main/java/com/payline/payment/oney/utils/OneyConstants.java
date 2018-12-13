package com.payline.payment.oney.utils;

public class OneyConstants {

    private OneyConstants() {
    }

    //Message common with all payment method
    public static final String PAYMENT_METHOD_NAME = "paymentMethod.name";

    public static final String RELEASE_DATE_FORMAT = "dd/MM/yyyy";
    public static final String RELEASE_DATE = "release.date";
    public static final String RELEASE_VERSION = "release.version";
    public static final String RELEASE_PROPERTIES = "release.properties";

    public static final String I18N_SERVICE_DEFAULT_LOCALE = "en";
    public static final String RESOURCE_BUNDLE_BASE_NAME = "messages";

    //Headers
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_TYPE_VALUE = "application/json";
    public static final String AUTHORIZATION = "X-Oney-Authorization";
    public static final String SECRET_KEY = "X-Oney-Secret";
    public static final String SECRET_VALUE = "Method-body";


    public static final String CONFIG_HTTP_CONNECT_TIMEOUT = "http.connectTimeout";
    public static final String CONFIG_HTTP_WRITE_TIMEOUT = "http.writeTimeout";
    public static final String CONFIG_HTTP_READ_TIMEOUT = "http.readTimeout";


    //Constant specific of this payment method
    public static final String AUTHORIZATION_VALUE = "oney.authorization";
    public static final String CHIFFREMENT_KEY = "66s581CG5W+RLEqZHAGQx+vskjy660Kt8x8rhtRpXtY=";
//    public static final String CHIFFREMENT_KEY = "merchant.crypto.key";

    public static final String X_ONEY_AUTHORIZATION_LABEL = "api.key.label";
    public static final String X_ONEY_AUTHORIZATION_KEY = "apiKey";
    public static final String X_ONEY_AUTHORIZATION_DESCRIPTION = "api.key.description";
    public static final String X_ONEY_AUTHORIZATION_MESSAGE_ERROR = "api.key.error.message";

    public static final String PSP_GUID_LABEL = "psp.id.label";
    public static final String PSP_GUID_KEY = "pspId";
    public static final String PSP_GUID_DESCRIPTION = "psp.id.description";
    public static final String PSP_GUID_MESSAGE_ERROR = "psp.id.error.message";

    public static final String MERCHANT_GUID_KEY = "merchantGuid";
    public static final String MERCHANT_GUID_LABEL = "merchant.guid.label";
    public static final String MERCHANT_GUID_DESCRIPTION = "merchant.guid.description";
    public static final String MERCHANT_GUID_MESSAGE_ERROR = "merchant.guid.error.message";

    public static final String API_MARKETING_KEY = "apiMarketing";
    public static final String API_MARKETING_LABEL = "api.marketing.label";
    public static final String API_MARKETING_DESCRIPTION = "api.marketing.description";
    public static final String API_MARKETING_MESSAGE_ERROR = "api.marketing.error.message";

    public static final String OPC_KEY = "opc";
    public static final String OPC_LABEL = "opc.label";
    public static final String OPC_DESCRIPTION = "opc.description";
    public static final String OPC_MESSAGE_ERROR = "opc.error.message";

    public static final String NB_ECHEANCES_KEY = "nbEcheances";
    public static final String NB_ECHEANCES_LABEL = "nb.echeances.label";
    public static final String NB_ECHEANCES_MESSAGE_ERROR = "nb.echeances.error.message";

    public static final String COUNTRY_CODE_KEY = "X-Oney-Partner-Country-Code";
    public static final String COUNTRY_CODE_VALUE = "country.code.value";
    public static final String COUNTRY_CODE_LABEL = "country.code.label";
    public static final String COUNTRY_CODE_DESCRIPTION = "X-Oney-Partner-Country-Code";
    public static final String COUNTRY_CODE_MESSAGE_ERROR = "country.code.error.message";

    public static final String LANGUAGE_CODE_KEY = "merchant_language_code";
    public static final String LANGUAGE_CODE_DESCRIPTION = "language.code.description";
    public static final String LANGUAGE_CODE_LABEL = "language.code.label";
    public static final String LANGUAGE_CODE_MESSAGE_ERROR = "language.code.error.message";


    public static final String ID_INTERNATIONAL_KEY = "oney_international_id";
    public static final String ID_INTERNATIONAL_DESCRIPTION = "X-Oney-International-Partner-ID";
    public static final String ID_INTERNATIONAL_LABEL = "international.code.label";
    public static final String ID_INTERNATIONAL_MESSAGE_ERROR = "international.code.error.message";


    public static final String SCHEME = "https";
    public static final String SANDBOX_URL = "oney-staging.azure-api.net";
    public static final String PRODUCTION_URL = "oney-staging.azure-api.net";

    public static final String CONFIRM_SANDBOX_URL = "api-staging.oney.io";
    public static final String CONFIRM_PRODUCTION_URL = "api-staging.oney.io";


    //Request URL's
    public static final String PAYMENT_REQUEST_URL = "staging/payments/v1/purchase/facilypay_url";
    public static final String CONFIRM_REQUEST_URL = "staging/payments/v1/purchase";
    public static final String CANCEL_REQUEST_URL = "";
    public static final String STATUS_REQUEST_URL = "";

    //0 immediate, 1 deferred, 2 check card
    public static final int PAYMENT_TYPE = 0;

    public static final String BUSINESS_TRANSACTION_CODE = "OPC";
    //fixme changer de localisation? config.properties ?
    public static final int BUSINESS_TRANSACTION_VERSION = 1;
    //PNFCB / AFFECTE / NONAMORTISSABLE
    public static final String BUSINESS_TRANSACTION_TYPE = "PNFCB";

    public static final String EXTERNAL_REFERENCE_TYPE = "CMDE";
    public static final String EXTERNAL_REFERENCE_KEY = "externalReference";
    public static final String PAYMENT_AMOUNT_KEY = "paymentAmount";

//
    //Code HTTP
    public static final int HTTP_OK = 200;

}
