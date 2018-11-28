package com.payline.payment.oney.utils.http;


import com.payline.payment.oney.utils.config.ConfigEnvironment;
import com.payline.payment.oney.utils.config.ConfigProperties;
import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;

import java.io.IOException;
import java.net.URISyntaxException;
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
     * @param scheme     URL scheme
     * @param host       URL host
     * @param path       URL path
     * @param requestContent The JSON content, as a string
     * @return The response returned from the HTTP call
     * @throws IOException        I/O error
     * @throws URISyntaxException URI Syntax Exception
     */
    public StringResponse doPost(String scheme, String host, String path, String requestContent) throws IOException, URISyntaxException {

        StringEntity entity = new StringEntity(requestContent);
        Header[] headers = createHeaders(AUTHORIZATION_VALUE,COUNTRY_CODE_VALUE,ConfigEnvironment.DEV);


        return super.doPost(scheme, host, path,headers, entity);

    }

    /**
     * Send a GET request, with a XML content type.
     *
     * @param scheme     URL scheme
     * @param host       URL host
     * @param path       URL path
     * @return The response returned from the HTTP call
     * @throws IOException        I/O error
     * @throws URISyntaxException URI Syntax Exception
     */
    public StringResponse doGet(String scheme, String host, String path,Map<String, String> param) throws IOException, URISyntaxException {


        //build Request
        String finalPath = this.buildGetOrderPath(path,param);
        Header[] headers = createHeaders(AUTHORIZATION_VALUE,COUNTRY_CODE_VALUE,ConfigEnvironment.DEV);


        return super.doGet(scheme, host, finalPath,headers);
    }

    public String buildGetOrderPath(String path, Map<String, String> param){

        String finalPath = "";

            finalPath = finalPath + "psp_guid/" + param.get("psp_guid");
            finalPath = finalPath + "/merchant_guid/" + param.get("merchant_guid");
            finalPath = finalPath + "/reference/" + param.get("reference");

        return path+ finalPath;
    }

    /**
     * Create header for POST/GET methdod
     * @param authentication authentication credential
     * @param countryCode country code of the merchant
     * @return Header[]  header data
     */
    private Header[] createHeaders(String authentication, String countryCode, ConfigEnvironment env) {
        Header[] headers = new Header[4];
        headers[0] = new BasicHeader(CONTENT_TYPE, CONTENT_TYPE_VALUE);
        headers[1] = new BasicHeader(AUTHORIZATION, ConfigProperties.get(authentication, env) );
        headers[2] = new BasicHeader(COUNTRY_CODE_KEY, ConfigProperties.get(countryCode, env));
        headers[3] = new BasicHeader(SECRET_KEY, SECRET_VALUE);

        return headers;
    }
}
