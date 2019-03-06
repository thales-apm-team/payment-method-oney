package com.payline.payment.oney.service.impl;

import com.payline.payment.oney.bean.request.OneyRefundRequest;
import com.payline.payment.oney.bean.request.OneyTransactionStatusRequest;
import com.payline.payment.oney.exception.PluginTechnicalException;
import com.payline.payment.oney.utils.OneyConfigBean;
import com.payline.payment.oney.utils.OneyConstants;
import com.payline.payment.oney.utils.http.OneyHttpClient;
import com.payline.payment.oney.utils.http.StringResponse;
import com.payline.pmapi.bean.common.FailureCause;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseFailure;
import com.payline.pmapi.bean.refund.request.RefundRequest;
import com.payline.pmapi.bean.refund.response.RefundResponse;
import com.payline.pmapi.bean.refund.response.impl.RefundResponseFailure;
import com.payline.pmapi.bean.refund.response.impl.RefundResponseSuccess;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static com.payline.payment.oney.utils.TestUtils.createDefaultRefundRequest;
import static com.payline.payment.oney.utils.TestUtils.createStringResponse;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RefundServiceImplTest extends OneyConfigBean {

    @InjectMocks
    public RefundServiceImpl service;

    @Spy
    OneyHttpClient httpClient;

    @BeforeEach
    public void setup() {
        service = new RefundServiceImpl();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void refundRequestTestOK() throws Exception {
        StringResponse responseMocked1 = createStringResponse(200, "OK", "{\"encrypted_message\":\"+l2i0o7hGRh+wJO02++ulzsMg0QfZ1N009CwI1PLZzBnbfv6/Enufe5TriN1gKQkEmbMYU0PMtHdk+eF7boW/lsIc5PmjpFX1E/4MUJGkzI=\"}");
        Mockito.doReturn(responseMocked1).when(httpClient).doGet(Mockito.anyString(), Mockito.anyMap(), Mockito.anyMap());

        StringResponse responseMocked = createStringResponse(200, "OK", "{\"encrypted_message\":\"+l2i0o7hGRh+wJO02++ul+pupX40ZlQGwcgL91laJl8Vmw5MnvB6zm+cpQviUjey0a4YEoiRButKTLyhHS8SBlDyClrx8GM0AWSp0+DsthbblWPrSSH9+6Oj0h25FWyQ\"}");
        Mockito.doReturn(responseMocked).when(httpClient).doPost(Mockito.anyString(), Mockito.anyString(), Mockito.anyMap());

        RefundResponse response = service.refundRequest(createDefaultRefundRequest());

        if (response.getClass() == RefundResponseSuccess.class) {
            RefundResponseSuccess success = (RefundResponseSuccess) response;
            Assertions.assertEquals("CANCELLED", success.getStatusCode());
            Assertions.assertNotNull(success.getPartnerTransactionId());
        }

    }


    @Test
    public void refundRequestTestKO() throws Exception {
        StringResponse responseMocked1 = createStringResponse(200, "OK", "{\"encrypted_message\":\"+l2i0o7hGRh+wJO02++ulzsMg0QfZ1N009CwI1PLZzBnbfv6/Enufe5TriN1gKQkEmbMYU0PMtHdk+eF7boW/lsIc5PmjpFX1E/4MUJGkzI=\"}");
        Mockito.doReturn(responseMocked1).when(httpClient).doGet(Mockito.anyString(), Mockito.anyMap(), Mockito.anyMap());

        StringResponse responseMocked = createStringResponse(200, "OK", "{\"encrypted_message\":\"ymDHJ7HBRe49whKjH1HDtA==\"}");
        Mockito.doReturn(responseMocked).when(httpClient).doPost(Mockito.anyString(), Mockito.anyString(), Mockito.anyMap());

        RefundRequest refundReq = createDefaultRefundRequest();
        RefundResponse response = service.refundRequest(refundReq);

        Assertions.assertSame(response.getClass(), RefundResponseFailure.class);
        RefundResponseFailure fail = (RefundResponseFailure) response;
        Assertions.assertEquals(FailureCause.REFUSED, fail.getFailureCause());
        Assertions.assertEquals(OneyConstants.EXTERNAL_REFERENCE_TYPE + OneyConstants.PIPE + refundReq.getOrder().getReference(), fail.getPartnerTransactionId());
    }

    @Test
    public void handleStatusRequestEncrypted() throws Exception {
        StringResponse responseMocked1 = createStringResponse(200, "OK", "{\"encrypted_message\":\"+l2i0o7hGRh+wJO02++ulzsMg0QfZ1N009CwI1PLZzBnbfv6/Enufe5TriN1gKQkEmbMYU0PMtHdk+eF7boW/lsIc5PmjpFX1E/4MUJGkzI=\"}");
        Mockito.doReturn(responseMocked1).when(httpClient).doGet(Mockito.anyString(), Mockito.anyMap(), Mockito.anyMap());
        RefundRequest refundReq = createDefaultRefundRequest();
        mockCorrectlyConfigPropertiesEnum(true);
        String status = service.handleStatusRequest(refundReq);

        Assertions.assertEquals("FUNDED", status);
    }

    @Test
    public void handleStatusRequestNotEncrypted() throws Exception {
        StringResponse responseMocked1 = createStringResponse(200, "OK", "{\"purchase\":{\"status_code\":\"FUNDED\",\"status_label\":\"Transaction is completed\"}}");
        Mockito.doReturn(responseMocked1).when(httpClient).doGet(Mockito.anyString(), Mockito.anyMap(), Mockito.anyMap());
        RefundRequest refundReq = createDefaultRefundRequest();
        mockCorrectlyConfigPropertiesEnum(false);
        String status = service.handleStatusRequest(refundReq);

        Assertions.assertEquals("FUNDED", status);
    }


    @Test
    public void getRefundFlagTrue() {
        String status = "FUNDED";
        boolean flag = service.getRefundFlag(status);
        Assertions.assertTrue(flag);

    }

    @Test
    public void getRefundFlagFalse() {
        String status = "FAVORABLE";
        String status2 = "PENDING";
        boolean flag = service.getRefundFlag(status);
        boolean flag2 = service.getRefundFlag(status2);

        Assertions.assertFalse(flag);
        Assertions.assertFalse(flag2);

    }

    @Test
    public void getRefundFlagInvalid() {

        String status = "XOXO";
        Assertions.assertFalse(service.getRefundFlag(status));
    }

    @Test
    public void getRefundFlagNotRefundable() {

        String status = "REFUSED";
        Assertions.assertFalse(service.getRefundFlag(status));
    }

    @Test
    public void getRefundFlagNotRefundable2() {

        String status = "ABORTED";
        Assertions.assertFalse(service.getRefundFlag(status));
    }

    @Test
    public void getRefundFlagNotRefundable3() {

        String status = "CANCELLED";
        Assertions.assertFalse(service.getRefundFlag(status));
    }

    @Test
    public void getRefundFlagNotRefundable4() {

        String status = null;
        Assertions.assertFalse(service.getRefundFlag(status));
    }

    @Test
    public void refundRequest_malformedStatusResponseKO() throws PluginTechnicalException {
        // given a malformed HTTP response received to the status request
        StringResponse responseMocked = createStringResponse(404, "Bad Request", "[]");
        Mockito.doReturn(responseMocked).when(httpClient).initiateGetTransactionStatus( Mockito.any(OneyTransactionStatusRequest.class) );

        // when calling the method refundRequest
        RefundResponse response = service.refundRequest( createDefaultRefundRequest() );

        // then a RefundResponseFailure with the FailureCause.COMMUNICATION_ERROR is returned
        Assertions.assertTrue( response instanceof RefundResponseFailure);
        Assertions.assertEquals( FailureCause.COMMUNICATION_ERROR, ((RefundResponseFailure)response).getFailureCause() );
    }

    @Test
    public void refundRequest_malformedStatusResponseOK() throws PluginTechnicalException {
        // given a malformed HTTP response received to the status request
        StringResponse responseMocked = createStringResponse(200, "OK", "[]");
        Mockito.doReturn(responseMocked).when(httpClient).initiateGetTransactionStatus( Mockito.any(OneyTransactionStatusRequest.class) );

        // when calling the method refundRequest
        RefundResponse response = service.refundRequest( createDefaultRefundRequest() );

        // then a RefundResponseFailure with the FailureCause.COMMUNICATION_ERROR is returned
        Assertions.assertTrue( response instanceof RefundResponseFailure);
        Assertions.assertEquals( FailureCause.COMMUNICATION_ERROR, ((RefundResponseFailure)response).getFailureCause() );
    }

    @Test
    public void refundRequest_malformedRefundResponseKO() throws PluginTechnicalException {
        // given a malformed HTTP response received to the refund/cancel request
        StringResponse responseMocked = createStringResponse(404, "Bad Request", "[]");
        Mockito.doReturn(responseMocked).when(httpClient).initiateRefundPayment( Mockito.any(OneyRefundRequest.class) );

        // when calling the method refundRequest
        RefundResponse response = service.refundRequest( createDefaultRefundRequest() );

        // then a RefundResponseFailure with the FailureCause.COMMUNICATION_ERROR is returned
        Assertions.assertTrue( response instanceof RefundResponseFailure);
        Assertions.assertEquals( FailureCause.COMMUNICATION_ERROR, ((RefundResponseFailure)response).getFailureCause() );
    }

    @Test
    public void refundRequest_malformedRefundResponseOK() throws PluginTechnicalException {
        // given a malformed HTTP response received to the refund/cancel request
        StringResponse responseMocked = createStringResponse(200, "OK", "[]");
        Mockito.doReturn(responseMocked).when(httpClient).initiateRefundPayment( Mockito.any(OneyRefundRequest.class) );

        // when calling the method refundRequest
        RefundResponse response = service.refundRequest( createDefaultRefundRequest() );

        // then a RefundResponseFailure with the FailureCause.COMMUNICATION_ERROR is returned
        Assertions.assertTrue( response instanceof RefundResponseFailure);
        Assertions.assertEquals( FailureCause.COMMUNICATION_ERROR, ((RefundResponseFailure)response).getFailureCause() );
    }
}
