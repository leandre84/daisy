/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.mic16.prj.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * Helper utility for facilitating commonly used functions related to files
 * @author leandros
 */
public class FileUtil {
    
    /**
     * Closes any object that implements the Closeable interface, properly handling null references
     * @param closeables to be closed
     */
    public static void safeClose(Closeable...closeables) {
        for (Closeable c : closeables) {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (IOException ignore) {
            }
        }
    }
    
}
