package com.payline.payment.oney.utils.http;

import com.payline.payment.oney.exception.HttpCallException;
import com.payline.payment.oney.utils.properties.service.ConfigPropertiesEnum;
import com.payline.pmapi.logger.LogManager;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static com.payline.payment.oney.utils.OneyConstants.*;

/**
 * This utility class provides a basic HTTP client to send requests, using OkHttp library.
 * It must be extended to match each payment method needs.
 */
public abstract class AbstractHttpClient {

    private CloseableHttpClient client;
    private static final Logger LOGGER = LogManager.getLogger(AbstractHttpClient.class);


    /**
     * Instantiate a HTTP client.
     */

    protected AbstractHttpClient() {

        int connectTimeout = Integer.parseInt(ConfigPropertiesEnum.INSTANCE.get(CONFIG_HTTP_CONNECT_TIMEOUT));
        int requestTimeout = Integer.parseInt(ConfigPropertiesEnum.INSTANCE.get(CONFIG_HTTP_WRITE_TIMEOUT));
        int readTimeout = Integer.parseInt(ConfigPropertiesEnum.INSTANCE.get(CONFIG_HTTP_READ_TIMEOUT));


        final RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(connectTimeout * 1000)
                .setConnectionRequestTimeout(requestTimeout * 1000)
                .setSocketTimeout(readTimeout * 1000)
                .build();

        this.client = HttpClientBuilder.create()
                .useSystemProperties()
                .setDefaultRequestConfig(requestConfig)
                .setDefaultCredentialsProvider(new BasicCredentialsProvider())
                .setSSLSocketFactory(new SSLConnectionSocketFactory(HttpsURLConnection.getDefaultSSLSocketFactory(), SSLConnectionSocketFactory.getDefaultHostnameVerifier())).build();

    }

    /**
     * Send a POST request.
     *
     * @param url  URL scheme + host
     * @param path URL path
     * @param body Request body
     * @return The response returned from the HTTP call
     * @throws HttpCallException COMMUNICATION_ERROR
     */
    protected StringResponse doPost(String url, String path, Header[] headers, HttpEntity body) throws HttpCallException {
        final String methodName = "doPost";

        try {
            URI uri = new URI(url + path);

            final HttpPost httpPostRequest = new HttpPost(uri);
            httpPostRequest.setHeaders(headers);
            httpPostRequest.setEntity(body);

            return getStringResponse(url, methodName, httpPostRequest);

        } catch (URISyntaxException e) {
            LOGGER.error(e.getMessage(), e);
            throw new HttpCallException(e, "AbstractHttpClient.doPost.URISyntaxException");
        }


    }

    private StringResponse getStringResponse(String url, String methodName, HttpRequestBase httpPostRequest) throws HttpCallException {
        final long start = System.currentTimeMillis();
        int count = 0;
        StringResponse strResponse = null;
        String errMsg = null;
        while (count < 3 && strResponse == null) {
            try (CloseableHttpResponse httpResponse = this.client.execute(httpPostRequest)) {

                LOGGER.info("Start partner call... [URL: {}]", url);

                strResponse = new StringResponse();
                strResponse.setCode(httpResponse.getStatusLine().getStatusCode());
                strResponse.setMessage(httpResponse.getStatusLine().getReasonPhrase());

                if (httpResponse.getEntity() != null) {
                    final String responseAsString = EntityUtils.toString(httpResponse.getEntity());
                    strResponse.setContent(responseAsString);
                }
                final long end = System.currentTimeMillis();

                LOGGER.info("End partner call [T: {}ms] [CODE: {}]", end - start, strResponse.getCode());

            } catch (final IOException e) {
                LOGGER.error("Error while partner call [T: {}ms]", System.currentTimeMillis() - start, e);
                strResponse = null;
                errMsg = e.getMessage();
            } finally {
                count++;
            }
        }

        if (strResponse == null) {
            if (errMsg == null) {
                throw new HttpCallException("Http response is empty", "AbstractHttpClient." + methodName + " : empty partner response");
            }
            throw new HttpCallException(errMsg, "AbstractHttpClient." + methodName + ".IOException");
        }
        return strResponse;
    }


    /**
     * Send a GET request
     *
     * @param url  URL RL scheme + host
     * @param path URL path
     * @return The response returned from the HTTP call
     * @throws HttpCallException COMMUNICATION_ERROR
     */

    protected StringResponse doGet(String url, String path, Header[] headers) throws HttpCallException {
        final String methodName = "doGet";
        try {
            URI uri = new URI(url + path);

            final HttpGet httpGetRequest = new HttpGet(uri);
            httpGetRequest.setHeaders(headers);

            return getStringResponse(url, methodName, httpGetRequest);
        } catch (URISyntaxException e) {
            throw new HttpCallException(e, "AbstractHttpClient.doGet.URISyntaxException");
        }


    }


}
