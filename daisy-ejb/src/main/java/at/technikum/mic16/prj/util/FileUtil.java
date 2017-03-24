/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.mic16.prj.util;

import java.io.Closeable;
import java.io.IOException;

/**
 *
 * @author leandros
 */
public class FileUtil {
    
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
