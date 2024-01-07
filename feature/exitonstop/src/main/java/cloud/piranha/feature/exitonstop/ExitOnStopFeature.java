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
package cloud.piranha.feature.exitonstop;

import cloud.piranha.feature.impl.DefaultFeature;

/**
 * The Exit on Stop feature.
 * 
 * <p>
 *  The Exit on Stop feature will exit the JVM after all the features have 
 *  asked to be stopped. It waits for a predefined amount of time before it
 *  calls System.exit.
 * </p>
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class ExitOnStopFeature extends DefaultFeature {
    
    /**
     * Stores the exiting flag.
     */
    private boolean exiting = false;
    
    /**
     * Are we in the process of exiting?
     * 
     * @return true if we are, false otherwise.
     */
    public boolean isExiting() {
        return exiting;
    }

    @Override
    public void stop() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                exiting = true;
                try {
                    Thread.sleep(2000);
                } catch(InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
                System.exit(0);
            }
        };
        thread.start();
    }
}
