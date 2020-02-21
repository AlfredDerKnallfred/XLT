package action.modules;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

import com.xceptance.xlt.api.engine.scripting.AbstractHtmlUnitCommandsModule;


/**
 * TODO: Add class description
 */
public class SelectFrame_iframe_12 extends AbstractHtmlUnitCommandsModule
{


    /**
     * Constructor.
     * 
     */
    public SelectFrame_iframe_12()
    {
    }


    /**
     * @{inheritDoc}
     */
    protected HtmlPage execute(final HtmlPage page) throws Exception
    {
        HtmlPage resultingPage = page;
        resultingPage = selectWindow("title=example page");
        resultingPage = selectFrame("dom=frames[\"iframe1\"].frames[\"iframe2\"]");

        return resultingPage;
    }
}