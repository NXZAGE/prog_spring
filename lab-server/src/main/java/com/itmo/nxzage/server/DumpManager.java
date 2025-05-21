package com.itmo.nxzage.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import com.itmo.nxzage.server.exceptions.DumpException;
import com.itmo.nxzage.server.exceptions.ReadException;

// TODO возможно очень хуево писать это через String и нужны буферы
// * лучше переписать это уже на nio

/**
 * Вспомогательный класс, позволяющий считывать и записывать данные в файл
 */
public final class DumpManager {
    private File file;

    public DumpManager(String filePath) {
        this.file = new File(filePath);
    }

    public String getPath() {
        return this.file.getAbsolutePath();
    }

    /**
     * Очищает файл
     */
    public void clear() {
        try (FileWriter writer = new FileWriter(this.file, false)) {
            writer.write("");
        } catch (IOException exc) {
            throw new DumpException("Unable to clear file", exc);
        }
    }

    /**
     * Записывает данные в файл
     * <p> Предыдущие данные перезаписываются </p>
     * 
     * @param data данные для записи
     * @throws DumpException если не удается записать данные
     */
    public void write(String data) {
        try (FileWriter writer = new FileWriter(this.file, false)) {
            writer.write(data);
        } catch (IOException exc) {
            throw new DumpException("Unable to write to the file", exc);
        }
    }

    /**
     * Читает целиком данные из файла
     * 
     * @return считанные данные
     * @throws ReadException если не удается считать данные
     */
    public String read() {
        String result = new String();
        final int OFFSET = 0;
        final int CHARBUF_SIZE = 256;
        try (var reader = new InputStreamReader(new FileInputStream(this.file))) {
            char[] buf = new char[CHARBUF_SIZE];
            int count = 0;
            while ((count = reader.read(buf)) != -1) {
                result += new String(buf, OFFSET, count);
            }
            return result;
        } catch (IOException exc) {
            throw new ReadException("Unable to read data", exc);
        }
    }
}
