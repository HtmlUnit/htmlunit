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
package org.htmlunit;

import java.io.StringReader;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.htmlunit.WebClient.PooledCSS3Parser;
import org.htmlunit.cssparser.parser.CSSOMParser;
import org.htmlunit.cssparser.parser.InputSource;
import org.htmlunit.cssparser.parser.javacc.CSS3Parser;
import org.htmlunit.junit.BrowserRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link WebClient} and its CSS3Parser pool.
 *
 * @author René Schwietzke
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class WebClient9Test extends SimpleWebTestCase {

    /**
     * Ensure that we always get fresh parser when there
     * is none and we already fetched one.
     */
    @Test
    public void newParsersWhenNotReturning() {
        try (WebClient webClient = new WebClient()) {
            // ask for a parser
            final CSS3Parser p1 = webClient.getCSS3Parser();

            // if we ask again, it is not the same
            final CSS3Parser p2 = webClient.getCSS3Parser();

            assertFalse(p1 == p2);
        }
    }

    /**
     * When we close a parser, we get it again when
     * asking the next time.
     */
    @SuppressWarnings("resource")
    @Test
    public void closedParserIsPooled() {
        try (WebClient webClient = new WebClient()) {
            // ask for a parser
            final PooledCSS3Parser p1;
            final PooledCSS3Parser p2;

            try (PooledCSS3Parser p = webClient.getCSS3Parser()) {
                assertNotNull(p);
                p1 = p;
            }
            try (PooledCSS3Parser p = webClient.getCSS3Parser()) {
                assertNotNull(p);
                p2 = p;
                assertTrue(p == p1);
            }
            try (PooledCSS3Parser p = webClient.getCSS3Parser()) {
                assertNotNull(p);
                assertTrue(p == p2);
                assertTrue(p1 == p2);
            }
        }
    }

    /**
     * We can nest and get properly different parsers.
     */
    @SuppressWarnings("resource")
    @Test
    public void nestingWorks() {
        try (WebClient webClient = new WebClient()) {
            final PooledCSS3Parser p1;
            final PooledCSS3Parser p2;

            try (PooledCSS3Parser p11 = webClient.getCSS3Parser()) {
                try (PooledCSS3Parser p21 = webClient.getCSS3Parser()) {
                    assertNotNull(p11);
                    assertNotNull(p21);
                    assertFalse(p11 == p21);

                    // keep them
                    p1 = p11;
                    p2 = p21;
                }
            }

            try (PooledCSS3Parser p11 = webClient.getCSS3Parser()) {
                try (PooledCSS3Parser p21 = webClient.getCSS3Parser()) {
                    assertNotNull(p11);
                    assertNotNull(p21);
                    assertFalse(p11 == p21);

                    assertTrue(p11 == p1);
                    assertTrue(p21 == p2);
                }
            }
        }
    }

    /**
     * Take one, returned it, need two... get another new one
     */
    @SuppressWarnings("resource")
    @Test
    public void flow1_2() {
        try (WebClient webClient = new WebClient()) {
            final PooledCSS3Parser p1;

            try (PooledCSS3Parser p11 = webClient.getCSS3Parser()) {
                p1 = p11;
            }

            try (PooledCSS3Parser p11 = webClient.getCSS3Parser()) {
                assertNotNull(p11);
                assertTrue(p11 == p1);

                try (PooledCSS3Parser p21 = webClient.getCSS3Parser()) {
                    assertNotNull(p21);
                    assertFalse(p21 == p11);
                }
            }
        }
    }

    @Test
    public void multithreading() throws InterruptedException {
        final String css = "body { background-color: green; }";

        final int numberOfThreads = 10;

        final AtomicInteger errorCount = new AtomicInteger();
        final CountDownLatch latch = new CountDownLatch(numberOfThreads);

        final WebClient webClient = new WebClient();
        for (int i = 0; i < numberOfThreads; i++) {
            new Thread(() -> {
                try {
                    for (int j = 0; j < 100_000; j++) {
                        try (PooledCSS3Parser pooledParser = webClient.getCSS3Parser()) {
                            assertNotNull(pooledParser);

                            try (InputSource source = new InputSource(new StringReader(css))) {
                                final CSSOMParser parser = new CSSOMParser(pooledParser);
                                parser.parseStyleSheet(source, null);
                            }
                        }
                    }
                }
                catch (final Exception e) {
                    errorCount.addAndGet(1);
                }
                latch.countDown();
            }).start();
        }

        latch.await();
        webClient.close();

        assertEquals(0, errorCount.get());
    }
}
