package com.payline.payment.oney.utils;

import com.payline.payment.oney.utils.properties.service.ConfigPropertiesEnum;
import mockit.Mock;
import mockit.MockUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import static com.payline.payment.oney.utils.OneyConstants.CHIFFREMENT_IS_ACTIVE;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OneyConfigBean {


    private MockUp<ConfigPropertiesEnum> configPropertiesEnumMockUp;

    @BeforeAll
    public void setup() {
    }

    /**
     * Mock de ConfigPropertiesEnum avec retour de la valeur désirée pour CHIFFREMENT_IS_ACTIVE
     *
     * @param s boolean : chiffrement activé ?
     */
    protected void mockCorrectlyConfigPropertiesEnum(final boolean s) {
        if (configPropertiesEnumMockUp != null) {
            configPropertiesEnumMockUp.tearDown();
        }

        configPropertiesEnumMockUp = new MockUp<ConfigPropertiesEnum>() {
            @Mock
            public String get(String key) {
                if (CHIFFREMENT_IS_ACTIVE.equals(key)) {
                    return String.valueOf(s);
                } else {
                    return ConfigPropertiesEnum.INSTANCE.get(key);
                }
            }
        };
    }

    @AfterEach
    public void tearDown() {
        if (configPropertiesEnumMockUp != null) {
            configPropertiesEnumMockUp.tearDown();
        }
    }

}
