/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
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
