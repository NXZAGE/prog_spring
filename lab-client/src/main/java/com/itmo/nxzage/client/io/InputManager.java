package com.itmo.nxzage.client.io;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.Stack;
import com.itmo.nxzage.client.exceptions.InfiniteReqursionException;
import com.itmo.nxzage.client.exceptions.InputSourceHoldConflictException;
import com.itmo.nxzage.client.exceptions.InvalidSourceHolderReleaseException;


/**
 * Класс, поддерживающий стек источников ввода
 * <p> Реализует полный интерфейс источника ввода </p>
 */
public final class InputManager implements InputSource {
    private LinkedList<InputSource> sources;
    private LinkedList<Object> sourceHolders;

    {
        sources = new LinkedList<InputSource>();
        sourceHolders = new LinkedList<Object>();
    }

    private boolean isSourceFree() {
        return sourceHolders.isEmpty();
    }

    @SuppressWarnings("unused")
    private void freeSource() {
        sourceHolders.clear();
    }

    /**
     * @throws {@link InputSourceHoldConflictException} при попытке переключиться в режиме удержания
     * @throws {@link EmptyStackException} если в стеке нет источников
     */
    private void switchToPreviousSource() {
        if (!isSourceFree()) {
            throw new InputSourceHoldConflictException();
        }
        closeSource();
    }

    @Override
    /**
     * @throws {@link InputSourceHoldConflictException} при попытке переключиться в режиме удержания
     */
    public String nextLine() {
        if (sources.getLast().hasNextLine()) {
            return sources.getLast().nextLine();
        }
        switchToPreviousSource();
        return nextLine();
    }

    @Override
    public boolean hasNextLine() {
        if (sources.isEmpty()) {
            return false;
        }
        return sources.getLast().hasNextLine();
    }

    @Override
    public boolean isInteractive() {
        return sources.getLast().isInteractive();
    }

    @Override
    public String info() {
        return String.format("InputManger");
    }

    @Override
    public void close() {
        while(!sources.isEmpty()) {
            closeSource();
        }
    }

    /**
     * Добавляет очередной источник в стек
     * @param source новый источник
     * @throws {@link InputSourceHoldConflictException} при попытке добавить источник в режиме удержания
     */
    public void pushSource(InputSource source) {
        if (!isSourceFree()) {
            throw new InputSourceHoldConflictException();
        }
        checkReqursion(source);
        sources.add(source);
    }

    /**
     * Закрывает верхний источний и удаляет его из стека
     * @throws {@link EmptyStackException} если в стеке нет источников
     */
    public void closeSource() {
        sources.removeLast().close();
    }

    public void checkReqursion(InputSource source) {
        for (InputSource so : sources) {
            if (source.equals(so)) {
                throw new InfiniteReqursionException();
            }
        }
    }

    /**
     * Проверка на существование источников
     * @return true, если существует хотя бы один источник, false иначе
     */
    public boolean empty() {
        return sources.isEmpty();
    }

    /**
     * Удерживает текущий источник
     * <p> Запрещает переключения </p>
     * @param holder объект, требующий удержания
     */
    public void holdSource(Object holder) {
        sourceHolders.add(holder);
    }

    /**
     * Перестает удерживать источник
     * @param holder объект, освобождающий источник
     * @throws {@link InvalidSourceHolderReleaseException} если holder не на вершине стека
     */
    public void releaseSource(Object holder) {
        if (sourceHolders.getLast() == holder) {
            sourceHolders.pop();
            return;
        }
        throw new InvalidSourceHolderReleaseException();
    }
}
