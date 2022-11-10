package com.caveexp.util.bjson;

import com.badlogic.gdx.Gdx;
import com.caveexp.Main;
import com.caveexp.game.Game;
import com.caveexp.gui.screens.ErrorScreen;
import com.caveexp.util.ByteArray;
import com.caveexp.util.bjson.BJSONInputOutput;
import com.caveexp.util.bjson.ObjectElement;
import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class BJSONFile {
    public static void write(File file, ObjectElement element) {
        try {
            Gdx.files.absolute(file.getAbsolutePath()).writeBytes(gzipCompress(BJSONInputOutput.write(element)), false);
        }
        catch (Exception e) {
            Main.screenStack.push(new ErrorScreen(e));
        }
    }
    public static ObjectElement read(File file) {
        try {
            return BJSONInputOutput.readObject(new ByteArray(gzipUncompress(Gdx.files.absolute(file.getAbsolutePath()).readBytes())));
        }
        catch (Exception e) {
            Main.screenStack.push(new ErrorScreen(e));
        }
        return null;
    }
    private static byte[] gzipCompress(byte[] uncompressed) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPOutputStream out = new GZIPOutputStream(baos);
        out.write(uncompressed);
        out.finish();
        out.close();
        return baos.toByteArray();
    }
    private static byte[] gzipUncompress(byte[] compressed) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(compressed);
        GZIPInputStream in = new GZIPInputStream(bais);
        byte[] buffer = new byte[1024];
        int bytesRead;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while ((bytesRead = in.read(buffer)) > 0) {
            baos.write(buffer, 0, bytesRead);
        }
        return baos.toByteArray();
    }
}
