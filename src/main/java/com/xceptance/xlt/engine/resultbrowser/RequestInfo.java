package com.xceptance.xlt.engine.resultbrowser;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.api.engine.RequestData;

/**
 * Container that holds all information about a request necessary to be processed by the results browser.
 *
 * @author Jörg Werner (Xceptance Software Technologies GmbH)
 */
public class RequestInfo implements Comparable<RequestInfo>
{
    public String fileName;

    public long startTime;

    public long loadTime;

    public String mimeType;

    public String name;

    public final List<NameValuePair> requestHeaders = new ArrayList<NameValuePair>();

    public String requestMethod;

    public final List<NameValuePair> requestParameters = new ArrayList<NameValuePair>();

    public int responseCode;

    public final List<NameValuePair> responseHeaders = new ArrayList<NameValuePair>();

    public String status;

    public String url;

    public String requestBodyRaw;

    /**
     * Encoding used for form data submission.
     */
    public String formDataEncoding;

    /**
     * Request timings (used for HAR export).
     */
    public transient TimingInfo timings;

    public void setTimings(final RequestData request)
    {
        TimingInfo timing = null;
        if (request != null)
        {
            timing = new TimingInfo();
            timing.bytesReceived = request.getBytesReceived();
            timing.bytesSent = request.getBytesSent();
            timing.connectTime = request.getConnectTime();
            timing.dnsTime = request.getDnsTime();
            timing.receiveTime = request.getReceiveTime();
            timing.sendTime = request.getSendTime();
            timing.serverBusyTime = request.getServerBusyTime();
            timing.timeToFirstByte = request.getTimeToFirstBytes();
            timing.timeToLastByte = request.getTimeToLastBytes();

        }

        timings = timing;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(final RequestInfo other)
    {
        // sort by start time, then by load time (in ascending order)
        int result = Long.compare(startTime, other.startTime);
        if (result == 0)
        {
            result = Long.compare(loadTime, other.loadTime);
        }
        return result;
    }

    public static class TimingInfo
    {

        /* Value '-1' means: not available */

        public int bytesSent = -1;

        public int bytesReceived = -1;

        public int connectTime = -1;

        public int receiveTime = -1;

        public int sendTime = -1;

        public int serverBusyTime = -1;

        public int dnsTime = -1;

        public int timeToFirstByte = -1;

        public int timeToLastByte = -1;

        TimingInfo()
        {
        }
    }

}
