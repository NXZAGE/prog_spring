package com.itmo.nxzage.client.io;

import java.util.EmptyStackException;
import java.util.Stack;
import com.itmo.nxzage.client.exceptions.InputSourceHoldConflictException;
import com.itmo.nxzage.client.exceptions.InvalidSourceHolderReleaseException;


/**
 * Класс, поддерживающий стек источников ввода
 * <p> Реализует полный интерфейс источника ввода </p>
 */
public final class InputManager implements InputSource {
    private Stack<InputSource> sources;
    private Stack<Object> sourceHolders;

    {
        sources = new Stack<InputSource>();
        sourceHolders = new Stack<Object>();
    }

    private boolean isSourceFree() {
        return sourceHolders.empty();
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
        if (sources.peek().hasNextLine()) {
            return sources.peek().nextLine();
        }
        switchToPreviousSource();
        return nextLine();
    }

    @Override
    public boolean hasNextLine() {
        if (sources.empty()) {
            return false;
        }
        return sources.peek().hasNextLine();
    }

    @Override
    public boolean isInteractive() {
        return sources.peek().isInteractive();
    }

    @Override
    public String info() {
        return String.format("InputManger");
    }

    @Override
    public void close() {
        while(!sources.empty()) {
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
        sources.push(source);
    }

    /**
     * Закрывает верхний источний и удаляет его из стека
     * @throws {@link EmptyStackException} если в стеке нет источников
     */
    public void closeSource() {
        sources.pop().close();
    }

    /**
     * Проверка на существование источников
     * @return true, если существует хотя бы один источник, false иначе
     */
    public boolean empty() {
        return sources.empty();
    }

    /**
     * Удерживает текущий источник
     * <p> Запрещает переключения </p>
     * @param holder объект, требующий удержания
     */
    public void holdSource(Object holder) {
        sourceHolders.push(holder);
    }

    /**
     * Перестает удерживать источник
     * @param holder объект, освобождающий источник
     * @throws {@link InvalidSourceHolderReleaseException} если holder не на вершине стека
     */
    public void releaseSource(Object holder) {
        if (sourceHolders.peek() == holder) {
            sourceHolders.pop();
            return;
        }
        throw new InvalidSourceHolderReleaseException();
    }
}
