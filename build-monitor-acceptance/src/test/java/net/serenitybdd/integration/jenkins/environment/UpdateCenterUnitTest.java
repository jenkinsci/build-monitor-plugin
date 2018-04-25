package net.serenitybdd.integration.jenkins.environment;

import org.junit.Assert;
import org.junit.Test;

public class UpdateCenterUnitTest {

    private UpdateCenter updateCenter = new UpdateCenter();

    @Test(expected = RuntimeException.class)
    public void getUpdateVersionToUseTooLow() {
        String versionToUse = updateCenter.getUpdateVersionToUse("2.45");
    }

    @Test
    public void getUpdateVersionToUse() {
        Assert.assertEquals("2.46", updateCenter.getUpdateVersionToUse("2.46.0"));
        Assert.assertEquals("2.46", updateCenter.getUpdateVersionToUse("2.47.0"));
        Assert.assertEquals("2.60", updateCenter.getUpdateVersionToUse("2.72.0"));
        Assert.assertEquals("2.73", updateCenter.getUpdateVersionToUse("2.88.0"));
        Assert.assertEquals("2.89", updateCenter.getUpdateVersionToUse("2.106.9999"));
        Assert.assertEquals("2.107", updateCenter.getUpdateVersionToUse("2.108.0"));
    }
}