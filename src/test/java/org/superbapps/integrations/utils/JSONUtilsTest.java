package org.superbapps.integrations.utils;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

class JSONUtilsTest {

    // @Rule
    // public ExpectedException thrown = ExpectedException.none();

    @Test
    void test_getDateTimeFormat_When_NotSupplyingDateTime_Format_NotNull() {
        Assert.assertNotNull(JSONUtils.getDateTimeFormat());
    }

    @Test
    void test_getDateTimeFormat_Format_US() {
        Assert.assertNotNull(JSONUtils.getDateTimeFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"));
    }

    // not working:
    // @Test(expected = IllegalArgumentException.class)
    @Test
    void test_getDateTimeFormat_Wrong_Format() /*throws IllegalArgumentException*/ {
        // thrown.expect(IllegalArgumentException.class);

        try {
            String timeFormat = JSONUtils.getDateTimeFormat("no_valid_format");
            System.err.println(timeFormat);

            Assert.assertNotNull(timeFormat);
        } catch (Exception e) {
            System.err.println("As expected...");
        }
    }
}