/*
 * Copyright (c) 2005-2024 Xceptance Software Technologies GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package scripting.testcases;
import org.junit.Test;
import com.xceptance.xlt.api.engine.scripting.AbstractWebDriverScriptTestCase;
import scripting.modules.Open_ExamplePage;

/**
 * TODO: Add class description
 */
public class assertNotAttribute extends AbstractWebDriverScriptTestCase
{

    /**
     * Constructor.
     */
    public assertNotAttribute()
    {
        super("http://localhost:8080");
    }


    /**
     * Executes the test.
     *
     * @throws Throwable if anything went wrong
     */
    @Test
    public void test() throws Throwable
    {
        final Open_ExamplePage _open_ExamplePage = new Open_ExamplePage();
        _open_ExamplePage.execute();

        // 1 vs 2 whitespaces
        assertNotAttribute("xpath=id('select_17')/option[@title='2 spaces']@value", " ");
        // 2 vs 1 whitespace
        assertNotAttribute("xpath=id('select_17')/option[@title='1 space']@value", "  ");
        // empty attribute value must not match any sign
        assertNotAttribute("xpath=id('in_txt_13')@value", "regexp:.+");
        // substring (max length n-1) must not match
        assertNotAttribute("xpath=id('ws8_a')/input[1]@value", "foo");
        // any single character must not match
        assertNotAttribute("xpath=id('ws8_a')/input[1]@value", "?");

    }

}