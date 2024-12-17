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
import jakarta.ws.rs.sse.SseBroadcaster;
import jakarta.ws.rs.sse.SseEventSink;

/**
 * The single and one and only SSE broadcast bean.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@ApplicationScoped
public class SseBroadcastBean {

    /**
     * Stores the broadcaster.
     */
    private SseBroadcaster broadcaster;

    /**
     * Store the SSE.
     */
    @Context
    private Sse sse;

    /**
     * Constructor.
     */
    public SseBroadcastBean() {
    }

    /**
     * Register the given SSE event sink.
     *
     * @param sink the SSE event sink.
     */
    public void register(SseEventSink sink) {
        synchronized (sse) {
            if (broadcaster == null) {
                broadcaster = sse.newBroadcaster();
            }
        }
        broadcaster.register(sink);
    }

    /**
     * Broadcast the given message 10 times.
     *
     * @param message the message.
     */
    public void broadcast(String message) {
        if (broadcaster != null) {
            for (int i = 1; i <= 10; i++) {
                String eventMessage = message + " #" + i;
                OutboundSseEvent event = sse.newEventBuilder()
                        .data(String.class, eventMessage)
                        .build();
                broadcaster.broadcast(event);
            }
        }
    }

    /**
     * Close the event sinks.
     */
    public void close() {
        broadcaster.close();
    }
}
