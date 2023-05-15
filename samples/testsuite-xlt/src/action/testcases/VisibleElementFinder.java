/*
 * Copyright (c) 2005-2023 Xceptance Software Technologies GmbH
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
package action.testcases;

import org.junit.Test;
import com.xceptance.xlt.api.actions.AbstractHtmlPageAction;
import com.xceptance.xlt.api.engine.scripting.AbstractHtmlUnitScriptTestCase;


/**
 * Locator points to visible AND invisible elements. The invisible one is listed before the visible one.
 The element finder must choose the visible (second) one.
 */
public class VisibleElementFinder extends AbstractHtmlUnitScriptTestCase
{

    /**
     * Constructor.
     */
    public VisibleElementFinder()
    {
        super("http://localhost:8080");
    }

    @Test
    public void test() throws Throwable
    {
        AbstractHtmlPageAction lastAction = null;

        final action.modules.VisibleElementFinder visibleElementFinder = new action.modules.VisibleElementFinder();
        lastAction = visibleElementFinder.run(lastAction);


    }
}