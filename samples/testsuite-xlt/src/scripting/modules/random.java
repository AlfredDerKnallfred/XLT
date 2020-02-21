package scripting.modules;

import com.xceptance.xlt.api.engine.scripting.AbstractWebDriverModule;
import scripting.modules.Open_ExamplePage;
import scripting.modules.Random_ModuleWithParam;

/**
 * TODO: Add class description
 */
public class random extends AbstractWebDriverModule
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
        // ~~~ random_alphanumeric_string ~~~
        //
        startAction("random_alphanumeric_string");
        type("id=in_txt_1", resolve("${RANDOM.String(5)}"));
        assertText("id=cc_keyup", "regexp:keyup \\(in_txt_1\\) [a-z]{5}");
        //
        // ~~~ random_number ~~~
        //
        startAction("random_number");
        type("id=in_txt_1", resolve("${RANDOM.Number(10,99)}"));
        assertText("id=cc_keyup", "regexp:keyup \\(in_txt_1\\) \\d{2}");
        type("id=in_txt_1", resolve("${RANDOM.Number(9)}"));
        assertText("id=cc_keyup", "regexp:keyup \\(in_txt_1\\) \\d{1}");
        //
        // ~~~ timestamp ~~~
        //
        startAction("timestamp");
        type("id=in_txt_1", resolve("${NOW}"));
        assertText("id=cc_keyup", "regexp:keyup \\(in_txt_1\\) \\d{13}");
        //
        // ~~~ double ~~~
        //
        startAction("double");
        type("id=in_txt_2", resolve("${RANDOM.String(8)}"));
        assertNotText("id=in_txt_2", resolve("exact:${RANDOM.String(8)}"));
        //
        // ~~~ randomParam ~~~
        //
        startAction("randomParam");
        final Random_ModuleWithParam _random_ModuleWithParam = new Random_ModuleWithParam();
        _random_ModuleWithParam.execute(resolve("${RANDOM.Number(0,5)}"));

        //
        // ~~~ randomParamWithPlaceholder ~~~
        //
        startAction("randomParamWithPlaceholder");
        final Random_ModuleWithParam _random_ModuleWithParam0 = new Random_ModuleWithParam();
        _random_ModuleWithParam0.execute(resolve("${RANDOM.Number(0,${myCount})}"));


    }
}