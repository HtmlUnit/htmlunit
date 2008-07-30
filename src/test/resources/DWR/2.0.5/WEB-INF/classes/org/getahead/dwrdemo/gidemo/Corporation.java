/*
 * Copyright 2005 Joe Walker
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
package org.getahead.dwrdemo.gidemo;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * An object representing the stock price of a corporation.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Corporation
{
    /**
     * @param jsxid
     * @param name
     * @param last
     * @param change
     */
    public Corporation(String jsxid, String name)
    {
        this.jsxid = jsxid;
        this.name = name;

        float temp = random.nextFloat() * 100.0F;
        last = new BigDecimal(Math.round(temp * 100.0F) / 100.0F);
        last = last.setScale(2, BigDecimal.ROUND_UP);
        time = new Date();
        change = new BigDecimal(0.0F);
        change = change.setScale(2, BigDecimal.ROUND_UP);
        max = last;
        min = last;
    }

    private String jsxid;

    private String name;

    private BigDecimal last;

    private Date time;

    private BigDecimal change;

    private BigDecimal max;

    private BigDecimal min;

    private Random random = new Random();

    /**
     * @return the jsxid
     */
    public String getJsxid()
    {
        return jsxid;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return the last
     */
    public BigDecimal getLast()
    {
        return last;
    }

    /**
     * @return the time
     */
    public String getTime()
    {
        return FORMAT.format(time);
    }

    /**
     * @return the change
     */
    public BigDecimal getChange()
    {
        return change;
    }

    /**
     * @return the max
     */
    public BigDecimal getMax()
    {
        return max;
    }

    /**
     * @return the min
     */
    public BigDecimal getMin()
    {
        return min;
    }

    /**
     * Alter the stock price
     */
    public void change()
    {
        float newChange = (random.nextFloat() * 4) - 2;
        newChange = Math.round(newChange * 100.0F) / 100.0F;

        if (last.floatValue() + newChange < 9)
        {
            newChange = -newChange;
        }

        change = new BigDecimal(newChange);
        change = change.setScale(2, BigDecimal.ROUND_UP);

        last = last.add(change);

        if (last.compareTo(max) > 0)
        {
            max = last;
        }

        if (last.compareTo(min) < 0)
        {
            min = last;
        }

        time = new Date();
    }

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("hh:MM:ss");
}
