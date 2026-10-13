package com.vezh.lab.core.log;

import java.io.PrintStream;

/**
 * Print stream, that masks passwords before writing
 * Used for RestAssured logs, because they are written directly to System.out, not through logback
 */
public class MaskingPrintStream extends PrintStream {

    public MaskingPrintStream(PrintStream out) {
        super(out, true);
    }

    /**
     * println(String) and println(Object) also go through this method
     */
    @Override
    public void print(String s) {
        super.print(LogMasker.mask(s));
    }
}
