package scripting.testcases;

import org.junit.After;
import org.junit.Test;
import com.xceptance.xlt.api.webdriver.XltDriver;
import com.xceptance.xlt.api.engine.scripting.AbstractWebDriverScriptTestCase;

/**
 * TODO: Add class description
 */
public class deleteAllVisibleCookies extends AbstractWebDriverScriptTestCase
{

    /**
     * Constructor.
     */
    public deleteAllVisibleCookies()
    {
        super(new XltDriver(true), "http://localhost:8080/");
    }


    /**
     * Executes the test.
     *
     * @throws Throwable if anything went wrong
     */
    @Test
    public void test() throws Throwable
    {
        final scripting.modules.deleteAllVisibleCookies _deleteAllVisibleCookies = new scripting.modules.deleteAllVisibleCookies();
        _deleteAllVisibleCookies.execute();


    }


    /**
     * Clean up.
     */
    @After
    public void after()
    {
        // Shutdown WebDriver.
        getWebDriver().quit();
    }
}