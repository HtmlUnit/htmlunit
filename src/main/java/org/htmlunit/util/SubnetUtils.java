/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.htmlunit.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Performs subnet calculations given a network address and a subnet mask.
 * Inspired by org.apache.commons.net.util.SubnetUtils.
 *
 * @see "http://www.faqs.org/rfcs/rfc1519.html"
 *
 * @author Ronald Brill
 */
public class SubnetUtils {

    private static final String IP_ADDRESS = "(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})";
    private static final Pattern ADDRESS_PATTERN = Pattern.compile(IP_ADDRESS);
    private static final String PARSE_FAIL = "Could not parse [%s]";
    private static final long UNSIGNED_INT_MASK = 0x0FFFFFFFFL;

    private final int netmask_;
    private final int address_;
    private final int network_;
    private final int broadcast_;

    /**
     * Constructs an instance from a dotted decimal address and a dotted decimal mask.
     *
     * @param address An IP address, e.g. "192.168.0.1"
     * @param mask    A dotted decimal netmask e.g. "255.255.0.0"
     * @throws IllegalArgumentException if the address or mask is invalid, i.e. does not match n.n.n.n where n=1-3 decimal digits and the mask is not all zeros
     */
    public SubnetUtils(final String address, final String mask) {
        address_ = toInteger(address);
        netmask_ = toInteger(mask);

        if ((netmask_ & -netmask_) - 1 != ~netmask_) {
            throw new IllegalArgumentException(PARSE_FAIL.formatted(mask));
        }

        network_ = address_ & netmask_;
        broadcast_ = network_ | ~netmask_;
    }

    /*
     * Extracts the components of a dotted decimal address and pack into an integer using a regex match
     */
    private static int matchAddress(final Matcher matcher) {
        int addr = 0;
        for (int i = 1; i <= 4; ++i) {
            final int n = rangeCheck(Integer.parseInt(matcher.group(i)), 0, 255);
            addr |= (n & 0xff) << 8 * (4 - i);
        }
        return addr;
    }

    /*
     * Checks integer boundaries. Checks if a value x is in the range [begin,end]. Returns x if it is in range, throws an exception otherwise.
     */
    private static int rangeCheck(final int value, final int begin, final int end) {
        // (begin,end]
        if (value >= begin && value <= end) {
            return value;
        }
        throw new IllegalArgumentException("Value [" + value + "] not in range [" + begin + "," + end + "]");
    }

    /*
     * Converts a dotted decimal format address to a packed integer format
     */
    private static int toInteger(final String address) {
        final Matcher matcher = ADDRESS_PATTERN.matcher(address);
        if (matcher.matches()) {
            return matchAddress(matcher);
        }
        throw new IllegalArgumentException(PARSE_FAIL.formatted(address));
    }

    private long broadcastLong() {
        return broadcast_ & UNSIGNED_INT_MASK;
    }

    private int high() {
        return broadcastLong() - networkLong() > 1 ? broadcast_ - 1 : 0;
    }

    private int low() {
        return broadcastLong() - networkLong() > 1 ? network_ + 1 : 0;
    }

    /** Long versions of the values (as unsigned int) which are more suitable for range checking. */
    private long networkLong() {
        return network_ & UNSIGNED_INT_MASK;
    }

    /**
     * Tests if the parameter <code>address</code> is in the range of usable endpoint addresses for this subnet.
     * This excludes the network and broadcast addresses by default.
     *
     * @param address the address to check
     * @return true if it is in range
     */
    private boolean isInRange(final int address) {
        if (address == 0) {
            return false;
        }
        final long addLong = address & UNSIGNED_INT_MASK;
        final long lowLong = low() & UNSIGNED_INT_MASK;
        final long highLong = high() & UNSIGNED_INT_MASK;
        return addLong >= lowLong && addLong <= highLong;
    }

    /**
     * Tests if the parameter <code>address</code> is in the range of usable endpoint addresses for this subnet.
     * This excludes the network and broadcast addresses.
     *
     * @param address A dot-delimited IPv4 address, e.g. "192.168.0.1"
     * @return true if in range, false otherwise
     */
    public boolean isInRange(final String address) {
        return isInRange(toInteger(address));
    }
}
