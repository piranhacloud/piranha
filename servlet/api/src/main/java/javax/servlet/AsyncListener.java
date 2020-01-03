/*
 * Copyright (c) 2002-2020 Manorrock.com. All Rights Reserved.
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
package javax.servlet;

import java.io.IOException;
import java.util.EventListener;

/**
 * The AsyncListener API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface AsyncListener extends EventListener {

    /**
     * Handle the on complete event.
     *
     * @param event the event.
     * @throws IOException when an I/O error occurs.
     */
    public void onComplete(AsyncEvent event) throws IOException;

    /**
     * Handle the on error event.
     *
     * @param event the event.
     * @throws IOException when an I/O error occurs.
     */
    public void onError(AsyncEvent event) throws IOException;

    /**
     * Handle the on start async event.
     *
     * @param event the event.
     * @throws IOException when an I/O error occurs.
     */
    public void onStartAsync(AsyncEvent event) throws IOException;

    /**
     * Handle the on timeout event.
     *
     * @param event the event.
     * @throws IOException when an I/O error occurs.
     */
    public void onTimeout(AsyncEvent event) throws IOException;
}
