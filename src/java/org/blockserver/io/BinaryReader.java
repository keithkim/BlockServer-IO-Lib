package org.blockserver.io;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class BinaryReader implements Closeable{
	protected InputStream is;
	protected boolean endianness;

	public BinaryReader(InputStream is){
		this(is, BinaryUtils.BIG_ENDIAN);
	}
	public BinaryReader(InputStream is, boolean endianness){
		this.is = is;
		this.endianness = endianness;
	}

	@Override
	public void close() throws IOException{
		is.close();
	}

	public String readString() throws IOException{
		falloc(2);
		return readString(2);
	}
	public String readString(int lenLen) throws IOException{
		falloc(lenLen);
		int length = (int) readNat(lenLen);
		falloc(length);
		return new String(read(length));
	}
	public byte readByte() throws IOException{
		falloc(1);
		return (byte) is.read();
	}
	public short readShort() throws IOException{
		falloc(2);
		return (short) readNat(2);
	}
	public int readTriad() throws IOException{
		falloc(3);
		return (int) readNat(3);
	}
	public int readInt() throws IOException{
		falloc(4);
		return (int) readNat(4);
	}
	public long readLong() throws IOException{
		falloc(8);
		return readNat(8);
	}
	public float readFloat() throws IOException{
		falloc(4);
		ByteBuffer bb = ByteBuffer.wrap(read(4));
		return bb.getFloat();
	}
	public double readDouble() throws IOException{
		falloc(8);
		ByteBuffer bb = ByteBuffer.wrap(read(8));
		return bb.getDouble();
	}
	public byte[] read(int length) throws IOException{
		falloc(length);
		byte[] buffer = new byte[length];
		is.read(buffer, 0, length);
		return buffer;
	}
	public long readNat(int length) throws IOException{
		falloc(length);
		return BinaryUtils.read(read(length), 0, length, endianness);
	}

	@SuppressWarnings("unchecked")
	public <T> T readType(Class<T> clazz, Object[] args) throws IOException{
		if(clazz.equals(String.class)){
			if(args.length > 0){
				Object arg = args[0];
				if(arg instanceof Integer){
					return (T) readString((int) arg);
				}
			}
			return (T) readString();
		}
		else if(clazz.equals(Byte.class)){
			return (T) (Byte) readByte();
		}
		else if(clazz.equals(Short.class)){
			return (T) (Short) readShort();
		}
		else if(clazz.equals(Integer.class)){
			return (T) (Integer) readInt();
		}
		else if(clazz.equals(Long.class)){
			return (T) (Long) readLong();
		}
		else if(clazz.equals(Float.class)){
			return (T) (Float) readFloat();
		}
		else if(clazz.equals(Double.class)){
			return (T) (Double) readDouble();
		}
		return getUnknownTypeValue(clazz, args);
	}
	protected <T> T getUnknownTypeValue(Class<T> clazz, Object[] args) throws IOException{
		throw new java.lang.UnsupportedOperationException(String.format("Trying to read unknown type %s from class %s", clazz.getSimpleName(), getClass().getName()));
	}

	protected void falloc(int l) throws IOException{
		int lack = l - is.available();
		if(lack > 0){
			throw getUEOFException(lack);
		}
	}
	protected IOException getUEOFException(int needed){
		return new IOException(String.format("Unexpected end of file: %d more bytes expected", needed));
	}
}
