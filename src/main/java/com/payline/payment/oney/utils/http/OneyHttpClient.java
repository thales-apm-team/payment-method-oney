package com.payline.payment.oney.utils.http;


import com.payline.payment.oney.exception.DecryptException;
import com.payline.payment.oney.service.impl.request.OneyConfirmRequest;
import com.payline.payment.oney.service.impl.request.OneyEncryptedRequest;
import com.payline.payment.oney.service.impl.request.OneyPaymentRequest;
import com.payline.payment.oney.service.impl.request.OneyTransactionStatusRequest;
import com.payline.payment.oney.utils.OneyConstants;
import com.payline.payment.oney.utils.config.ConfigEnvironment;
import com.payline.payment.oney.utils.config.ConfigProperties;
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
     * @param scheme         URL scheme
     * @param host           URL host
     * @param path           URL path
     * @param requestContent The JSON content, as a string
     * @return The response returned from the HTTP call
     * @throws IOException        I/O error
     * @throws URISyntaxException URI Syntax Exception
     */
    public StringResponse doPost(String scheme, String host, String path, String requestContent, boolean isSandbox) throws IOException, URISyntaxException {

        StringEntity entity = new StringEntity(requestContent);
        ConfigEnvironment env = (isSandbox) ? ConfigEnvironment.DEV : ConfigEnvironment.PROD;

        Header[] headers = createHeaders(env);


        return super.doPost(scheme, host, path, headers, entity);

    }

    /**
     * Send a GET request, with a XML content type.
     *
     * @param scheme URL scheme
     * @param host   URL host
     * @param path   URL path
     * @return The response returned from the HTTP call
     * @throws IOException        I/O error
     * @throws URISyntaxException URI Syntax Exception
     */
    public StringResponse doGet(String scheme, String host, String path, Map<String, String> param, boolean isSandbox) throws IOException, URISyntaxException {


        //build Request
        String finalPath = this.buildGetOrderPath(path, param);

        ConfigEnvironment env = (isSandbox) ? ConfigEnvironment.DEV : ConfigEnvironment.PROD;

        Header[] headers = createHeaders(env);


        return super.doGet(scheme, host, finalPath, headers);
    }

    public String buildGetOrderPath(String path, Map<String, String> param) {

        return path + "/psp_guid/" + param.get("psp_guid")
                + "/merchant_guid/" + param.get("merchant_guid")
                + "/reference/" + param.get("reference");

    }

    public String buildConfirmOrderPath(String path, Map<String, String> param) {

        return buildGetOrderPath(path, param) + "/action/confirm";
    }

    /**
     * Create header for POST/GET methdod
     *
     * @return Header[]  header data
     */
    private Header[] createHeaders(ConfigEnvironment env) {
        Header[] headers = new Header[4];
        headers[0] = new BasicHeader(CONTENT_TYPE, CONTENT_TYPE_VALUE);
        headers[1] = new BasicHeader(AUTHORIZATION, ConfigProperties.get(AUTHORIZATION_VALUE, env));
        headers[2] = new BasicHeader(COUNTRY_CODE_KEY, ConfigProperties.get(COUNTRY_CODE_VALUE, env));
        headers[3] = new BasicHeader(SECRET_KEY, SECRET_VALUE);

        return headers;
    }

    private String getHost(boolean isSandbox) {
        return isSandbox ? OneyConstants.SANDBOX_URL : OneyConstants.PRODUCTION_URL;
    }

//    private String getConfirmHost(boolean isSandbox) {
//        return isSandbox ? OneyConstants.CONFIRM_SANDBOX_URL : OneyConstants.CONFIRM_PRODUCTION_URL;
//    }

    public StringResponse initiatePayment(OneyPaymentRequest request, boolean isSandbox) throws IOException, URISyntaxException, DecryptException {
        String host = getHost(isSandbox);
        OneyEncryptedRequest requestEncrypted = OneyEncryptedRequest.fromOneyPaymentRequest(request);
        String jsonBody = requestEncrypted.toString();
        // do the request
        return doPost(SCHEME, host, PAYMENT_REQUEST_URL, jsonBody, isSandbox);

    }

    public StringResponse initiateConfirmationPayment(OneyConfirmRequest request, boolean isSandbox) throws IOException, URISyntaxException, DecryptException {
        String host = getHost(isSandbox);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("psp_guid", request.getPspGuid());
        parameters.put("merchant_guid", request.getMerchantGuid());
        parameters.put("reference", request.getPurchaseReference());
        String path = buildGetOrderPath(CONFIRM_REQUEST_URL, parameters);
        OneyEncryptedRequest requestEncrypted = OneyEncryptedRequest.fromOneyConfirmRequest(request);
        String jsonBody = requestEncrypted.toString();
        // do the request
        return doPost(SCHEME, host, path, jsonBody,isSandbox);

    }

    public StringResponse initiateGetTransactionStatus(OneyTransactionStatusRequest request, boolean isSandbox) throws IOException, URISyntaxException, DecryptException {
        String host = getHost(isSandbox);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("psp_guid", request.getPspGuid());
        parameters.put("merchant_guid", request.getMerchantGuid());
        parameters.put("reference", request.getPurchaseReference());
        // do the request
        return doGet(SCHEME, host, STATUS_REQUEST_URL, parameters, isSandbox);

    }
}
