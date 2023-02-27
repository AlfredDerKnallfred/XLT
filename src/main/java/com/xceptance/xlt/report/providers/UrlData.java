/*
 * Copyright (c) 2005-2022 Xceptance Software Technologies GmbH
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
package com.xceptance.xlt.report.providers;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.xceptance.xlt.api.util.XltCharBuffer;

/**
 * Represents a list of most called URLs for detailed reporting. Keeps also total data.
 */
@XStreamAlias("urls")
public class UrlData
{
    /**
     * The total count of different URLs.
     */
    public int total;

    /**
     * A list of URLs.
     */
    public List<String> list;
}
