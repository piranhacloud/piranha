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
package cloud.piranha.feature.pid;

import cloud.piranha.feature.impl.DefaultFeature;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.System.Logger.Level.WARNING;

/**
 * The PID feature.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class PidFeature extends DefaultFeature implements Runnable {

    /**
     * Stores the logger.
     */
    private static final System.Logger LOGGER = System.getLogger(PidFeature.class.getName());

    /**
     * Stores the PID.
     */
    private Long pid;

    /**
     * Stores the stop flag.
     */
    private boolean stop = false;
    
    /**
     * Stores the thread.
     */
    private Thread thread;

    /**
     * Get the PID.
     *
     * @return the PID.
     */
    public Long getPid() {
        return pid;
    }
    
    /**
     * Get the thread.
     * 
     * @return the thread.
     */
    public Thread getThread() {
        return thread;
    }

    @Override
    public void run() {
        while (!stop) {
            if (pid != null) {
                File pidFile = new File("tmp", "piranha.pid");
                if (!pidFile.exists()) {
                    stop();
                }
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Set the PID.
     *
     * @param pid the PID.
     */
    public void setPid(Long pid) {
        this.pid = pid;
    }

    @Override
    public void start() {
        stop = false;
        thread = new Thread(this);
        thread.start();

        File pidFile = new File("tmp", "piranha.pid");
        if (!pidFile.getParentFile().exists() && !pidFile.getParentFile().mkdirs()) {
            LOGGER.log(WARNING, "Unable to create tmp directory for PID file");
        }
        try (PrintWriter writer = new PrintWriter(new FileWriter(pidFile))) {
            writer.println(pid);
            writer.flush();
        } catch (IOException ioe) {
            LOGGER.log(WARNING, "Unable to write PID file", ioe);
        }
    }

    @Override
    public void stop() {
        stop = true;
        thread = null;
        featureManager.stop();
    }
}
