package md.dev.integration;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import md.dev.log.LoggerHandler;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features="src/test/resources/features", glue="")
public class IntegrationRunner {
    @BeforeClass
    public static void setup() {
        LoggerHandler.disableLog();
    }
}
