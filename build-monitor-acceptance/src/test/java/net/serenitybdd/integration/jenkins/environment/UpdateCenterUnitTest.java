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
        Assert.assertEquals("2.346.2", updateCenter.getUpdateVersionToUse("2.346.2"));
        // Higher patch
        Assert.assertEquals("2.346.3", updateCenter.getUpdateVersionToUse("2.346.9999"));
        // Higher minor
        Assert.assertEquals("2.361.4", updateCenter.getUpdateVersionToUse("2.362.0"));
        // Higher major
        Assert.assertEquals("2.375.4", updateCenter.getUpdateVersionToUse("3.1.0"));
    }
}
