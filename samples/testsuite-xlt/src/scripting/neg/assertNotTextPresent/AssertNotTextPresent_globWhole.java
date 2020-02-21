package scripting.neg.assertNotTextPresent;

import org.junit.Test;
import org.junit.After;

import com.xceptance.xlt.api.engine.scripting.AbstractWebDriverScriptTestCase;
import com.xceptance.xlt.api.webdriver.XltDriver;
import scripting.util.PageOpener;


/**
 * 
 */
public class AssertNotTextPresent_globWhole extends AbstractWebDriverScriptTestCase
{

	/**
	 * Constructor.
	 */
	public AssertNotTextPresent_globWhole()
	{
		super(new XltDriver(true), null);
	}

	@Test(expected = AssertionError.class)
	public void test() throws Throwable
	{
		PageOpener.examplePage( this );
		assertNotTextPresent( "glob:*ipsum*" );
    }

    @After
    public void after()
    {
        getWebDriver().quit();
    }
}