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
        Assert.assertEquals("2.249.3", updateCenter.getUpdateVersionToUse("2.249.3"));
        Assert.assertEquals("2.249.3", updateCenter.getUpdateVersionToUse("2.250.0"));
        Assert.assertEquals("2.263.4", updateCenter.getUpdateVersionToUse("2.276.0"));
        Assert.assertEquals("2.289.3", updateCenter.getUpdateVersionToUse("2.289.9999"));
        Assert.assertEquals("2.303.1", updateCenter.getUpdateVersionToUse("2.304.0"));
    }
}