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

    public static final String TEST_JSON_MSG = "{\n" +
            "\t\"merchant_request_id\": \"xxx\",\n" +
            "\t\"purchase\": {\n" +
            "\t\t\"external_reference_type\": \"CMDE\",\n" +
            "\t\t\"external_reference\": \"0\",\n" +
            "\t\t\"purchase_amount\": 0,\n" +
            "\t\t\"currency_code\": \"EUR\",\n" +
            "\t\t\"delivery\": {\n" +
            "\t\t\t\"delivery_date\": \"2019-01-17\",\n" +
            "\t\t\t\"delivery_mode_code\": 1,\n" +
            "\t\t\t\"delivery_option\": 2,\n" +
            "\t\t\t\"address_type\": 5,\n" +
            "\t\t\t\"recipient\": {\n" +
            "\t\t\t\t\"recipient_honorific_code\": 1,\n" +
            "\t\t\t\t\"surname\": \"xxx\",\n" +
            "\t\t\t\t\"first_name\": \"xxx\",\n" +
            "\t\t\t\t\"phone_number\": \"0\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"delivery_address\": {\n" +
            "\t\t\t\t\"line1\": \"1 rue xxx\",\n" +
            "\t\t\t\t\"postal_code\": \"1000\",\n" +
            "\t\t\t\t\"municipality\": \"xxx\",\n" +
            "\t\t\t\t\"country_code\": \"BEL\",\n" +
            "\t\t\t\t\"country_label\": \"xxx\"\n" +
            "\t\t\t}\n" +
            "\t\t},\n" +
            "\t\t\"item_list\": [{\n" +
            "\t\t\t\"is_main_item\": 1,\n" +
            "\t\t\t\"category_code\": 15,\n" +
            "\t\t\t\"label\": \"xxx\",\n" +
            "\t\t\t\"item_external_code\": \"xxx\",\n" +
            "\t\t\t\"quantity\": 0,\n" +
            "\t\t\t\"price\": 0\n" +
            "\t\t}],\n" +
            "\t\t\"number_of_items\": 1\n" +
            "\t},\n" +
            "\t\"customer\": {\n" +
            "\t\t\"customer_external_code\": \"xxx\",\n" +
            "\t\t\"language_code\": \"fr\",\n" +
            "\t\t\"identity\": {\n" +
            "\t\t\t\"person_type\": 2,\n" +
            "\t\t\t\"honorific_code\": 1,\n" +
            "\t\t\t\"birth_name\": \"xxx\",\n" +
            "\t\t\t\"first_name\": \"xxx\",\n" +
            "\t\t\t\"birth_date\": \"1981-05-04\"\n" +
            "\t\t},\n" +
            "\t\t\"contact_details\": {\n" +
            "\t\t\t\"landline_number\": \"0\",\n" +
            "\t\t\t\"mobile_phone_number\": \"0\",\n" +
            "\t\t\t\"email_address\": \"xxx@xxx.xx\"\n" +
            "\t\t},\n" +
            "\t\t\"customer_address\": {\n" +
            "\t\t\t\"line1\": \"1 rue xxx\",\n" +
            "\t\t\t\"postal_code\": \"1000\",\n" +
            "\t\t\t\"municipality\": \"xxx\",\n" +
            "\t\t\t\"country_code\": \"BEL\",\n" +
            "\t\t\t\"country_label\": \"xxx\"\n" +
            "\t\t}\n" +
            "\t},\n" +
            "\t\"payment\": {\n" +
            "\t\t\"payment_amount\": 0,\n" +
            "\t\t\"currency_code\": \"EUR\",\n" +
            "\t\t\"payment_type\": 0,\n" +
            "\t\t\"business_transaction\": {\n" +
            "\t\t\t\"code\": \"#OPC_KEY#\"\n" +
            "\t\t}\n" +
            "\t},\n" +
            "\t\"navigation\": {\n" +
            "\t\t\"server_response_url\": \"xxx\",\n" +
            "\t\t\"success_url\": \"xxx\",\n" +
            "\t\t\"fail_url\": \"xxx\"\n" +
            "\t},\n" +
            "\t\"merchant_guid\": \"#MERCHANT_GUID_KEY#\",\n" +
            "\t\"psp_guid\": \"#PSP_GUID_KEY#\"\n" +
            "}";


    public static final String OPC_KEY_TAG = "#OPC_KEY#";
    public static final String MERCHANT_GUID_TAG = "#MERCHANT_GUID_KEY#";
    public static final String PSP_GUID_TAG = "#PSP_GUID_KEY#";
    public static final String UNEXPECTED_ERR = "error.unexpected";


    public static final String DATE_FORMAT = "\\d{4}-\\d{2}-\\d{2}";
}
