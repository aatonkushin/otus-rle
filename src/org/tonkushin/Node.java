package org.tonkushin;

public class Node {
    private final int freq;
    private final Node bit0;
    private final Node bit1;
    private byte symbol;

    // Терминальный узел
    public Node(byte symbol, int freq) {
        this.freq = freq;
        this.symbol = symbol;
        bit0=null;
        bit1=null;
    }

    // Внутренние узлы
    public Node(Node bit0, Node bit1) {
        this.freq = bit0.freq + bit1.freq;
        this.bit0 = bit0;
        this.bit1 = bit1;
    }
}
