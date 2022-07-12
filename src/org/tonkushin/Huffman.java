package org.tonkushin;

/**
 * Сжатие кодом Хаффмана
 */
public class Huffman {
    public byte[] compress(byte[] data) {
        byte[] arch = compressBytes(data);

        return arch;
    }

    public byte[] decompress(byte[] data) {

        return data;
    }

    private byte[] compressBytes(byte[] data) {
        int[] freqs = calculateFreqs(data);                 // частотная таблица символов
        byte[] head = createHeader(data.length, freqs);     // заголовок сжатого файла
        Node root = createHuffmanTree(freqs);               // дерево Хаффмана

        return data;
    }

    private Node createHuffmanTree(int[] freqs) {
        PriorityQueue<Node> pq = new PriorityQueue();

        for (int i = 0; i < 256; i++) {
            if (freqs[i] > 0) {
                pq.enqueue(freqs[i], new Node((byte) i, freqs[i]));
            }
        }

        return pq.dequeue();
    }

    private byte[] createHeader(int dataLength, int[] freqs) {
        VectorArray<Byte> head = new VectorArray<>(4 + freqs.length);

        // Размер файла
        head.add((byte) (dataLength & 255));
        head.add((byte) ((dataLength >> 8) & 255));
        head.add((byte) ((dataLength >> 16) & 255));
        head.add((byte) ((dataLength >> 24) & 255));

        // Размещаем частотную таблицу
        int count = 0;  // кол-во элементов в таблице
        for (int i = 0; i < freqs.length; i++) {
            if (freqs[i] > 0) {
                count++;
            }
        }
        head.add((byte) count);

        // Сама таблица
        for (int i = 0; i < freqs.length; i++) {
            if (freqs[i] > 0) {
                head.add((byte) i);
                head.add((byte) freqs[i]);
            }
        }

        byte[] retVal = new byte[head.size()];
        for (int i = 0; i < head.size(); i++) {
            retVal[i] = head.get(i);
        }

        return retVal;
    }

    private int[] calculateFreqs(byte[] data) {
        int[] freqs = new int[256];

        for (byte b : data) {
            freqs[b]++;
        }

        return freqs;
    }
}