/*****************************************************************************
 *                                                                           *
 *  This file is part of the BeanShell Java Scripting distribution.          *
 *  Documentation and updates may be found at http://www.beanshell.org/      *
 *                                                                           *
 *  Sun Public License Notice:                                               *
 *                                                                           *
 *  The contents of this file are subject to the Sun Public License Version  *
 *  1.0 (the "License"); you may not use this file except in compliance with *
 *  the License. A copy of the License is available at http://www.sun.com    *
 *                                                                           *
 *  The Original Code is BeanShell. The Initial Developer of the Original    *
 *  Code is Pat Niemeyer. Portions created by Pat Niemeyer are Copyright     *
 *  (C) 2000.  All Rights Reserved.                                          *
 *                                                                           *
 *  GNU Public License Notice:                                               *
 *                                                                           *
 *  Alternatively, the contents of this file may be used under the terms of  *
 *  the GNU Lesser General Public License (the "LGPL"), in which case the    *
 *  provisions of LGPL are applicable instead of those above. If you wish to *
 *  allow use of your version of this file only under the  terms of the LGPL *
 *  and not to allow others to use your version of this file under the SPL,  *
 *  indicate your decision by deleting the provisions above and replace      *
 *  them with the notice and other provisions required by the LGPL.  If you  *
 *  do not delete the provisions above, a recipient may use your version of  *
 *  this file under either the SPL or the LGPL.                              *
 *                                                                           *
 *  Patrick Niemeyer (pat@pat.net)                                           *
 *  Author of Learning Java, O'Reilly & Associates                           *
 *  http://www.pat.net/~pat/                                                 *
 *                                                                           *
 *****************************************************************************/

package org.simbrain.console;

import java.io.FilterReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * This is a quick hack to turn empty lines entered interactively on the command
 * line into ';\n' empty lines for the interpreter. It's just more pleasant to
 * be able to hit return on an empty line and see the prompt reappear.
 * <p>
 * This is *not* used when text is sourced from a file non-interactively.
 */
class CommandLineReader extends FilterReader {

    private static final int NORMAL = 0, LAST_CHAR_NL = 1, SENT_SEMI = 2;

    private int state = LAST_CHAR_NL;

    public CommandLineReader(Reader in) {
        super(in);
    }

    public int read() throws IOException {
        int b;

        if (state == SENT_SEMI) {
            state = LAST_CHAR_NL;
            return '\n';
        }

        // skip CR
        while ((b = in.read()) == '\r')
            ;

        if (b == '\n')
            if (state == LAST_CHAR_NL) {
                b = ';';
                state = SENT_SEMI;
            } else
                state = LAST_CHAR_NL;
        else
            state = NORMAL;

        return b;
    }

    /**
     * This is a degenerate implementation. I don't know how to keep this from
     * blocking if we try to read more than one char... There is no available()
     * for Readers ??
     *
     * @param buff
     * @param off
     * @param len
     * @return
     * @throws IOException
     */
    public int read(char buff[], int off, int len) throws IOException {
        int b = read();
        if (b == -1)
            return -1; // EOF, not zero read apparently
        else {
            buff[off] = (char) b;
            return 1;
        }
    }

    // Test it
    public static void main(String[] args) throws Exception {
        Reader in = new CommandLineReader(new InputStreamReader(System.in));
        while (true)
            System.out.println(in.read());

    }
}
