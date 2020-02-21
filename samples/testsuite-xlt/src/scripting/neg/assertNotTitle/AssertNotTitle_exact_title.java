package scripting.neg.assertNotTitle;

import org.junit.Test;
import org.junit.After;

import com.xceptance.xlt.api.engine.scripting.AbstractWebDriverScriptTestCase;
import com.xceptance.xlt.api.webdriver.XltDriver;
import scripting.util.PageOpener;


/**
 * 
 */
public class AssertNotTitle_exact_title extends AbstractWebDriverScriptTestCase
{

	/**
	 * Constructor.
	 */
	public AssertNotTitle_exact_title()
	{
		super(new XltDriver(true), null);
	}

	@Test(expected = AssertionError.class)
	public void test() throws Throwable
	{
		PageOpener.examplePage( this );
		assertNotTitle( "exact:example page" );
    }

    @After
    public void after()
    {
        getWebDriver().quit();
    }
}