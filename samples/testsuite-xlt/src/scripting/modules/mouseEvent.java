package scripting.modules;

import com.xceptance.xlt.api.engine.scripting.AbstractWebDriverModule;
import scripting.modules.Open_ExamplePage;

/**
 * TODO: Add class description
 */
public class mouseEvent extends AbstractWebDriverModule
{

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doCommands(final String...parameters) throws Exception
    {
        final Open_ExamplePage _open_ExamplePage = new Open_ExamplePage();
        _open_ExamplePage.execute();


        //
        // ~~~ mouseOver ~~~
        //
        startAction("mouseOver");
        mouseOver("id=mouseevent_a");
        assertText("id=cc_mouseover_head", "mouseover (mouseevent_a)");

        //
        // ~~~ mouseDown ~~~
        //
        startAction("mouseDown");
        mouseDown("xpath=id('mouseevent')/a");
        assertText("id=cc_mousedown_head", "mousedown (mouseevent_a)");

        //
        // ~~~ mouseUp ~~~
        //
        startAction("mouseUp");
        mouseUp("name=mouseevent_a");
        assertText("id=cc_mouseup_head", "mouseup (mouseevent_a)");

        //
        // ~~~ mouseOut ~~~
        //
        startAction("mouseOut");
        mouseOut("link=mouseevent_a");
        assertText("id=cc_mouseout_head", "mouseout (mouseevent_a)");

        //
        // ~~~ mouseOver ~~~
        //
        startAction("mouseOver");
        click("id=cc_clear_button");
        mouseOver("dom=document.getElementById('mouseevent_a')");
        assertText("id=cc_mouseover_head", "mouseover (mouseevent_a)");

        //
        // ~~~ mouseDownAt ~~~
        //
        startAction("mouseDownAt");
        click("id=cc_clear_button");
        mouseDownAt("css=#mouseevent_a", "5,3");
        assertText("id=cc_mousedown_head", "mousedown (mouseevent_a)");

        //
        // ~~~ mouseMoveAt ~~~
        //
        startAction("mouseMoveAt");
        mouseMoveAt("id=mouseevent_a", "5,5");
        assertText("id=cc_mousemove_head", "mousemove (mouseevent_a)");

        //
        // ~~~ mouseUpAt ~~~
        //
        startAction("mouseUpAt");
        mouseUpAt("id=mouseevent_a", "20,3");
        assertText("id=cc_mouseup_head", "mouseup (mouseevent_a)");

        //
        // ~~~ mouseMove ~~~
        //
        startAction("mouseMove");
        click("id=cc_clear_button");
        mouseMove("id=mouseevent_a");
        assertText("id=cc_mousemove_head", "mousemove (mouseevent_a)");

    }
}