/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uiowa.icts.safeseed.refseq;

import java.util.LinkedHashMap;

/**
 *
 * @author Ray
 *
 * Can ONLY handle ACGT, not N
 */
public class GenomeByteArray {
    private static final int A = 00;
    private static final int C = 01;
    private static final int G = 10;
    private static final int T = 11;

    public static void main(String[] arg) {
        byte[] b = new byte[5];
        b[0] = (byte) 0x0000;   // 00000000
        b[1] = (byte) 0x0009;   // 00001001
        b[2] = (byte) 0x0090;   // 10010000
        b[3] = (byte) 0x000F;   // 00001111
        b[4] = (byte) 0x00FF;   // 11111111
        /*for (int i = 0; i < b.length; i++) {
            System.out.println(byteToBits(b[i]));
        }*/
        System.out.println(byteArrayToBits(b));
        
        byte[] b2 = new byte[5];
        setBits(b2, 0, "00000000");
        setBits(b2, 1, "00001001");
        setBits(b2, 2, "10010000");
        setBits(b2, 3, "00001111");
        setBits(b2, 4, "11111111");
        /*for (int i = 0; i < b.length; i++) {
            System.out.println(byteToBits(b2[i]));
        }*/
        System.out.println(byteArrayToBits(b2));
        
    }

    private static StringBuffer dnaToBinary(String seq) throws Exception{
        StringBuffer sb = new StringBuffer(seq.length());
        for(int i=0; i<seq.length(); i++){
            char c = seq.charAt(i);
            if(c == 'A'){
                sb.append(A);
            } else if(c== 'C'){
                sb.append(C);
            } else if(c == 'G'){
                sb.append(G);
            } else if(c == 'T'){
                sb.append(T);
            } else {
                throw new Exception("The following character is not a recognized nucleotide: " + c + " at position " + i);
            }
        }
        return sb;
    }

    private static byte[] rotateLeft(byte[] in, int len, int step) {
        int numOfBytes = (len - 1) / 8 + 1;
        byte[] out = new byte[numOfBytes];
        for (int i = 0; i < len; i++) {
            int val = getBit(in, (i + step) % len);
            setBit(out, i, val);
        }
        return out;
    }

    private static int getBit(byte[] data, int pos) {
        int posByte = pos / 8;
        int posBit = pos % 8;
        byte valByte = data[posByte];
        int valInt = valByte >> (8 - (posBit + 1)) & 0x0001;
        return valInt;
    }

    private static void setBit(byte[] data, int pos, int val) {
        int posByte = pos / 8;
        int posBit = pos % 8;
        byte oldByte = data[posByte];
        oldByte = (byte) (((0xFF7F >> posBit) & oldByte) & 0x00FF);
        byte newByte = (byte) ((val << (8 - (posBit + 1))) | oldByte);
        data[posByte] = newByte;
    }

    private static void setBits(byte[] data, int pos, String val) {
        byte newByte = (byte)0x0000;
        for(int i=0; i<8; i++){
            byte oldByte = data[pos];
            oldByte = (byte) (((0xFF7F >> i) & oldByte) & 0x00FF);
            newByte = (byte) ((Integer.parseInt(val.substring(i, i+1)) << (8 - (i + 1 ))) | oldByte);
            data[pos] = newByte;
        }
    }

    private static String byteToBits(byte b) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < 8; i++) {
            buf.append((int) (b >> (8 - (i + 1)) & 0x0001));
        }
        return buf.toString();
    }

    private static String byteArrayToBits(byte[] b) {
        StringBuffer buf = new StringBuffer();
        for (int j = 0; j < b.length; j++) {
            byte y = b[j];
            for (int i = 0; i < 8; i++) {
                buf.append((int) (y >> (8 - (i + 1)) & 0x0001));
            }
        }
        return buf.toString();
    }
}
