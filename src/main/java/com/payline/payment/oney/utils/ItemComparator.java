package com.payline.payment.oney.utils;

import com.payline.payment.oney.bean.common.purchase.Item;

import java.util.Comparator;

public class ItemComparator implements Comparator<Item> {

    @Override
    public int compare(Item item1, Item item2) {
        return item1.getPrice().compareTo(item2.getPrice());
    }
}
