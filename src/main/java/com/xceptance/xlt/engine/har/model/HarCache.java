package com.xceptance.xlt.engine.har.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * A request fetched from browser/application cache.
 *
 * @see <a href="http://www.softwareishard.com/blog/har-12-spec/#cache">specification</a>
 */
@JsonPropertyOrder(
    {
        "beforeRequest", "afterRequest", "comment"
    })
public class HarCache
{
    private final HarCacheRequest beforeRequest;

    private final HarCacheRequest afterRequest;

    private final String comment;

    @JsonCreator
    public HarCache(@JsonProperty("beforeRequest") HarCacheRequest beforeRequest,
                    @JsonProperty("afterRequest") HarCacheRequest afterRequest, @JsonProperty("comment") String comment)
    {
        this.beforeRequest = beforeRequest;
        this.afterRequest = afterRequest;
        this.comment = comment;
    }

    public HarCacheRequest getBeforeRequest()
    {
        return beforeRequest;
    }

    public HarCacheRequest getAfterRequest()
    {
        return afterRequest;
    }

    public String getComment()
    {
        return comment;
    }

    @Override
    public String toString()
    {
        return "HarCache [beforeRequest = " + beforeRequest + ", afterRequest = " + afterRequest + ", comment = " + comment + "]";
    }

    public static class Builder
    {
        private HarCacheRequest beforeRequest;

        private HarCacheRequest afterRequest;

        private String comment;

        public Builder withBeforeRequest(HarCacheRequest beforeRequest)
        {
            this.beforeRequest = beforeRequest;
            return this;
        }

        public Builder withAfterRequest(HarCacheRequest afterRequest)
        {
            this.afterRequest = afterRequest;
            return this;
        }

        public Builder withComment(String comment)
        {
            this.comment = comment;
            return this;
        }

        public HarCache build()
        {
            return new HarCache(beforeRequest, afterRequest, comment);
        }

    }
}
