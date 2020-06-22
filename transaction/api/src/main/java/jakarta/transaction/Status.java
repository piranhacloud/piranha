/*
 * Copyright (c) 2002-2020 Manorrock.com. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 *   1. Redistributions of source code must retain the above copyright notice, 
 *      this list of conditions and the following disclaimer.
 *   2. Redistributions in binary form must reproduce the above copyright notice,
 *      this list of conditions and the following disclaimer in the documentation
 *      and/or other materials provided with the distribution.
 *   3. Neither the name of the copyright holder nor the names of its 
 *      contributors may be used to endorse or promote products derived from this
 *      software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jakarta.transaction;

/**
 * The Status API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface Status {

    /**
     * Defines the active status.
     */
    public static final int STATUS_ACTIVE = 0;

    /**
     * Defines the committed status.
     */
    public static final int STATUS_COMMITTED = 3;

    /**
     * Defines the committing status.
     */
    public static final int STATUS_COMMITTING = 8;

    /**
     * Defines the marked for rollback status.
     */
    public static final int STATUS_MARKED_ROLLBACK = 1;

    /**
     * Defines the no transaction status.
     */
    public static final int STATUS_NO_TRANSACTION = 6;

    /**
     * Defines the prepared status.
     */
    public static final int STATUS_PREPARED = 2;

    /**
     * Defines the preparing status.
     */
    public static final int STATUS_PREPARING = 7;

    /**
     * Defines the rolled back status.
     */
    public static final int STATUS_ROLLEDBACK = 4;

    /**
     * Defines the rolling back status.
     */
    public static final int STATUS_ROLLING_BACK = 9;
    
    /**
     * Defines the unknown status.
     */
    public static final int STATUS_UNKNOWN = 5;
}
