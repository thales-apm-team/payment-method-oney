package com.payline.payment.oney.utils;

public class OneyConstants {

    private OneyConstants() {
    }


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

    public static final String PSP_GUID_LABEL = "psp.id.label";
    public static final String PSP_GUID_KEY = "psp.id";
    public static final String PSP_GUID_DESCRIPTION = "psp.id.description";
    public static final String PSP_GUID_MESSAGE_ERROR = "psp.id.error.message";

    public static final String MERCHANT_GUID_KEY = "merchantGuid";
    public static final String MERCHANT_GUID_LABEL = "merchant.guid.label";
    public static final String MERCHANT_GUID_DESCRIPTION = "merchant.guid.description";
    public static final String MERCHANT_GUID_MESSAGE_ERROR = "merchant.guid.error.message";

    public static final String OPC_KEY = "opc";
    public static final String OPC_LABEL = "opc.label";
    public static final String OPC_DESCRIPTION = "opc.description";
    public static final String OPC_MESSAGE_ERROR = "opc.error.message";

    public static final String NB_ECHEANCES_KEY = "nbEcheances";
    public static final String NB_ECHEANCES_LABEL = "nb.echeances.label";
    public static final String NB_ECHEANCES_MESSAGE_ERROR = "nb.echeances.error.message";

    public static final String COUNTRY_CODE_KEY = "xOneyPatnerCountryCode";
    public static final String COUNTRY_CODE_HEADER = "X-Oney-Partner-Country-Code";
    public static final String COUNTRY_CODE_LABEL = "country.code.label";
    public static final String COUNTRY_CODE_DESCRIPTION = "X-Oney-Partner-Country-Code";
    public static final String COUNTRY_CODE_MESSAGE_ERROR = "country.code.error.message";

    public static final String LANGUAGE_CODE_KEY = "merchantLanguageCode";
    public static final String LANGUAGE_CODE_DESCRIPTION = "language.code.description";
    public static final String LANGUAGE_CODE_LABEL = "language.code.label";
    public static final String LANGUAGE_CODE_MESSAGE_ERROR = "language.code.error.message";


    public static final String ID_INTERNATIONAL_KEY = "oneyInternationalId";
    public static final String ID_INTERNATIONAL_DESCRIPTION = "X-Oney-International-Partner-ID";
    public static final String ID_INTERNATIONAL_LABEL = "international.code.label";
    public static final String ID_INTERNATIONAL_MESSAGE_ERROR = "international.code.error.message";


    public static final String PARTNER_AUTHORIZATION_VALUE = "oney.authorization";
    public static final String PARTNER_CHIFFREMENT_KEY = "partner.oney.access.secret.key";
    public static final String PARTNER_CHIFFREMENT_DESCRIPTION = "partner.oney.access.secret.key.description";
    public static final String PARTNER_CHIFFREMENT_LABEL = "partner.oney.access.secret.key.label";
    public static final String PARTNER_CHIFFREMENT_KEY_MESSAGE_ERROR = "partner.oney.access.secret.key.error.message";
    public static final String PARTNER_AUTHORIZATION_KEY = "partner.oney.access.key";
    public static final String PARTNER_AUTHORIZATION_KEY_MESSAGE_ERROR = "partner.oney.access.key.error.message";
    public static final String PARTNER_API_URL = "partner.oney.url";
    public static final String HEADER_COUNTRY_CODE = "oney.coutry.code";


    public static final String PIPE = "%7C";


    //Request URL's
    public static final String PAYMENT_REQUEST_URL = "/staging/payments/v1/purchase/facilypay_url";
    public static final String CONFIRM_REQUEST_URL = "/staging/payments/v1/purchase";
    public static final String CANCEL_REQUEST_URL = "/staging/payments/v1/purchase";
    public static final String STATUS_REQUEST_URL = "/staging/payments/v1/purchase";

    //0 immediate, 1 deferred, 2 check card
    public static final int PAYMENT_TYPE = 0;

    // Paramètre optionnel, n'est pas utilisé dans la version actuelle
    public static final int BUSINESS_TRANSACTION_VERSION = 1;
    //PNFCB / AFFECTE / NONAMORTISSABLE
    public static final String BUSINESS_TRANSACTION_TYPE = "PNFCB";

    public static final String EXTERNAL_REFERENCE_TYPE = "CMDE";
    public static final String EXTERNAL_REFERENCE_KEY = "externalReference";
    public static final String PAYMENT_AMOUNT_KEY = "paymentAmount";

    //Code HTTP
    public static final int HTTP_OK = 200;
    public static final int HTTP_401 = 401;
    public static final int HTTP_400 = 400;
    public static final int HTTP_500 = 500;


    public static final String COUNTRY_NOT_ISO = "contract.errors.countryNotISO";
    public static final String LANGUAGE_NOT_ISO = "contract.errors.languageNotISO";

    public static final String TEST_JSON_MSG = "{" +
            "\"merchant_request_id\": \"xxx\"," +
            "\"purchase\": {" +
            "\"external_reference_type\": \"CMDE\"," +
            "\"external_reference\": \"0\"," +
            "\"purchase_amount\": 0," +
            "\"currency_code\": \"EUR\"," +
            "\"delivery\": {" +
            "\"delivery_date\": \"2019-01-17\"," +
            "\"delivery_mode_code\": 1," +
            "\"delivery_option\": 2," +
            "\"address_type\": 5," +
            "\"recipient\": {" +
            "\"recipient_honorific_code\": 1," +
            "\"surname\": \"xxx\"," +
            "\"first_name\": \"xxx\"," +
            "\"phone_number\": \"0\"" +
            "}," +
            "\"delivery_address\": {" +
            "\"line1\": \"1 rue xxx\"," +
            "\"postal_code\": \"1000\"," +
            "\"municipality\": \"xxx\"," +
            "\"country_code\": \"#COUNTRY_ADDRESS#\"," +
            "\"country_label\": \"xxx\"" +
            "}" +
            "}," +
            "\"item_list\": [{" +
            "\"is_main_item\": 1," +
            "\"category_code\": 15," +
            "\"label\": \"xxx\"," +
            "\"item_external_code\": \"xxx\"," +
            "\"quantity\": 0," +
            "\"price\": 0" +
            "}]," +
            "\"number_of_items\": 1" +
            "}," +
            "\"customer\": {" +
            "\"customer_external_code\": \"xxx\"," +
            "\"language_code\": \"#LANGUAGE_CODE#\"," +
            "\"identity\": {" +
            "\"person_type\": 2," +
            "\"honorific_code\": 1," +
            "\"birth_name\": \"xxx\"," +
            "\"first_name\": \"xxx\"," +
            "\"birth_date\": \"1981-05-04\"" +
            "}," +
            "\"contact_details\": {" +
            "\"landline_number\": \"0\"," +
            "\"mobile_phone_number\": \"0\"," +
            "\"email_address\": \"xxx@xxx.xx\"" +
            "}," +
            "\"customer_address\": {" +
            "\"line1\": \"1 rue xxx\"," +
            "\"postal_code\": \"1000\"," +
            "\"municipality\": \"xxx\"," +
            "\"country_code\": \"#COUNTRY_ADDRESS#\"," +
            "\"country_label\": \"xxx\"" +
            "}" +
            "}," +
            "\"payment\": {" +
            "\"payment_amount\": 0," +
            "\"currency_code\": \"EUR\"," +
            "\"payment_type\": 0," +
            "\"business_transaction\": {" +
            "\"code\": \"#OPC_KEY#\"" +
            "}" +
            "}," +
            "\"navigation\": {" +
            "\"server_response_url\": \"http://example.com/servlet\"," +
            "\"success_url\": \"http://example.com/servlet\"," +
            "\"fail_url\": \"http://example.com/servlet\"" +
            "}," +
            "\"merchant_guid\": \"#MERCHANT_GUID_KEY#\"," +
            "\"psp_guid\": \"#PSP_GUID_KEY#\"" +
            "}";


    public static final String OPC_KEY_TAG = "#OPC_KEY#";
    public static final String MERCHANT_GUID_TAG = "#MERCHANT_GUID_KEY#";
    public static final String PSP_GUID_TAG = "#PSP_GUID_KEY#";
    public static final String COUNTRY_ADDRESS = "#COUNTRY_ADDRESS#";
    public static final String LANGUAGE_CODE = "#LANGUAGE_CODE#";
    public static final String UNEXPECTED_ERR = "error.unexpected";


    public static final String DATE_FORMAT = "\\d{4}-\\d{2}-\\d{2}";
}
