package com.xceptance.xlt.misc.performance;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.xceptance.xlt.engine.util.TimerUtils;

/**
 * Check the best use of XPath expressions on a document.
 * 
 * @author René Schwietzke (Xceptance Software Technologies GmbH)
 */
public class XPathPerformanceTest extends AbstractHtmlTest
{
    /**
     * HTML page used for test.
     */
    private HtmlPage htmlPage;

    /**
     * Test fixture setup.
     * 
     * @throws Exception
     *             thrown when setup failed
     */
    @Before
    public void setUp() throws Exception
    {
        htmlPage = setUp(this);
    }

    /**
     * Runs the speed test.
     * 
     * @throws Exception
     *             thrown when something went wrong
     */
    @Test
    @Ignore("Performance test")
    public void testPerformance() throws Exception
    {
        final int count = 5000;
        // html > body > div #doc > div #container > div #content > form #addressForm
        final String fullXPath = "/html/body/div[@id='doc']/div[@id='container']/div[@id='content']/form[@id='addressForm']";
        final String fullXPathNoIDs = "/html/body/div/div/div/form[@id='addressForm']";
        final String allUnderBodyXPath = "//div[@id='doc']/div[@id='container']/div[@id='content']/form[@id='addressForm']";
        final String allFormsXPath = "//form[@id='addressForm']";
        final String byIDFunction = "id('addressForm')";

        final String contentXPath = "/html/body/div[@id='doc']/div[@id='container']/div[@id='content']";
        final String onlyThisFormXPath = "./form[@id='addressForm']";

        final long s1 = TimerUtils.getTime();
        List<?> list1 = null;
        for (int i = 0; i < count; i++)
        {
            list1 = htmlPage.getByXPath(fullXPath);
        }
        final long e1 = TimerUtils.getTime();

        final long s2 = TimerUtils.getTime();
        List<?> list2 = null;
        for (int i = 0; i < count; i++)
        {
            list2 = htmlPage.getByXPath(allUnderBodyXPath);
        }
        final long e2 = TimerUtils.getTime();

        final long s3 = TimerUtils.getTime();
        List<?> list3 = null;
        for (int i = 0; i < count; i++)
        {
            list3 = htmlPage.getByXPath(allFormsXPath);
        }
        final long e3 = TimerUtils.getTime();

        final long s4 = TimerUtils.getTime();
        List<?> list4 = null;
        for (int i = 0; i < count; i++)
        {
            list4 = htmlPage.getByXPath(fullXPathNoIDs);
        }
        final long e4 = TimerUtils.getTime();

        final long s5 = TimerUtils.getTime();
        final List<?> content = htmlPage.getByXPath(contentXPath);
        final HtmlElement element = (HtmlElement) content.get(0);
        List<?> list5 = null;
        for (int i = 0; i < count; i++)
        {
            list5 = element.getByXPath(onlyThisFormXPath);
        }
        final long e5 = TimerUtils.getTime();

        final long s6 = TimerUtils.getTime();
        HtmlElement id = null;
        for (int i = 0; i < count; i++)
        {
            id = htmlPage.getHtmlElementById("addressForm");
        }
        final long e6 = TimerUtils.getTime();

        final long s7 = TimerUtils.getTime();
        List<?> list7 = null;
        for (int i = 0; i < count; i++)
        {
            list7 = htmlPage.getByXPath(byIDFunction);
        }
        final long e7 = TimerUtils.getTime();

        Assert.assertNotNull(list1);
        Assert.assertEquals(1, list1.size());
        Assert.assertNotNull(list2);
        Assert.assertEquals(1, list2.size());
        Assert.assertNotNull(list3);
        Assert.assertEquals(1, list3.size());
        Assert.assertNotNull(list4);
        Assert.assertEquals(1, list4.size());
        Assert.assertNotNull(list5);
        Assert.assertEquals(1, list5.size());
        Assert.assertNotNull(id);
        Assert.assertEquals(1, list7.size());
        System.out.println(formattedMsg("Full", e1 - s1, null));
        System.out.println(formattedMsg("All under body", e2 - s2, "- " + allUnderBodyXPath));
        System.out.println(formattedMsg("Only element", e3 - s3, "- " + allFormsXPath));
        System.out.println(formattedMsg("Less ids", e4 - s4, "- " + fullXPathNoIDs));
        System.out.println(formattedMsg("Query on result", e5 - s5, "- " + contentXPath + " && " + onlyThisFormXPath));
        System.out.println(formattedMsg("getHtmlElementById", e6 - s6, "- getHtmlElementById(\"addressForm\")"));
        System.out.println(formattedMsg("xpath id function", e7 - s7, "- " + byIDFunction));

    }

    private String formattedMsg(final String msg, final long duration, final String appendix)
    {
        return StringUtils.rightPad(msg, 20) + ":" + StringUtils.leftPad(duration + "ms ", 8) + ((appendix != null) ? appendix : "");
    }
}
