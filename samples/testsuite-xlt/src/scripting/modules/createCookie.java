package scripting.modules;

import com.xceptance.xlt.api.engine.scripting.AbstractWebDriverModule;
import scripting.modules.Open_ExamplePage;
import scripting.modules.AssertCookie;

/**
 * TODO: Add class description
 */
public class createCookie extends AbstractWebDriverModule
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
        // ~~~ cleanup ~~~
        //
        startAction("cleanup");
        deleteCookie("x_1");
        deleteCookie("x_2");
        deleteCookie("x_3");
        deleteCookie("x_4");
        deleteCookie("x_5");
        deleteCookie("x_6");
        deleteCookie("x_7");

        //
        // ~~~ create ~~~
        //
        startAction("create");
        createCookie("x_1=create");
        final AssertCookie _assertCookie = new AssertCookie();
        _assertCookie.execute("x_1", "create");


        //
        // ~~~ overwrite ~~~
        //
        startAction("overwrite");
        createCookie("x_2=value_a");
        createCookie("x_2=value_b");
        _assertCookie.execute("x_2", "value_b");


        //
        // ~~~ empty_cookie_value ~~~
        //
        startAction("empty_cookie_value");
        createCookie("x_3=");
        _assertCookie.execute("x_3", "");


        //
        // ~~~ optionsString ~~~
        //
        startAction("optionsString");
        createCookie("x_4=create_with_option_string", "path=/,max_age=10");
        _assertCookie.execute("x_4", "create_with_option_string");


        //
        // ~~~ specialChars ~~~
        //
        // startAction("specialChars");
        // createCookie("x_5=^°!§$%&`´|üöäÜÖÄß+*~#'-_.\\");
        // _assertCookie.execute("x_5","^°!§$%&`´|üöäÜÖÄß+*~#'-_.\\");

        //
        // ~~~ quotedString ~~~
        //
        startAction("quotedString");
        createCookie("x_6=\"( ){ }[ ]< >:@?/=,\"");
        _assertCookie.execute("x_6", "\"( ){ }[ ]< >:@?/=,\"");


        //
        // ~~~ create_without_open_page ~~~
        //
        startAction("create_without_open_page");
        close();
        createCookie("x_7=create", "path=/testpages/examplePage_1.html");
        _open_ExamplePage.execute();

        _assertCookie.execute("x_7", "create");


        //
        // ~~~ cleanup ~~~
        //
        startAction("cleanup");
        deleteCookie("x_1");
        deleteCookie("x_2");
        deleteCookie("x_3");
        deleteCookie("x_4");
        deleteCookie("x_5");
        deleteCookie("x_6");
        deleteCookie("x_7");

    }
}