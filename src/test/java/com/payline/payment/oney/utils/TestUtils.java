package com.payline.payment.oney.utils;

import com.payline.payment.oney.utils.http.StringResponse;
import com.payline.pmapi.bean.common.Amount;
import com.payline.pmapi.bean.common.Buyer;
import com.payline.pmapi.bean.common.Buyer.Address;
import com.payline.pmapi.bean.configuration.PartnerConfiguration;
import com.payline.pmapi.bean.configuration.request.ContractParametersCheckRequest;
import com.payline.pmapi.bean.payment.*;
import com.payline.pmapi.bean.payment.request.PaymentRequest;
import com.payline.pmapi.bean.payment.request.RedirectionPaymentRequest;
import com.payline.pmapi.bean.paymentform.request.PaymentFormConfigurationRequest;
import com.payline.pmapi.bean.refund.request.RefundRequest;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static com.payline.payment.oney.utils.OneyConstants.*;


/**
 * Class with method to generate mock easier
 */
public class TestUtils {

    private static final String SUCCESS_URL = "https://succesurl.com/";
    private static final String CANCEL_URL = "http://localhost/cancelurl.com/";
    private static final String NOTIFICATION_URL = "http://google.com/";

    public static String PHONE_NUMBER_TEST = "06060606";
    public HashMap<String, String> extendedData;


    /**
     * Create a paymentRequest with default parameters.
     *
     * @return paymentRequest created
     */
    public static PaymentRequest createDefaultPaymentRequest() {
        final Amount amount = createAmount("EUR");
        final ContractConfiguration contractConfiguration = createContractConfiguration();
        final Environment paylineEnvironment = new Environment(NOTIFICATION_URL, SUCCESS_URL, CANCEL_URL, true);
        final String transactionID = "transactionID";
        final Order order = createOrder(transactionID);
        final String softDescriptor = "softDescriptor";


        return PaymentRequest.builder()
                .withAmount(amount)
                .withBrowser(new Browser("", Locale.FRANCE))
                .withLocale(Locale.FRANCE)
                .withContractConfiguration(contractConfiguration)
                .withEnvironment(paylineEnvironment)
                .withOrder(order)
                .withBuyer(createDefaultBuyer())
                .withTransactionId(transactionID)
                .withSoftDescriptor(softDescriptor)
                .withEnvironment(createDefaultEnvironment())
                .withPartnerConfiguration(createDefaultPartnerConfiguration())
                .withSoftDescriptor(softDescriptor)
                .build();
    }


    /**
     * Create a default form context for Unit Test and IT Test
     *
     * @return PaymentFormContext which contain a mobile phone number and a iban
     */
    public static PaymentFormContext createDefaultPaymentFormContext(String phoneNumber) {
        Map<String, String> paymentFormParameter = new HashMap<>();
        paymentFormParameter.put(PSP_GUID_KEY,"Psp_id_test");

        Map<String, String> sensitivePaymentFormParameter = new HashMap<>();
        sensitivePaymentFormParameter.put(SECRET_KEY,"x-oney-secret-Value");

        return PaymentFormContext.PaymentFormContextBuilder
                .aPaymentFormContext()
                .withPaymentFormParameter(paymentFormParameter)
                .withSensitivePaymentFormParameter(sensitivePaymentFormParameter)
                .build();

    }


    public static PaymentFormContext createCustomPaymentFormContext(Map<String, String> customPaymentFormParameter, Map<String, String> customSensitivePaymentFormParameter) {
        //Ajout du numero de telephone et IBAN a la requête
        return PaymentFormContext.PaymentFormContextBuilder
                .aPaymentFormContext()
                .withPaymentFormParameter(customPaymentFormParameter)
                .withSensitivePaymentFormParameter(customSensitivePaymentFormParameter)
                .build();

    }


    public static RefundRequest createRefundRequest(String transactionId) {
        final Environment paylineEnvironment = new Environment(NOTIFICATION_URL, SUCCESS_URL, CANCEL_URL, true);
//       final String transactionID = createTransactionId();
        final Amount amount = createAmount("EUR");
        final Map<String, String> partnerConfiguration = new HashMap<>();
        final Map<String, String> sensitiveConfig = new HashMap<>();
        return RefundRequest.RefundRequestBuilder.aRefundRequest()
                .withAmount(amount)
                .withOrder(createOrder(transactionId, amount))
                .withBuyer(createDefaultBuyer())
                .withContractConfiguration(createContractConfiguration())
                .withEnvironment(paylineEnvironment)
                .withTransactionId(transactionId)
                .withPartnerTransactionId("toto")
                .withPartnerConfiguration(new PartnerConfiguration(partnerConfiguration, sensitiveConfig))
                .build();
    }

    public static RedirectionPaymentRequest createRedirectionPaymentRequest() {
        RedirectionPaymentRequest request = RedirectionPaymentRequest.builder().build();


        return request;
    }

    /**
     * Create a complete payment request used for Integration Tests
     *
     * @return PaymentRequest.Builder
     */

    public static PaymentRequest.Builder createCompletePaymentBuilder() {
        final Amount amount = createAmount("EUR");
        final ContractConfiguration contractConfiguration = createContractConfiguration();

        final Environment paylineEnvironment = new Environment(NOTIFICATION_URL, SUCCESS_URL, CANCEL_URL, true);
        final String transactionID = createTransactionId();
        final Order order = createOrder(transactionID);
        final String softDescriptor = "softDescriptor";
        final Locale locale = new Locale("FR");

        return PaymentRequest.builder()
                .withAmount(amount)
                .withBrowser(new Browser("", Locale.FRANCE))
                .withContractConfiguration(contractConfiguration)
                .withEnvironment(paylineEnvironment)
                .withOrder(order)
                .withLocale(locale)
                .withTransactionId(transactionID)
                .withSoftDescriptor(softDescriptor)
                .withPaymentFormContext(createDefaultPaymentFormContext(PHONE_NUMBER_TEST))
                .withPartnerConfiguration(createDefaultPartnerConfiguration())
                .withLocale(Locale.FRANCE)
                .withBuyer(createDefaultBuyer());
    }

    //Cree une redirection payment par defaut
    public static RedirectionPaymentRequest.Builder createCompleteRedirectionPaymentBuilder() {
        final Amount amount = createAmount("EUR");
        final ContractConfiguration contractConfiguration = createContractConfiguration();

        final Environment paylineEnvironment = new Environment(NOTIFICATION_URL, SUCCESS_URL, CANCEL_URL, true);
        final String transactionID = createTransactionId();
        final Order order = createOrder(transactionID);
        final String softDescriptor = "softDescriptor";
        final Locale locale = new Locale("FR");

        Map<String, String> requestData = new HashMap<>();
        requestData.put(PSP_GUID_KEY,"6ba2a5e2-df17-4ad7-8406-6a9fc488a60a");
        requestData.put(SECRET_KEY,"Method-body");
        requestData.put(EXTERNAL_REFERENCE_KEY, "mareference");


        final RequestContext requestContext = RequestContext.RequestContextBuilder
                .aRequestContext()
                .withRequestData(requestData)
                .build();
        return (RedirectionPaymentRequest.Builder) RedirectionPaymentRequest.builder()
                .withAmount(amount)
                .withBrowser(new Browser("", Locale.FRANCE))
                .withContractConfiguration(contractConfiguration)
                .withEnvironment(paylineEnvironment)
                .withOrder(order)
                .withLocale(locale)
                .withTransactionId(transactionID)
                .withSoftDescriptor(softDescriptor)
                .withPaymentFormContext(createDefaultPaymentFormContext(PHONE_NUMBER_TEST))
                .withPartnerConfiguration(createDefaultPartnerConfiguration())
                .withLocale(Locale.FRANCE)
                .withBuyer(createDefaultBuyer())
                //propre a la redirectionPayment
//                .withPaymentFormContext()
                .withRequestContext(requestContext)
                ;
    }


    public static String createTransactionId() {
        return "transactionID" + Calendar.getInstance().getTimeInMillis();
    }

    public static String createsignatureId() {
        return "signatureID" + Calendar.getInstance().getTimeInMillis();
    }


    public static Map<Buyer.AddressType, Address> createAddresses(Address address) {
        Map<Buyer.AddressType, Address> addresses = new HashMap<>();
        addresses.put(Buyer.AddressType.DELIVERY, address);
        addresses.put(Buyer.AddressType.BILLING, address);

        return addresses;
    }

    public static Map<Buyer.AddressType, Address> createDefaultAddresses() {
        Address address = createDefaultCompleteAddress();
        return createAddresses(address);
    }

    public static Amount createAmount(String currency) {
        return new Amount(BigInteger.TEN, Currency.getInstance(currency));
    }

    public static Order createOrder(String transactionID) {
        List<Order.OrderItem> orderItems = new ArrayList<>();
        orderItems.add(createOrderItem("item1", createAmount("EUR")));
        orderItems.add(createOrderItem("item2", createAmount("EUR")));
        return Order.OrderBuilder.anOrder()
                .withReference(transactionID)
                .withAmount(createAmount("EUR"))
                .withDate(new Date())
                .withItems(orderItems)
                .build();
    }

    public static Order.OrderItem createOrderItem(String reference, Amount amount) {
        return Order.OrderItem.OrderItemBuilder.anOrderItem()
                .withAmount(amount)
                .withQuantity(4L)
                .withCategory(Order.OrderItemCategory.THINGS)
                .withComment("some label")
                .withBrand("someBrand")
                .withReference(reference)
                .withTaxRatePercentage(BigDecimal.TEN)
                .build();
    }

    public static Order createOrder(String transactionID, Amount amount) {
        return Order.OrderBuilder.anOrder().withReference(transactionID).withAmount(amount).build();
    }


    public static Buyer.FullName createFullName() {
        return new Buyer.FullName("foo", "bar", Buyer.Civility.MR);
    }

    public static Map<Buyer.PhoneNumberType, String> createDefaultPhoneNumbers() {
        Map<Buyer.PhoneNumberType, String> phoneNumbers = new HashMap<>();
        phoneNumbers.put(Buyer.PhoneNumberType.BILLING, "+33606060606");
        phoneNumbers.put(Buyer.PhoneNumberType.CELLULAR, "+33707070707");
        phoneNumbers.put(Buyer.PhoneNumberType.HOME, "+33708070708");
        phoneNumbers.put(Buyer.PhoneNumberType.UNDEFINED, "+33708070708");

        return phoneNumbers;
    }

    public static ContractConfiguration createContractConfiguration() {
        final ContractConfiguration contractConfiguration = new ContractConfiguration("Oney", new HashMap<>());
        contractConfiguration.getContractProperties().put(BUSINESS_TRANSACTION_CODE, new ContractProperty("3x002"));
        contractConfiguration.getContractProperties().put(MERCHANT_GUID_KEY, new ContractProperty("9813e3ff-c365-43f2-8dca-94b850befbf9"));
        contractConfiguration.getContractProperties().put(PSP_GUID_KEY, new ContractProperty("6ba2a5e2-df17-4ad7-8406-6a9fc488a60a"));
        contractConfiguration.getContractProperties().put(API_MARKETING_KEY, new ContractProperty("01c6ea9021574d608c631f1c3b880b3be"));
        contractConfiguration.getContractProperties().put(OPC_KEY, new ContractProperty("OPC"));
        contractConfiguration.getContractProperties().put(NB_ECHEANCES_KEY, new ContractProperty("2"));
        contractConfiguration.getContractProperties().put(COUNTRY_CODE_KEY, new ContractProperty("FRA")); //3 caractères
        contractConfiguration.getContractProperties().put(LANGUAGE_CODE_KEY, new ContractProperty("FR"));
        contractConfiguration.getContractProperties().put(ID_INTERNATIONAL_KEY, new ContractProperty("FR"));
//used for Confirm

        return contractConfiguration;
    }

    public static Map<String, String> createAccountInfo() {
        Map<String, String> accountInfo = new HashMap<>();
//        accountInfo.put(CONTRACT_CONFIG_CREDITOR_ID, GOOD_CREDITOR_ID);
//        accountInfo.put(PARTNER_CONFIG_AUTH_LOGIN, GOOD_LOGIN);
//        accountInfo.put(PARTNER_CONFIG_AUTH_PASS, GOOD_PWD);
        return accountInfo;
    }

    public static ContractParametersCheckRequest createContractParametersCheckRequest() {

        final ContractParametersCheckRequest contractParametersCR = ContractParametersCheckRequest
                .CheckRequestBuilder
                .aCheckRequest()
                .withContractConfiguration(createContractConfiguration())
                .withAccountInfo(createAccountInfo())
                .withEnvironment(createDefaultEnvironment())
                .withLocale(Locale.FRANCE)
                .withPartnerConfiguration(createDefaultPartnerConfiguration())
                .build();

        return contractParametersCR;
    }


    public static Address createAddress(String street, String city, String zip) {
        return Address.AddressBuilder.anAddress()
                .withStreet1(street)
                .withCity(city)
                .withZipCode(zip)
                .withCountry("FR")
                .build();
    }


    public static Address createCompleteAddress(String street, String street2, String city, String zip, String country) {
        return Address.AddressBuilder.anAddress()
                .withStreet1(street)
                .withStreet2(street2)
                .withCity(city)
                .withZipCode(zip)
                .withCountry(country)
                .withFullName(createFullName())
                .build();
    }

    public static Address createDefaultAddress() {
        return createAddress("12 a street", "Paris", "75000");
    }

    public static Address createDefaultCompleteAddress() {
        return createCompleteAddress("12 rue Paradis", "residence azerty", "Paris", "75001", "FR");
    }

    public static Buyer createBuyer(Map<Buyer.PhoneNumberType, String> phoneNumbers, Map<Buyer.AddressType, Address> addresses, Buyer.FullName fullName) {
        return Buyer.BuyerBuilder.aBuyer()
                .withEmail("testdocapost@yopmail.com")
                .withPhoneNumbers(phoneNumbers)
                .withAddresses(addresses)
                .withFullName(fullName)
                .withCustomerIdentifier("subscriber1")
                .withExtendedData(createDefaultExtendedData())
                .withBirthday(new Date(1991, 1, 1))
                .withLegalStatus(Buyer.LegalStatus.PERSON)
                .build();
    }


    public static Map<String, String> createDefaultExtendedData() {

        HashMap<String, String> extData = new HashMap<>();
        extData.put("bic", "TESTFRP1");
        extData.put("iban", "FR7630076020821234567890186");
        extData.put("country", "FR");
        extData.put("institutionName", "Bank Test");
        return extData;

    }

    public static Buyer createDefaultBuyer() {
        return createBuyer(createDefaultPhoneNumbers(), createDefaultAddresses(), createFullName());
    }


    public static Environment createDefaultEnvironment() {
        return new Environment("http://notificationURL.com", "http://redirectionURL.com", "http://redirectionCancelURL.com", true);
    }

    public static PartnerConfiguration createDefaultPartnerConfiguration() {
        Map<String, String> partnerConfiguration = new HashMap<>();
        partnerConfiguration.put(PSP_GUID_KEY,"6ba2a5e2-df17-4ad7-8406-6a9fc488a60a");
        partnerConfiguration.put(SECRET_KEY,"Method-body");

        Map<String, String> sensitivePartnerConfiguration = new HashMap<>();


        return new PartnerConfiguration(partnerConfiguration, sensitivePartnerConfiguration);
    }

    public static PaymentFormConfigurationRequest createDefaultPaymentFormConfigurationRequest() {
        return PaymentFormConfigurationRequest.PaymentFormConfigurationRequestBuilder.aPaymentFormConfigurationRequest()
                .withLocale(Locale.FRANCE)
                .withBuyer(createDefaultBuyer())
                .withAmount(new Amount(null, Currency.getInstance("EUR")))
                .withContractConfiguration(createContractConfiguration())
                .withOrder(createOrder("007"))
                .withEnvironment(createDefaultEnvironment())
                .withPartnerConfiguration(createDefaultPartnerConfiguration())

                .build();
    }

    public static StringResponse createStringResponse(int code, String message, String content) {
        StringResponse response = new StringResponse();
        response.setCode(code);
        response.setMessage(message);
        response.setContent(content);
        return response;
    }


}
