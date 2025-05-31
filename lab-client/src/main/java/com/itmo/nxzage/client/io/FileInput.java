package com.itmo.nxzage.client.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.InetSocketAddress;
import java.util.Scanner;
import com.itmo.nxzage.common.util.data.Person;

public final class FileInput implements InputSource {
    private String filename;
    private Scanner scanner;

    public FileInput(String filename) throws FileNotFoundException {
        this.filename = filename;
        scanner = new Scanner(new File(filename));
        // TODO check exceptions
    }

    @Override
    public String nextLine() {
        return scanner.nextLine();
    }

    // ! slkfjlsdjfl
    @Override
    public boolean hasNextLine() {
        return scanner.hasNextLine();
        // TODO посмотреть, как работает некст лайн
    }

    @Override
    public boolean isInteractive() {
        return false;
    }

    @Override
    public void close() {
        scanner.close();
        // TODO проверить ошибки закрытия
    }

    @Override
    public String info() {
        return String.format("%s file input", filename);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof FileInput))
            return false;

        FileInput otherSource = (FileInput) other;

        return this.info().equals(otherSource.info());
    }
}
