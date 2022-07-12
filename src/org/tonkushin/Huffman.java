package org.tonkushin;

/**
 * Сжатие кодом Хаффмана
 */
public class Huffman {
    public byte[] compress(byte[] data) {
        byte[] arch = compressBytes(data);

        return arch;
    }

    public byte[] decompress(byte[] arch) {

        return decompressBytes(arch);
    }

    private byte[] decompressBytes(byte[] arch) {
        // Parse Header
        int dataLength = unsignedByte(arch[0]) | (unsignedByte(arch[1]) << 8) | (unsignedByte(arch[2]) << 16) | (unsignedByte(arch[3]) << 24);

        int[] freqs = new int[256];
        int count = arch[4];
        if (count == 0) {
            count = 256;
        }
        for (int i = 0; i < count; i++) {
            byte symbol = arch[5 + i * 2];
            freqs[unsignedByte(symbol)] = arch[5 + i * 2 + 1];
        }

        int startIndex = 4 + 1 + 2 * count;

        Node root = createHuffmanTree(freqs);
        byte[] data = decompressBytes(arch, startIndex, dataLength, root);

        return data;
    }

    private byte[] decompressBytes(byte[] arch, int startIndex, int dataLength, Node root) {
        int size = 0;
        Node curr = root;

        byte[] data = new byte[dataLength];

        for (int i = startIndex; i < arch.length; i++) {
            for (int bit = 1; bit <= 128; bit <<= 1) {
                if ((arch[i] & bit) == 0) {
                    curr = curr.getBit0();
                } else {
                    curr = curr.getBit1();
                }

                if (curr.getBit0() == null) {
                    if (size < dataLength) {
                        data[size++] = curr.getSymbol();
                    }

                    curr = root;
                }
            }
        }

        return data;
    }

    private byte[] compressBytes(byte[] data) {
        int[] freqs = calculateFreqs(data);                 // частотная таблица символов
        byte[] head = createHeader(data.length, freqs);     // заголовок сжатого файла
        Node root = createHuffmanTree(freqs);               // корневой узел дерева Хаффмана
        String[] codes = CreateHuffmanCode(root);           // массив с кодами Хаффмана
        byte[] bits = compress(data, codes);

        byte[] retVal = new byte[head.length + bits.length];

        for (int i = 0; i < head.length; i++) {
            retVal[i] = head[i];
        }

        for (int i = 0; i < bits.length; i++) {
            retVal[i + head.length] = bits[i];
        }

        return retVal;
    }

    private byte[] compress(byte[] data, String[] codes) {
        VectorArray<Byte> bits = new VectorArray<>(data.length);
        byte sum = 0;
        byte bit = 1;

        for (byte symbol : data) {                          // 66 = B
            for (char c : codes[unsignedByte(symbol)].toCharArray()) {    // "110"
                if (c == '1') {
                    sum |= bit;
                }

                if (bit == -128) {
                    bits.add(sum);
                    sum = 0;
                    bit = 1;
                } else {
                    bit <<= 1;
                }
            }
        }

        // остаток
        if (bit == -128 || bit > 1) {
            bits.add(sum);
        }

        byte[] retVal = new byte[bits.size()];
        for (int i = 0; i < bits.size(); i++) {
            retVal[i] = bits.get(i);
        }

        return retVal;
    }

    private String[] CreateHuffmanCode(Node root) {
        String[] codes = new String[256];
        Next(codes, root, "");
        return codes;
    }

    private void Next(String[] codes, Node node, String code) {
        if (node.getBit0() == null) {
            codes[unsignedByte(node.getSymbol())] = code;
        } else {
            Next(codes, node.getBit0(), code + "0");
            Next(codes, node.getBit1(), code + "1");
        }
    }

    // Создаёт дерево Хаффмана
    private Node createHuffmanTree(int[] freqs) {
        PriorityQueue<Node> pq = new PriorityQueue();

        for (int i = 0; i < 256; i++) {
            if (freqs[i] > 0) {
                pq.enqueue(freqs[i], new Node((byte) i, freqs[i]));
            }
        }

        while (pq.getSize() > 1) {
            Node bit0 = pq.dequeue();
            Node bit1 = pq.dequeue();
            Node parent = new Node(bit0, bit1);

            pq.enqueue(parent.getFreq(), parent);
        }

        return pq.dequeue();
    }

    // Создаёт заголовок файла
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
            freqs[unsignedByte(b)]++;
        }

        // --- нормализация
        int max = 0;
        for (int f : freqs) {
            if (f > max) {
                max = f;
            }
        }

        if (max > 127) {
            for (int i = 0; i < 127; i++) {
                if (freqs[i] > 0) {
                    freqs[i] = 1 + (freqs[i] * 127) / (max + 1);
                }
            }
        }

        return freqs;
    }

    private int unsignedByte(byte b) {
        return b & 0xFF;
    }
}