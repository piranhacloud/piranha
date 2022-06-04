/*
 * Copyright (c) 2002-2022 Manorrock.com. All Rights Reserved.
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

package cloud.piranha.test.common;

import java.io.IOException;
import java.lang.System.Logger.Level;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

/**
 *
 * @author Ondro Mihalyi
 */
public class PiranhaStartup {

    /**
     * Wait until Piranha process is ready and opens the port. 
     * @param process
     * @param port
     */
    public static void waitUntilPiranhaReady(Process process, int port) {
        waitUntilPiranhaReady(port, () -> {
            return process.isAlive();
        });
    }

    /**
     * Wait until Piranha is ready and opens the port. 
     * @param port
     */
    public static void waitUntilPiranhaReady(int port) {
        waitUntilPiranhaReady(port, () -> true);
    }

    private static void waitUntilPiranhaReady(int port, BooleanSupplier customCheck) {
        final int timeoutMillis = 30 * 1000;
        final int sleepTimeMillis = 100;
        long initialTimeMillis = System.currentTimeMillis();
        boolean started = false;

        while (customCheck.getAsBoolean()
                && !started) 
                {
            if (initialTimeMillis + timeoutMillis < System.currentTimeMillis()) { // timeout reached
                throw new RuntimeException("Piranha wasn't ready before timeout reached. Timeout is " 
                        + timeoutMillis + " milliseconds.");
            }
            try {
                TimeUnit.MILLISECONDS.sleep(sleepTimeMillis);
                pingPiranha("localhost", port);
                started = true;
            } catch (IOException ex) {
                System.getLogger(PiranhaStartup.class.getName()).log(Level.DEBUG, "Not ready yet: {0}({1}", new Object[]{ex.getMessage(), ex.getClass()});
            } catch (InterruptedException ex) {
                System.getLogger(PiranhaStartup.class.getName()).log(Level.WARNING, ex.getMessage(), ex);
                Thread.currentThread().interrupt();
            }
        }

        if (!customCheck.getAsBoolean()) {
            throw new RuntimeException("Piranha Micro failed to start");
        }
        
        // wait some more time just in case the port is already bound to but Piranha isn't ready to serve requests yet
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static void pingPiranha(String host, int port) throws IOException, InterruptedException {
        try ( Socket s = new Socket(host, port)) {
        }
    }

}
