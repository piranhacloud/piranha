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
import java.net.Socket;
import java.util.Random;

/**
 *
 * Finds a free port that isn't currently bound to by anything else.
 */
public class FreePortFinder {

    /**
     * Finds a free port by trying random ports higher than the initial port and lower than 65000. Never returns the initial port, always returns a random port to avoid probability of clashing if executed in parallel. The initial port is only used to set the lower boundary for searching.
     * @param initialPort Optionally provide a port to start with. If set to 0, the default value 8080 is used
     * @return A random port that is currently free
     */
    public static int findFreePort(int initialPort) {
        int portCandidate = 8080;
        int numberOfAttempts = 100;
        boolean foundFreePort = false;
        final Random random = new Random();

        if (initialPort > 0) {
            portCandidate = initialPort;
        }

        do {
            portCandidate += random.nextInt(100);
            foundFreePort = isFreePort(portCandidate);
            numberOfAttempts--;
        } while (!foundFreePort || numberOfAttempts <= 0 || portCandidate > 65000);

        if (foundFreePort) {
            return portCandidate;
        } else {
            throw new RuntimeException("No free port found!");
        }
    }

    private static boolean isFreePort(int portCandidate) {
        try ( Socket s = new Socket("localhost", portCandidate)) {
        } catch (IOException ex) {
            return true;
        }
        return false;
    }

}
