/**
 * Copyright (c) 2009-2011, netBout.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are PROHIBITED without prior written permission from
 * the author. This product may NOT be used anywhere and on any computer
 * except the server platform of netBout Inc. located at www.netbout.com.
 * Federal copyright law prohibits unauthorized reproduction by any means
 * and imposes fines up to $25,000 for violation. If you received
 * this code occasionally and without intent to use it, please report this
 * incident to the author by email.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */
package com.netbout.inf;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Mocker of {@link Index}.
 * @author Yegor Bugayenko (yegor@netbout.com)
 * @version $Id$
 */
public final class IndexMocker {

    /**
     * All maps.
     */
    private final transient ConcurrentMap<String, ConcurrentMap<Object, Object>> maps =
        new ConcurrentHashMap<String, ConcurrentMap<Object, Object>>();

    /**
     * The object.
     */
    private final transient Index index = Mockito.mock(Index.class);

    /**
     * Public ctor.
     */
    public IndexMocker() {
        Mockito.doAnswer(
            new Answer<ConcurrentMap>() {
                public ConcurrentMap answer(final InvocationOnMock invocation) {
                    final String name = (String) invocation.getArguments()[0];
                    synchronized (IndexMocker.this.maps) {
                        if (!IndexMocker.this.maps.containsKey(name)) {
                            IndexMocker.this.maps.put(
                                name,
                                new ConcurrentHashMap<Object, Object>()
                            );
                        }
                        return IndexMocker.this.maps.get(name);
                    }
                }
            }
        ).when(this.index).get(Mockito.anyString());
    }

    /**
     * Build it.
     * @return The Index
     */
    public Index mock() {
        return this.index;
    }

}