
package calculator.cucumberTests;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("calculator")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "calculator")
@SuppressWarnings("PMD.TestClassWithoutTestCases")
public class CucumberTest {
    // This class is empty, it is used only as a holder for the above annotations
}
