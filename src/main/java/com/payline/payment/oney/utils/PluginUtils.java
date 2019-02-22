package com.payline.payment.oney.utils;


import com.payline.payment.oney.bean.common.purchase.Purchase;
import com.payline.payment.oney.exception.InvalidDataException;
import com.payline.payment.oney.exception.InvalidFieldFormatException;
import com.payline.payment.oney.exception.InvalidRequestException;
import com.payline.payment.oney.service.impl.RequestConfigServiceImpl;
import com.payline.payment.oney.utils.properties.service.ConfigPropertiesEnum;
import com.payline.pmapi.bean.common.Buyer;
import com.payline.pmapi.bean.configuration.request.ContractParametersCheckRequest;
import com.payline.pmapi.bean.payment.request.PaymentRequest;
import com.payline.pmapi.bean.payment.request.TransactionStatusRequest;
import com.payline.pmapi.bean.refund.request.RefundRequest;

import java.math.BigInteger;
import java.util.*;
import java.util.regex.Pattern;

import static com.payline.payment.oney.utils.OneyConstants.*;

public class PluginUtils {

    public static final String LINE_1 = "line1";
    public static final String LINE_4 = "line4";

    private PluginUtils() {
        // ras.
    }

    public static boolean isEmpty(String s) {

        return s == null || s.isEmpty();
    }

    public static <T> T requireNonNull(T obj, String message) throws InvalidRequestException {
        if (obj == null) {
            throw new InvalidRequestException(message);
        }
        return obj;
    }

    public static <T> T requireNonNull(Map map, String key, String err) throws InvalidRequestException {
        return PluginUtils.requireNonNull((T) map.get(key), err);
    }

// ------------  Methodes de mapping entre Oney et Payline  -----------------------


    //Mapping methods betwen  Payline and Oney
    public static Integer getPersonType(Buyer.LegalStatus legalStatus) {
        switch (legalStatus) {
            case UNKNOWN:
                return 0;
            case COMPANY:
                return 1;
            case PERSON:
                return 2;
        }
        return null;
    }

    public static Integer getHonorificCode(String civility) {

        if (civility == null) {
            return null;
        }
        switch (civility.toLowerCase()) {
            //Inconnu ex : professeu, Maitre, docteur
            case "7":
            case "9":
            case "10":
            case "11":
                return 0;
            //MR
            case "4":
            case "5":
                return 1;
            //MME
            case "1":
            case "2":
            case "6":
                return 2;
            //MLLE
            case "3":
                return 3;
            default:
                return 1;
        }
    }

    public static Integer getOneyDeliveryModeCode(String paylineCode) {

        if (paylineCode == null) {
            return null;
        }
        switch (paylineCode) {

            // "Collection of the goods in the merchant store":
            case "1":
                return 1;
            // "Collection in a third party point":
            case "2":
                return 2;
            // case "Collection in an airport, train station or travel agency":
            case "3":
                return 3;
            //  case "Carrier (La Poste, Colissimo, UPS, DHL...or any private carrier)":
            case "4":
                return 4;
            //  case "Electronic ticket":
            case "5":
                return 5;

            default:
                return 4;

        }
    }

    public static Integer getOneyDeliveryOption(String paylineCode) {

        if (paylineCode == null) {
            return null;
        }
        switch (paylineCode.toLowerCase()) {
            //  cas "Express'
            case "1":
                return 1;
            //standard
            case "2":
                return 2;
            //non gere par Payline a ce jour
//            case "priority":
//                return 3;
            default:
                return 2;

        }
    }

    /**
     * Decoupe le texte en 5 renvoi un tableau
     *
     * @param longText
     * @param longText2
     * @param size
     * @return
     */
    public static Map<String, String> truncateLongText(String longText, String longText2, int size) {
        Map<String, String> textTruncated = new HashMap();

        StringBuffer sb = new StringBuffer();


        if (longText != null) {
            sb.append(longText.trim());
        }
        if (longText2 != null) {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(longText2.trim());
        }

        splitInLines(textTruncated, sb, size);

        return textTruncated;

    }

    private static void splitInLines(Map<String, String> textTruncated, StringBuffer sb, int max) {
        int i = 1;
        String keyBase = "line";

        // pour être sûr de tjs avoir un " " en fin de buffer.
        sb.append(" ");
        while (sb.length() > 0 && i < 6) {
            int limite = getMaxValue(max, sb.length());
            int end = sb.substring(0, limite).lastIndexOf(" ");
            end = end > 0 ? end : limite;
            String line = sb.substring(0, end);
            textTruncated.put(keyBase + i, line.trim());
            // sprrime les caractères déjà lus.
            sb.delete(0, end);
            int start = 0;
            while (start != sb.length() && Character.isWhitespace(sb.charAt(start))) {
                start++;
            }
            sb.delete(0, start);
            i++;

        }
    }

    private static int getMaxValue(int limit, int size) {
        if (size >= limit) {
            return limit;
        }
        return size;
    }

// --------------------------- FIN methode de mapping -----------------------

    /**
     * Genere un merchant request id qui doit etre unique pour chaque requete
     *
     * @param merchantId
     * @return
     */
    public static String generateMerchantRequestId(String merchantId) {
        return merchantId + Calendar.getInstance().getTimeInMillis();
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


    /**
     * Return a ISO-3166 alpha 3 code  from a ISO-3166 alpha 2 code
     *
     * @param code
     * @return
     */
    public static String getIsoAlpha3CodeFromCountryCode2(String code) {
        if (code == null || code.isEmpty()) {
            return null;
        }
        Locale locale = new Locale("", code);
        return locale.getISO3Country();
    }

    /**
     * Return a country name from a ISO-3166 alpha 2 code
     *
     * @param code
     * @return
     */
    public static String getCountryNameCodeFromCountryCode2(String code) {
        if (code == null || code.isEmpty()) {
            return null;
        }

        Locale locale = new Locale("", code);
        return locale.getDisplayCountry();
    }

    public static String generateReference(Purchase purchase) {

        return purchase.getExternalReferenceType() + OneyConstants.PIPE + purchase.getExternalReference();
    }

    public static String parseReference(String reference) throws InvalidFieldFormatException {

        if (reference == null || reference.isEmpty() || !reference.contains(OneyConstants.PIPE)) {
            throw new InvalidFieldFormatException("Oney reference should contain a '|' : " + reference, "Oney.PurchaseReference");
        }
        return reference.split(OneyConstants.PIPE)[1];
    }

    /**
     * check if a String respect ISO-3166 rules
     *
     * @param countryCode the code to compare
     * @return true if countryCode is in ISO-3166 list, else return false
     */
    public static boolean isISO3166(String countryCode) {
        return countryCode != null && Arrays.asList(Locale.getISOCountries()).contains(countryCode);
    }

    /**
     * check if a String respect ISO-3166 rules
     *
     * @param languageCode the code to compare
     * @return true if languageCode is in ISO-3166 list, else return false
     */
    public static boolean isISO639(String languageCode) {
        return languageCode != null && Arrays.asList(Locale.getISOLanguages()).contains(languageCode);
    }

    /**
     * Return a string which was converted from cents to euro
     *
     * @param amount
     * @return Amount as String
     */
    public static String createStringAmount(BigInteger amount, Currency currency) {
        //récupérer le nombre de digits dans currency
        int nbDigits = currency.getDefaultFractionDigits();

        StringBuilder sb = new StringBuilder();
        sb.append(amount);

        for (int i = sb.length(); i < 3; i++) {
            sb.insert(0, "0");
        }

        sb.insert(sb.length() - nbDigits, ".");
        return sb.toString();
    }

    /**
     * Return a Float which was converted from cents to euro
     *
     * @param amount   BigInteger
     * @param currency Currency
     * @return Amount as Float
     */
    public static Float createFloatAmount(BigInteger amount, Currency currency) {
        if (amount == null || currency == null) {
            return null;
        }
        return Float.parseFloat(createStringAmount(amount, currency));
    }

    /**
     * Buid a map with all needed parameters for HTTP calls
     *
     * @param refundRequest Payline RefundRequest
     * @return the ParametersMap
     */
    public static Map<String, String> getParametersMap(RefundRequest refundRequest) throws InvalidDataException {

        String authorization = RequestConfigServiceImpl.INSTANCE.getParameterValue(refundRequest, PARTNER_AUTHORIZATION_KEY);
        String url = RequestConfigServiceImpl.INSTANCE.getParameterValue(refundRequest, PARTNER_API_URL);
        String coutryCode = RequestConfigServiceImpl.INSTANCE.getParameterValue(refundRequest, COUNTRY_CODE_KEY);
        return getParametersMap(authorization, url, coutryCode);
    }

    /**
     * Buid a map with all needed parameters for HTTP calls
     *
     * @param contractParametersCheckRequest Payline ContractParametersCheckRequest
     * @return the ParametersMap
     */
    public static Map<String, String> getParametersMap(ContractParametersCheckRequest contractParametersCheckRequest) throws InvalidDataException {

        String authorization = RequestConfigServiceImpl.INSTANCE.getParameterValue(contractParametersCheckRequest, PARTNER_AUTHORIZATION_KEY);
        String url = RequestConfigServiceImpl.INSTANCE.getParameterValue(contractParametersCheckRequest, PARTNER_API_URL);
        String coutryCode = RequestConfigServiceImpl.INSTANCE.getParameterValue(contractParametersCheckRequest, COUNTRY_CODE_KEY);
        return getParametersMap(authorization, url, coutryCode);
    }

    /**
     * Buid a map with all needed parameters for HTTP calls
     *
     * @param transactionStatusRequest Payline TransactionStatusRequest
     * @return the ParametersMap
     */
    public static Map<String, String> getParametersMap(TransactionStatusRequest transactionStatusRequest) throws InvalidDataException {

        String authorization = RequestConfigServiceImpl.INSTANCE.getParameterValue(transactionStatusRequest, PARTNER_AUTHORIZATION_KEY);
        String url = RequestConfigServiceImpl.INSTANCE.getParameterValue(transactionStatusRequest, PARTNER_API_URL);
        String coutryCode = RequestConfigServiceImpl.INSTANCE.getParameterValue(transactionStatusRequest, COUNTRY_CODE_KEY);
        return getParametersMap(authorization, url, coutryCode);
    }

    /**
     * Buid a map with all needed parameters for HTTP calls
     *
     * @param paymentRequest Payline PaymentRequest
     * @return the ParametersMap
     */
    public static Map<String, String> getParametersMap(PaymentRequest paymentRequest) throws InvalidDataException {

        String authorization = RequestConfigServiceImpl.INSTANCE.getParameterValue(paymentRequest, PARTNER_AUTHORIZATION_KEY);
        String url = RequestConfigServiceImpl.INSTANCE.getParameterValue(paymentRequest, PARTNER_API_URL);
        String coutryCode = RequestConfigServiceImpl.INSTANCE.getParameterValue(paymentRequest, COUNTRY_CODE_KEY);
        return getParametersMap(authorization, url, coutryCode);
    }


    /**
     * Buid a map with all needed parameters for HTTP calls
     *
     * @param authorization PARTNER_AUTHORIZATION_KEY
     * @param url           PARTNER_API_URL
     * @param coutryCode    coutryCode from ContractParameters
     * @return the ParametersMap
     */
    private static Map<String, String> getParametersMap(String authorization, String url, String coutryCode) throws InvalidDataException {

        if (coutryCode == null || coutryCode.isEmpty()) {
            throw new InvalidDataException("coutryCode is mandatory", "coutryCode");
        }

        if (authorization == null) {
            throw new InvalidDataException(PARTNER_AUTHORIZATION_KEY + " is mandatory", PARTNER_AUTHORIZATION_KEY);
        }

        if (url == null) {
            throw new InvalidDataException(PARTNER_API_URL + " is mandatory", PARTNER_API_URL);
        }


        Map<String, String> parametersMap = new HashMap<>();
        parametersMap.put(PARTNER_AUTHORIZATION_KEY, authorization);
        parametersMap.put(PARTNER_API_URL, url);
        parametersMap.put(HEADER_COUNTRY_CODE, coutryCode.toUpperCase());
        if (Boolean.valueOf(ConfigPropertiesEnum.INSTANCE.get(CHIFFREMENT_IS_ACTIVE))) {
            parametersMap.put(SECRET_KEY, SECRET_VALUE_ON);
        } else {
            parametersMap.put(SECRET_KEY, SECRET_VALUE_OFF);
        }

        return parametersMap;
    }


    public static String truncate(String value, int length) {
        if (value != null && value.length() > length) {
            value = value.substring(0, length);
        }
        return value;
    }
}