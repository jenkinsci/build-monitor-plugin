package net.serenitybdd.integration.jenkins.environment;

import org.junit.Assert;
import org.junit.Test;

public class UpdateCenterUnitTest {

    private UpdateCenter updateCenter = new UpdateCenter();

    @Test(expected = RuntimeException.class)
    public void getUpdateVersionToUseTooLow() {
        String versionToUse = updateCenter.getUpdateVersionToUse("2.280");
    }

    @Test
    public void getUpdateVersionToUse() {
        // Exact version
        Assert.assertEquals("2.289.2", updateCenter.getUpdateVersionToUse("2.289.2"));
        // Higher patch
        Assert.assertEquals("2.289.3", updateCenter.getUpdateVersionToUse("2.289.9999"));
        // Higher minor
        Assert.assertEquals("2.303.3", updateCenter.getUpdateVersionToUse("2.304.0"));
        // Higher major
        Assert.assertEquals("2.303.3", updateCenter.getUpdateVersionToUse("3.1.0"));
    }
}