/*
 * Copyright (c) 2002-2019 Manorrock.com. All Rights Reserved.
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
import java.io.OutputStream;

/**
 * The ServletOutputStream API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public abstract class ServletOutputStream extends OutputStream {

    /**
     * Constructor.
     */
    protected ServletOutputStream() {
    }

    /**
     * Is ready for output.
     *
     * @return true if it is, false otherwise.
     */
    public abstract boolean isReady();

    /**
     * Print a string.
     *
     * @param string the string.
     * @throws IOException when an I/O error occurs.
     */
    public void print(String string) throws IOException {
        if (string == null) {
            string = "null";
        }
        int len = string.length();
        for (int i = 0; i < len; i++) {
            char c = string.charAt(i);
            write(c);
        }
    }

    /**
     * Print a boolean.
     *
     * @param bool the boolean.
     * @throws IOException when an I/O error occurs.
     */
    public void print(boolean bool) throws IOException {
        String msg;
        if (bool) {
            msg = "true";
        } else {
            msg = "false";
        }
        print(msg);
    }

    /**
     * Print a character.
     *
     * @param character the character.
     * @throws IOException when an I/O error occurs.
     */
    public void print(char character) throws IOException {
        print(String.valueOf(character));
    }

    /**
     * Print an integer.
     *
     * @param integer the integer.
     * @throws IOException when an I/O error occurs.
     */
    public void print(int integer) throws IOException {
        print(String.valueOf(integer));
    }

    /**
     * Print the long.
     *
     * @param l the long.
     * @throws IOException when an I/O error occurs.
     */
    public void print(long l) throws IOException {
        print(String.valueOf(l));
    }

    /**
     * Print the float.
     *
     * @param f the float.
     * @throws IOException when an I/O error occurs.
     */
    public void print(float f) throws IOException {
        print(String.valueOf(f));
    }

    /**
     * Print the double.
     *
     * @param d the double.
     * @throws IOException when an I/O error occurs.
     */
    public void print(double d) throws IOException {
        print(String.valueOf(d));
    }

    /**
     * Print a carriage return and line feed.
     *
     * @throws IOException when an I/O error occurs.
     */
    public void println() throws IOException {
        print("\r\n");
    }

    /**
     * Print string with linefeed.
     *
     * @param string the string.
     * @throws IOException when an I/O error occurs.
     */
    public void println(String string) throws IOException {
        print(string);
        println();
    }

    /**
     * Print boolean with linefeed.
     *
     * @param bool the boolean.
     * @throws IOException when an I/O error occurs.
     */
    public void println(boolean bool) throws IOException {
        print(bool);
        println();
    }

    /**
     * Print character with linefeed.
     *
     * @param character the character.
     * @throws IOException when an I/O error occurs.
     */
    public void println(char character) throws IOException {
        print(character);
        println();
    }

    /**
     * Print integer with linefeed.
     *
     * @param integer the integer.
     * @throws IOException when an I/O error occurs.
     */
    public void println(int integer) throws IOException {
        print(integer);
        println();
    }

    /**
     * Print long with linefeed.
     *
     * @param l the long.
     * @throws IOException when an I/O error occurs.
     */
    public void println(long l) throws IOException {
        print(l);
        println();
    }

    /**
     * Print float with linefeed.
     *
     * @param f the float.
     * @throws IOException when an I/O error occurs.
     */
    public void println(float f) throws IOException {
        print(f);
        println();
    }

    /**
     * Print double with linefeed.
     *
     * @param d the double.
     * @throws IOException when an I/O error occurs.
     */
    public void println(double d) throws IOException {
        print(d);
        println();
    }

    /**
     * Set the write listener.
     *
     * @param writeListener the write listener.
     */
    public abstract void setWriteListener(WriteListener writeListener);
}
