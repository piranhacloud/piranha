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

/**
 * The Platform extension module.
 *
 * <p>
 *  This module of modules that delivers the extensions for the Jakarta EE 
 *  Platform, as in:
 * </p>
 * <ul>
 *  <li>Annotation Scanning</li>
 *  <li>Eclipse Parsson (JSON)</li>
 *  <li>Eclipse Yasson (JSON-B)</li>
 *  <li>Glassfish Expressly (EL)</li>
 *  <li>Glassfish Jersey (REST)</li>
 *  <li>Naming (JNDI)</li>
 *  <li>ServletContainerInitializer</li>
 * </ul>
 */
module cloud.piranha.extension.platform {
    
    exports cloud.piranha.extension.platform;
    opens cloud.piranha.extension.platform;
    requires cloud.piranha.core.api;
    requires cloud.piranha.extension.annotationscan;
    requires cloud.piranha.extension.naming;
    requires cloud.piranha.extension.scinitializer;
    requires jakarta.ws.rs;
    requires org.eclipse.parsson;
    requires org.eclipse.yasson;
    requires org.glassfish.expressly;
}
