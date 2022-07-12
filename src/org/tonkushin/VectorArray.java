package org.tonkushin;

public class VectorArray<T> implements IArray<T> {
    private T[] array;
    private final int vector;
    private int size = 0;

    @SuppressWarnings("unchecked")
    public VectorArray(int vector) {
        this.vector = vector;
        this.array = (T[]) new Object[vector];
    }

    @Override
    public T get(int index) {
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException();
        }

        return this.array[index];
    }

    @Override
    public void add(T item) {
        if (size() == this.array.length) {
            resize(vector);
        }

        this.array[size] = item;
        size++;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void add(T item, int index) {
        if (size() == this.array.length) {
            //  Если массив нужно расширить
            T[] newArray = (T[]) new Object[size() + vector];

            if (!isEmpty()) {
                System.arraycopy(this.array, 0, newArray, 0, index);
            }

            // Устанавливаем элемент в указанном индексе
            newArray[index] = item;

            // Копируем от индекса
            System.arraycopy(this.array, index, newArray, index + 1, size() - index);

            array = newArray;
        } else {
            // в рамках существующего массива
            if (size - index >= 0) {
                System.arraycopy(this.array, index, this.array, index + 1, size - index);
            }

            array[index] = item;
        }

        size++;
    }

    @Override
    public T remove(int index) {
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException();
        }

        T itemToRemove = this.array[index];

        int length = size - index - 1;
        if (length >= 0) {
            System.arraycopy(this.array, index + 1, this.array, index, length);
        }

        size--;

        this.array[size] = null;

        if (this.array.length > size() + vector) {
            resize(0);
        }

        return itemToRemove;
    }

    @Override
    public int size() {
        return size;
    }

    @SuppressWarnings("unchecked")
    private void resize(int v) {
        T[] newArray = (T[]) new Object[size() + v];
        if (!isEmpty()) {
            System.arraycopy(this.array, 0, newArray, 0, size());
        }

        this.array = newArray;
    }

    @SuppressWarnings("unchecked")
    public void clear() {
        for (int i = 0; i < size(); i++)
            this.array[i] = null;

        this.array = (T[]) new Object[0];
        this.size = 0;
    }

    @Override
    public T[] toArray() {
        return array;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(array[i]).append(";");
        }
        sb.append("]");
        return sb.toString();
    }
}
