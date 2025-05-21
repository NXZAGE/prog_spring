package com.itmo.nxzage.client.io;

/**
 * Интерфейс для унификации источника ввода
 */
public interface InputSource {
    /**
     * @return очередную строку или null
     */
    String nextLine();
    boolean hasNextLine();
    boolean isInteractive();
    /**
     * @return краткое описание источника
     */
    void close();
    String info();
}
