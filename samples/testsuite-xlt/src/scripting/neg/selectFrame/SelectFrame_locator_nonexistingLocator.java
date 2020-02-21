package scripting.neg.selectFrame;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.InvalidSelectorException;

import scripting.util.PageOpener;

import com.xceptance.xlt.api.engine.scripting.AbstractWebDriverScriptTestCase;
import com.xceptance.xlt.api.webdriver.XltDriver;


/**
 * 
 */
public class SelectFrame_locator_nonexistingLocator extends AbstractWebDriverScriptTestCase
{

	/**
	 * Constructor.
	 */
	public SelectFrame_locator_nonexistingLocator()
	{
		super(new XltDriver(true), null);
	}

	@Test(expected = InvalidSelectorException.class)
	public void test() throws Throwable
	{
		PageOpener.examplePage( this );
		selectFrame( "xyz=iframe1" );
    }

    @After
    public void after()
    {
        getWebDriver().quit();
    }
}