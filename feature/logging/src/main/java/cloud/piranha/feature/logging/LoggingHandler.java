/*
 * Copyright (c) 2002-2025 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.feature.logging;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.ConsoleHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The LoggingHandler class.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class LoggingHandler extends Handler {

    /**
     * When {@code -Dpiranha.async.stacktrace=true} every published log record
     * automatically gets a {@link Throwable} attached so that the configured
     * {@link java.util.logging.Formatter} renders the full call stack.
     * Evaluated once at class-load time; restart required to change.
     */
    private static final boolean STACK_TRACE_ENABLED =
            Boolean.getBoolean("piranha.async.stacktrace");

    /**
     * Stores the executor service for handling log records.
     */
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    /**
     * Stores the original ConsoleHandler.
     */
    private final ConsoleHandler consoleHandler = new ConsoleHandler();

    /**
     * Constructor.
     */
    public LoggingHandler() {
        super.setLevel(Level.ALL);
        consoleHandler.setLevel(Level.ALL);
    }

    @Override
    public void publish(LogRecord record) {
        if (STACK_TRACE_ENABLED && record.getThrown() == null) {
            record.setThrown(new Throwable("caller stack"));
        }
        executorService.submit(new LogRecordPublisher(record));
    }

    /**
     * The LogRecordPublisher class.
     */
    private class LogRecordPublisher implements Runnable {

        /**
         * Stores the LogRecord.
         */
        private final LogRecord record;

        /**
         * Constructor.
         * 
         * @param record the log record.
         */
        LogRecordPublisher(LogRecord record) {
            this.record = record;
        }

        @Override
        public void run() {
            consoleHandler.publish(record);
        }
    }

    @Override
    public void flush() {
        consoleHandler.flush();
    }

    @Override
    public void close() throws SecurityException {
        consoleHandler.close();
        executorService.shutdown();
    }
}
