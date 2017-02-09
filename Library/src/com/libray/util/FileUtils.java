package com.libray.util;

import android.content.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * @author t77yq @2014-07-22.
 */
public final class FileUtils {

    private FileUtils() {}

    public static String readFileFromAssets(Context context, String filename) {
        try {
            InputStream input = context.getAssets().open(filename);
            Writer writer = new StringWriter();
            try {
                Reader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
                char[] buffer = new char[1024];
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                input.close();
            }
            return writer.toString();
        } catch (IOException e) {
            return null;
        }
    }
}
