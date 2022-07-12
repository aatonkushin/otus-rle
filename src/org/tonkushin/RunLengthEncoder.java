package org.tonkushin;

public class RunLengthEncoder {
    public static byte[] encode(byte[] input) {
        VectorArray<Byte> vectorArray = new VectorArray<>(input.length / 2);

        for (int i=0; i<input.length; i++){
            short count = 1;

            while (i < input.length-1 && input[i] == input[i+1]) {
                count++;
                i++;

                if (count == 255) {
                    vectorArray.add(input[i]);
                    vectorArray.add((byte) count);

                    count = 0;
                }
            }

            vectorArray.add(input[i]);
            vectorArray.add((byte) count);
        }

        byte[] retVal = new byte[vectorArray.size()];
        for (int i = 0; i < vectorArray.size(); i++) {
            retVal[i] = vectorArray.get(i);
        }

        return retVal;
    }

    public static byte[] decode(byte[] input) {
        VectorArray<Byte> vectorArray = new VectorArray<>(input.length);

        for (int i = 0; i < input.length; i += 2) {
            for (int j = 0; j < input[i + 1]; j++) {
                vectorArray.add(input[i]);
            }
        }

        byte[] retVal = new byte[vectorArray.size()];
        for (int i = 0; i < vectorArray.size(); i++) {
            retVal[i] = vectorArray.get(i);
        }

        return retVal;
    }
}
