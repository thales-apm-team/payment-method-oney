package com.payline.payment.oney.utils.http;


import com.payline.payment.oney.bean.request.*;
import com.payline.payment.oney.exception.DecryptException;
import com.payline.payment.oney.utils.OneyConstants;
import com.payline.payment.oney.utils.config.ConfigEnvironment;
import com.payline.payment.oney.utils.properties.service.ConfigPropertiesEnum;
import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static com.payline.payment.oney.utils.OneyConstants.*;

/**
 * Created by Thales on  27/11/2018
 */
public class OneyHttpClient extends AbstractHttpClient {

    public static final String PSP_GUID = "psp_guid";
    public static final String MERCHANT_GUID = "merchant_guid";
    public static final String REFERENCE = "reference";
    public static final String PSP_GUID_TAG = "/psp_guid/";
    public static final String MERCHANT_GUID_TAG = "/merchant_guid/";
    public static final String REFERENCE_TAG = "/reference/";

    /**
     * Instantiate a HTTP client with default values.
     */
    private OneyHttpClient() {
        super();
    }

    /**
     * Singleton Holder
     */
    private static class SingletonHolder {
        private static final OneyHttpClient INSTANCE = new OneyHttpClient();
    }

    /**
     * @return the singleton instance
     */
    public static OneyHttpClient getInstance() {
        return SingletonHolder.INSTANCE;
    }


    /**
     * Send a POST request, with a XML content type.
     *
     * @param host           URL host
     * @param path           URL path
     * @param requestContent The JSON content, as a string
     * @return The response returned from the HTTP call
     * @throws IOException        I/O error
     * @throws URISyntaxException URI Syntax Exception
     */
    public StringResponse doPost(String host, String path, String requestContent, boolean isSandbox, String countryCode) throws IOException, URISyntaxException {

        StringEntity entity = new StringEntity(requestContent);
        ConfigEnvironment env = (isSandbox) ? ConfigEnvironment.DEV : ConfigEnvironment.PROD;

        Header[] headers = createHeaders(env, countryCode);


        return super.doPost(SCHEME, host, path, headers, entity);

    }

    /**
     * Send a GET request, with a XML content type.
     *
     * @param host URL host
     * @param path URL path
     * @return The response returned from the HTTP call
     * @throws IOException        I/O error
     * @throws URISyntaxException URI Syntax Exception
     */
    public StringResponse doGet(String host, String path, Map<String, String> param, boolean isSandbox, String countryCode) throws IOException, URISyntaxException {


        //build Request
        String finalPath = this.buildGetOrderPath(path, param);

        ConfigEnvironment env = (isSandbox) ? ConfigEnvironment.DEV : ConfigEnvironment.PROD;

        Header[] headers = createHeaders(env, countryCode);


        return super.doGet(SCHEME, host, finalPath, headers);
    }

    public String buildGetOrderPath(String path, Map<String, String> param) {

        return path + PSP_GUID_TAG + param.get(PSP_GUID)
                + MERCHANT_GUID_TAG + param.get(MERCHANT_GUID)
                + REFERENCE_TAG + param.get(REFERENCE);

    }

    public String buildConfirmOrderPath(String path, Map<String, String> param) {

        return buildGetOrderPath(path, param) + "/action/confirm";
    }

    public String buildRefundOrderPath(String path, Map<String, String> param) {

        return buildGetOrderPath(path, param) + "/action/cancel";
    }

    /**
     * Create header for POST/GET methdod
     *
     * @return Header[]  header data
     */
    private Header[] createHeaders(ConfigEnvironment env, String countryCode) {
        Header[] headers = new Header[4];
        headers[0] = new BasicHeader(CONTENT_TYPE, CONTENT_TYPE_VALUE);
        headers[1] = new BasicHeader(AUTHORIZATION, ConfigPropertiesEnum.INSTANCE.get(AUTHORIZATION_VALUE, env));
        headers[2] = new BasicHeader(COUNTRY_CODE_KEY, countryCode.toUpperCase());
        headers[3] = new BasicHeader(SECRET_KEY, SECRET_VALUE);

        return headers;
    }

    private String getHost(boolean isSandbox) {
        return isSandbox ? OneyConstants.SANDBOX_URL : OneyConstants.PRODUCTION_URL;
    }


    public StringResponse initiatePayment(OneyPaymentRequest request, boolean isSandbox, String countryCode) throws IOException, URISyntaxException, DecryptException {
        String host = getHost(isSandbox);
        OneyEncryptedRequest requestEncrypted = OneyEncryptedRequest.fromOneyPaymentRequest(request);
        String jsonBody = requestEncrypted.toString();
        // do the request
        return doPost(host, PAYMENT_REQUEST_URL, jsonBody, isSandbox, countryCode);

    }

    public StringResponse initiateConfirmationPayment(OneyConfirmRequest request, boolean isSandbox, String countryCode) throws IOException, URISyntaxException, DecryptException {
        String host = getHost(isSandbox);
        Map<String, String> parameters = new HashMap<>();
        parameters.put(PSP_GUID, request.getPspGuid());
        parameters.put(MERCHANT_GUID, request.getMerchantGuid());
        parameters.put(REFERENCE, request.getPurchaseReference());
        String path = buildConfirmOrderPath(CONFIRM_REQUEST_URL, parameters);
        OneyEncryptedRequest requestEncrypted = OneyEncryptedRequest.fromOneyConfirmRequest(request);
        String jsonBody = requestEncrypted.toString();
        // do the request
        return doPost(host, path, jsonBody, isSandbox, countryCode);

    }

    public StringResponse initiateRefundPayment(OneyRefundRequest request, boolean isSandbox, String countryCode) throws IOException, URISyntaxException, DecryptException {
        String host = getHost(isSandbox);
        Map<String, String> parameters = new HashMap<>();
        parameters.put(PSP_GUID, request.getPspGuid());
        parameters.put(MERCHANT_GUID, request.getMerchantGuid());
        parameters.put(REFERENCE, request.getPurchaseReference());
        String path = buildRefundOrderPath(CANCEL_REQUEST_URL, parameters);
        OneyEncryptedRequest requestEncrypted = OneyEncryptedRequest.fromOneyRefundRequest(request);
        String jsonBody = requestEncrypted.toString();
        // do the request
        return doPost(host, path, jsonBody, isSandbox, countryCode);

    }

    public StringResponse initiateGetTransactionStatus(OneyTransactionStatusRequest request, boolean isSandbox, String countryCode) throws IOException, URISyntaxException, DecryptException {
        String host = getHost(isSandbox);
        Map<String, String> parameters = new HashMap<>();
        parameters.put(PSP_GUID, request.getPspGuid());
        parameters.put(MERCHANT_GUID, request.getMerchantGuid());
        parameters.put(REFERENCE, request.getPurchaseReference());
        // do the request
        return doGet(host, STATUS_REQUEST_URL, parameters, isSandbox, countryCode);

    }
}
