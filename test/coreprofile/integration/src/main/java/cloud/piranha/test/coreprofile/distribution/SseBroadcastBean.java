/*
 * Copyright (c) 2002-2024 Manorrock.com. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   1. Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *   2. Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *   3. Neither the name of the copyright holder nor the names of its
 *      contributors may be used to endorse or promote products derived from
 *      this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package cloud.piranha.test.coreprofile.distribution;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.sse.OutboundSseEvent;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseEventSink;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The single and one and only SSE broadcast bean.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@ApplicationScoped
public class SseBroadcastBean {

    /**
     * List of SSE event sinks.
     */
    private final List<SseEventSink> sinks = new CopyOnWriteArrayList<>();

    /**
     * Store the SSE.
     */
    @Context
    private Sse sse;

    /**
     * Register the given SSE event sink.
     *
     * @param sink the SSE event sink.
     */
    public void register(SseEventSink sink) {
        sinks.add(sink);
    }

    /**
     * Broadcast the given message 10 times.
     *
     * @param message the message.
     */
    public void broadcast(String message) {
        for (int i = 1; i <= 10; i++) {
            String eventMessage = message + " #" + i;
            OutboundSseEvent event = sse.newEventBuilder()
                    .name("message")
                    .data(String.class, eventMessage)
                    .build();
            sinks.forEach(sink -> sink.send(event));
        }
    }
}
