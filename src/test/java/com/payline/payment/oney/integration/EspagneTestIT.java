package com.payline.payment.oney.integration;

import com.payline.payment.oney.utils.TestCountry;
import com.payline.pmapi.bean.payment.ContractProperty;
import com.payline.pmapi.bean.payment.request.PaymentRequest;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import static com.payline.payment.oney.utils.OneyConstants.*;

public class EspagneTestIT extends TestIT {

    public static final String TEST_PSP_GUID_KEY = PSP_GUID_KEY + ".es";

    public static final String TEST_PARTNER_AUTHORIZATION_KEY = PARTNER_AUTHORIZATION_KEY + ".es";

    private HashMap<String, ContractProperty> contractConfiguration;

    @Override
    public TestCountry getCountry() {
        return TestCountry.ES;
    }

    @Override
    public Map.Entry<String, String> getPspGuid() {
        return new AbstractMap.SimpleEntry<>(TEST_PSP_GUID_KEY, "6fbba0dc-ea82-45dc-a700-fcba4b7a793f");
    }

    @Override
    public Map.Entry<String, String> getPartnerAuthorizationKey() {
        return new AbstractMap.SimpleEntry<>(TEST_PARTNER_AUTHORIZATION_KEY, "99cf041050704802bbfacbe4be48e58a");
    }

    @Override
    protected Map<String, ContractProperty> generateParameterContract() {
        if (contractConfiguration != null) {
            return contractConfiguration;
        }
        contractConfiguration = new HashMap();
        contractConfiguration.put(OPC_KEY, new ContractProperty("3x002"));
        contractConfiguration.put(MERCHANT_GUID_KEY, new ContractProperty("1e0ea3e2-4d7f-4bce-b846-62dc7318ad8f"));
        contractConfiguration.put(NB_ECHEANCES_KEY, new ContractProperty("3"));
        contractConfiguration.put(COUNTRY_CODE_KEY, new ContractProperty("ES")); // caractères
        contractConfiguration.put(LANGUAGE_CODE_KEY, new ContractProperty("es"));
        contractConfiguration.put(PARTNER_CHIFFREMENT_KEY, new ContractProperty("cHuAfKnyp1iiejmp3KEtWIQkokpnoLUrEamkM29xtqA="));
        return contractConfiguration;
    }

    @Test
    public void fullPaymentTest() {
        PaymentRequest request = createDefaultPaymentRequest();
        this.fullRedirectionPayment(request, paymentService, paymentWithRedirectionService);

    }
}
