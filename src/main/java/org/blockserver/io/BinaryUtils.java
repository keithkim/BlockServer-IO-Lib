package org.blockserver.io;

public abstract class BinaryUtils{
	public final static boolean BIG_ENDIAN = false;
	public final static boolean LITTLE_ENDIAN = true;
	public static byte[] write(long x, int length, boolean endianness){
		byte[] result = new byte[length];
		for(int i = 0; i < length; i++){
			result[endianness == BIG_ENDIAN ? (length - 1 - i) : i] = (byte) (x & 0xFF);
			x >>= 8;
		}
		return result;
	}
	public static byte[] write(long x, int length){
		return write(x, length, BIG_ENDIAN);
	}
	public static long read(byte[] buffer, int start, int length, boolean endianness){
		long x = 0;
		for(int i = 0; i < length; i++){
			x <<= 8;
			x |= buffer[endianness == BIG_ENDIAN ? (start + i) : (start + length - 1 - i)];
		}
		return x;
	}
	public static long read(byte[] buffer, int start, int length){
		return read(buffer, start, length, BIG_ENDIAN);
	}
	public static long read(byte[] buffer, boolean endianness){
		return read(buffer, 0, buffer.length, endianness);
	}
	public static long read(byte[] buffer){
		return read(buffer, 0, buffer.length, BIG_ENDIAN);
	}
}
