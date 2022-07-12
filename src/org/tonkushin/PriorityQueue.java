package org.tonkushin;

import java.util.LinkedList;
import java.util.Queue;
import java.util.SortedMap;
import java.util.TreeMap;

public class PriorityQueue<T> {
    int size;   // Размер очереди

    SortedMap<Integer, Queue<T>> storage;

    public PriorityQueue() {
        size = 0;
        storage = new TreeMap<Integer, Queue<T>>();
    }

    public int getSize() {
        return size;
    }

    // Помещает элемент в очередь
    public void enqueue(int priority, T item) {
        if (!storage.containsKey(priority)) {
            storage.put(priority, new LinkedList<>());
        }

        storage.get(priority).offer(item);
        size++;
    }

    // Возвращает элемент из очереди
    public T dequeue(){
        if (size == 0) {
            throw new RuntimeException("Queue is empty");
        }
        size--;

        for (Queue<T> q: storage.values()) {
            if (q.size() > 0) {
                return q.poll();
            }
        }

        throw new RuntimeException("Queue error");
    }
}
