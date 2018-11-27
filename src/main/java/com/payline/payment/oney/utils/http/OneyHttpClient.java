package com.payline.payment.oney.utils.http;


import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.net.URISyntaxException;

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
     * @param scheme     URL scheme
     * @param host       URL host
     * @param path       URL path
     * @param requestContent The JSON content, as a string
     * @param credential The authentication credential
     * @return The response returned from the HTTP call
     * @throws IOException        I/O error
     * @throws URISyntaxException URI Syntax Exception
     */
    public StringResponse doPost(String scheme, String host, String path, String requestContent, String credential) throws IOException, URISyntaxException {

        StringEntity entity = new StringEntity(requestContent);

        return super.doPost(scheme, host, path, entity, credential);

    }

    /**
     * Send a GET request, with a XML content type.
     *
     * @param scheme     URL scheme
     * @param host       URL host
     * @param path       URL path
     * @param credential The authentication credential
     * @return The response returned from the HTTP call
     * @throws IOException        I/O error
     * @throws URISyntaxException URI Syntax Exception
     */
    public StringResponse doGet(String scheme, String host, String path, String credential) throws IOException, URISyntaxException {
        return super.doGet(scheme, host, path, credential);
    }


}
