package ru.mail.polis.homework.collections.structure;

import java.util.EmptyStackException;
import java.util.Stack;

/**
 * Задание оценивается в 2 балла.
 * Надо расширить стандартный класс Stack методом getMaxValue().
 * Этот метод должен возвращать какое максимальное значение сейчас
 * храниться в Stack. Отрабатывать метод должен за О(1).
 */
public class MaxStack extends Stack<Integer> {

    private final Stack<Integer> stackMaxValues;

    public MaxStack() {
        super();
        stackMaxValues = new Stack<>();
    }

    @Override
    public Integer push(Integer item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (stackMaxValues.isEmpty() || item >= stackMaxValues.lastElement()) {
            stackMaxValues.push(item);
        }
        return super.push(item);
    }

    @Override
    public synchronized Integer pop() {
        if (stackMaxValues.isEmpty()) {
            throw new EmptyStackException();
        }
        if (super.peek().equals(stackMaxValues.lastElement())) {
            stackMaxValues.pop();
            return super.pop();
        }
        return super.pop();
    }

    public Integer getMaxValue() {
        if (super.isEmpty()) {
            throw new EmptyStackException();
        }
        return stackMaxValues.lastElement();
    }

}
