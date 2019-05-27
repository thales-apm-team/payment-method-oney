package com.payline.payment.oney.service.impl;

import com.payline.payment.oney.utils.OneyConfigBean;
import com.payline.payment.oney.utils.TestUtils;
import com.payline.payment.oney.utils.http.OneyHttpClient;
import com.payline.payment.oney.utils.http.StringResponse;
import com.payline.pmapi.bean.common.FailureTransactionStatus;
import com.payline.pmapi.bean.common.SuccessTransactionStatus;
import com.payline.pmapi.bean.notification.request.NotificationRequest;
import com.payline.pmapi.bean.notification.response.NotificationResponse;
import com.payline.pmapi.bean.notification.response.impl.IgnoreNotificationResponse;
import com.payline.pmapi.bean.notification.response.impl.TransactionStateChangedResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.stream.Stream;

import static com.payline.payment.oney.utils.TestUtils.createStringResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NotificationServiceTest extends OneyConfigBean {

    @InjectMocks
    NotificationServiceImpl service;

    @Spy
    OneyHttpClient client;

    @BeforeAll
    public void setup() {
        service = new NotificationServiceImpl();
        MockitoAnnotations.initMocks(this);

    }

    private static Stream<Arguments> parseSet() {
        return Stream.of(
                Arguments.of("FUNDED", SuccessTransactionStatus.class),
                Arguments.of("FAVORABLE", SuccessTransactionStatus.class),
                Arguments.of("REFUSED", FailureTransactionStatus.class),
                Arguments.of("ABORTED", FailureTransactionStatus.class)
        );
    }

    @ParameterizedTest
    @MethodSource("parseSet")
    void parse(String OneyStatus, Class expectedClass) throws Exception {
        String content = "{" +
                "  \"language_code\": \"FR\"," +
                "  \"merchant_guid\": \"anId\"," +
                "  \"oney_request_id\": \"123456789\"," +
                "  \"purchase\": {" +
                "    \"external_reference_type\": \"aType\"," +
                "    \"external_reference\": \"987654321\"," +
                "    \"purchase_merchant\": {" +
                "      \"merchant_guid\": \"azerty\"" +
                "    }," +
                "    \"status_code\": \"0000\"," +
                "    \"status_label\": \"XXXXXX\"," +
                "    \"reason_code\": \"aReason\"," +
                "    \"reason_label\": \"aLabel\"" +
                "  }," +
                "  \"customer\": {" +
                "    \"customer_external_code\": \"aCode\"" +
                "  }," +
                "  \"merchant_context\": \"CN!1000!EUR\"," +
                "  \"psp_context\": \"1234\"" +
                "}";
        content = content.replace("XXXXXX", OneyStatus);

        NotificationRequest request = NotificationRequest.NotificationRequestBuilder.aNotificationRequest()
                .withHeaderInfos(new HashMap<>())
                .withPathInfo("thisIsAPath")
                .withHttpMethod("POST")
                .withContent(new ByteArrayInputStream(content.getBytes()))
                .withContractConfiguration(TestUtils.createContractConfiguration())
                .withPartnerConfiguration(TestUtils.createDefaultPartnerConfiguration())
                .build();

        StringResponse responseMockedConfirm = createStringResponse(200, "OK", "{\"purchase\":{\"status_code\":\"FUNDED\",\"status_label\":\"a label\"}}");
        Mockito.doReturn(responseMockedConfirm).when(client).initiateConfirmationPayment(any(), anyBoolean());

        NotificationResponse response = service.parse(request);
        TransactionStateChangedResponse transactionStateChangedResponse = (TransactionStateChangedResponse) response;

        Assertions.assertEquals(expectedClass ,transactionStateChangedResponse.getTransactionStatus().getClass());
    }

    @Test
    void parseOther(){
        String content = "{" +
                "  \"language_code\": \"FR\"," +
                "  \"merchant_guid\": \"anId\"," +
                "  \"oney_request_id\": \"123456789\"," +
                "  \"purchase\": {" +
                "    \"external_reference_type\": \"aType\"," +
                "    \"external_reference\": \"987654321\"," +
                "    \"purchase_merchant\": {" +
                "      \"merchant_guid\": \"azerty\"" +
                "    }," +
                "    \"status_code\": \"0000\"," +
                "    \"status_label\": \"XXXXXX\"," +
                "    \"reason_code\": \"aReason\"," +
                "    \"reason_label\": \"aLabel\"" +
                "  }," +
                "  \"customer\": {" +
                "    \"customer_external_code\": \"aCode\"" +
                "  }," +
                "  \"merchant_context\": \"aContext\"," +
                "  \"psp_context\": \"1234\"" +
                "}";
        content = content.replace("XXXXXX", "ANYTHING");
        NotificationRequest request = NotificationRequest.NotificationRequestBuilder.aNotificationRequest()
                .withHeaderInfos(new HashMap<>())
                .withPathInfo("thisIsAPath")
                .withHttpMethod("POST")
                .withContent(new ByteArrayInputStream(content.getBytes()))
                .withContractConfiguration(TestUtils.createContractConfiguration())
                .withPartnerConfiguration(TestUtils.createDefaultPartnerConfiguration())
                .build();

        NotificationResponse response = service.parse(request);
        Assertions.assertEquals(IgnoreNotificationResponse.class, response.getClass());
    }


    @Test
    void parseException(){
        NotificationRequest request = NotificationRequest.NotificationRequestBuilder.aNotificationRequest()
                .withHeaderInfos(new HashMap<>())
                .withPathInfo("thisIsAPath")
                .withHttpMethod("POST")
                .withContent(new ByteArrayInputStream("foo".getBytes()))
                .withContractConfiguration(TestUtils.createContractConfiguration())
                .withPartnerConfiguration(TestUtils.createDefaultPartnerConfiguration())
                .build();

        NotificationResponse response = service.parse(request);
        TransactionStateChangedResponse stateChangedResponse = (TransactionStateChangedResponse) response;
        Assertions.assertEquals(FailureTransactionStatus.class ,stateChangedResponse.getTransactionStatus().getClass());
    }
}
