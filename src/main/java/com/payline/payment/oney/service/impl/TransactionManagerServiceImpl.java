package com.payline.payment.oney.service.impl;

import com.payline.pmapi.service.TransactionManagerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class TransactionManagerServiceImpl  implements TransactionManagerService {

    private static final Logger LOGGER = LogManager.getLogger(TransactionManagerServiceImpl.class);

    @Override
    public Map<String, String> readAdditionalData(String s, String s1) {
        //todo definir donnees a renvoyer
        return null;
    }
}
