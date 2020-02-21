package com.xceptance.xlt.api.engine;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebResponseData;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * @author Sebastian Oerding
 */
public class NetworkDataTest
{
    @Test
    public void test() throws MalformedURLException
    {
        final NetworkData data = new NetworkData(new WebRequest(new URL("http://www.heise.de")), null);

        Assert.assertEquals("Default status code changed!", 0, data.getResponseStatusCode());
        Assert.assertEquals("Default content changed!", null, data.getContentAsString());
        Assert.assertEquals("Default content type changed!", null, data.getContentType());
        Assert.assertEquals("Default response status message changed!", null, data.getResponseStatusMessage());
        Assert.assertEquals("Default response headers changed!", Collections.emptyList(), data.getResponseHeaders());
    }

    @Test
    public void test2() throws MalformedURLException, IOException
    {
        final WebRequest request = new WebRequest(new URL("http://www.heise.de"));
        final String content = "<meta content=\" charset=utf8\"";
        final List<NameValuePair> responseHeaders = new ArrayList<NameValuePair>();
        responseHeaders.add(new NameValuePair("content-type", "text/html"));
        final WebResponseData wrd = new WebResponseData(content.getBytes(), 200, "Don't mind", responseHeaders);
        final WebResponse response = new WebResponse(wrd, request, 0);
        final NetworkData data = new NetworkData(request, response);

        Assert.assertEquals("Status code changed!", 200, data.getResponseStatusCode());
        Assert.assertEquals("Content changed!", content, data.getContentAsString());
        Assert.assertEquals("Content type changed!", "text/html", data.getContentType());
        Assert.assertEquals("Default response status message changed!", "Don't mind", data.getResponseStatusMessage());
        Assert.assertEquals("Default response headers changed!", responseHeaders, data.getResponseHeaders());

        Assert.assertEquals("Wrong request method, ", request.getHttpMethod(), data.getRequestMethod());
        Assert.assertEquals("Wrong request body, ", request.getRequestBody(), data.getRequestBody());
        Assert.assertEquals("Wrong request parameters, ", request.getRequestParameters(), data.getRequestParameters());
        Assert.assertEquals("Wrong request additional request headers, ", request.getAdditionalHeaders(),
                            data.getAdditionalRequestHeaders());

        Assert.assertEquals("Wrong request URI, ", request.getUrl(), data.getURL());
        Assert.assertEquals("Wrong response, ", response, data.getResponse());
    }
}
