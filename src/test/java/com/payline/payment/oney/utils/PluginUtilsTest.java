package com.payline.payment.oney.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

import static com.payline.payment.oney.utils.PluginUtils.truncateLongText;

public class PluginUtilsTest {


    @Test
    public void testTruncateText() {
        String longText = "Update: yes it doesn't take the slim characters into account but I don't agree with that considering everyone has different screens and fonts setup and a large portion of the people that land here on this page are probably looking for a maintained library like the above.";
        String longText2 = "Update:";
        Map textCutted = truncateLongText( longText,  longText2, 19) ;
        Assert.assertTrue(textCutted.get("line1").toString().length()<20);
        Assert.assertTrue(textCutted.get("line2")==null||textCutted.get("line2").toString().length()<20);
        Assert.assertTrue(textCutted.get("line3")==null||textCutted.get("line3").toString().length()<20);
        Assert.assertTrue(textCutted.get("line4")==null ||textCutted.get("line4").toString().length()<20);
        Assert.assertTrue(textCutted.get("line5")==null ||textCutted.get("line5").toString().length()<20);

    }
}
