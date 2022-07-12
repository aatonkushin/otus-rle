package org.tonkushin;

/**
 * Интерфейс для динамического массива
 * @param <T>
 */
public interface IArray<T> {
    T get(int index);

    void add(T item);

    void add(T item, int index);

    T remove(int index);

    int size();

    default boolean isEmpty() {
        return size() == 0;
    }

    default void checkBounds(int index) {
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException();
        }
    }

    void clear();

    T[] toArray();
}
