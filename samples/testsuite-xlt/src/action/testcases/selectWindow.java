package action.testcases;

import org.junit.Test;
import com.xceptance.xlt.api.actions.AbstractHtmlPageAction;
import com.xceptance.xlt.api.engine.scripting.AbstractHtmlUnitScriptTestCase;


/**
 * TODO: Add class description
 */
public class selectWindow extends AbstractHtmlUnitScriptTestCase
{

    /**
     * Constructor.
     */
    public selectWindow()
    {
        super("http://localhost:8080/");
    }

    @Test
    public void test() throws Throwable
    {
        AbstractHtmlPageAction lastAction = null;

        final action.modules.selectWindow selectWindow = new action.modules.selectWindow();
        lastAction = selectWindow.run(lastAction);


    }
}