package org.blockserver.io;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class BinaryWriter implements Flushable, Closeable{
	protected OutputStream os;
	protected boolean endianness;

	public BinaryWriter(OutputStream os){
		this(os, BinaryUtils.BIG_ENDIAN);
	}
	public BinaryWriter(OutputStream os, boolean endianness){
		this.os = os;
		this.endianness = endianness;
	}

	@Override
	public void flush() throws IOException{
		os.flush();
	}
	@Override
	public void close() throws IOException{
		os.close();
	}
	
	public void switchEndianness(boolean endianness){
		this.endianness = endianness;
	}

	public void writeString(String string) throws IOException{
		writeString(string, 2);
	}
	public void writeString(String string, int lenLen) throws IOException{
		write(string.length(), lenLen);
		os.write(string.getBytes());
	}
	public void writeByte(byte b) throws IOException{
		os.write(b);
	}
	public void writeShort(short s) throws IOException{
		write(s, 2);
	}
	public void writeTriad(int t) throws IOException{
		write(t, 3);
	}
	public void writeInt(int i) throws IOException{
		write(i, 4);
	}
	public void writeLong(long l) throws IOException{
		write(l, 8);
	}
	public void writeFloat(float f) throws IOException{
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.putFloat(f);
		os.write(bb.array());
	}
	public void writeDouble(double d) throws IOException{
		ByteBuffer bb = ByteBuffer.allocate(8);
		bb.putDouble(d);
		os.write(bb.array());
	}
	public void write(long x, int len) throws IOException{
		os.write(BinaryUtils.write(x, len, endianness));
	}
	public void write(byte[] bytes) throws IOException{
		os.write(bytes);
	}
	public void writeNat(int oneByte) throws IOException{
		os.write(oneByte);
	}
	public void writeVarInt(int i) throws IOException{
		//Credit to thinkofdeath: https://github.com/thinkofdeath
		while (true) {
		      if ((i & 0xFFFFFF80) == 0) {
		        writeByte(i);
		        return;
		      }
		 
		      writeByte(i & 0x7F | 0x80);
		      i >>>= 7;
		}
	}

	public <T> void writeObject(T obj, Object[] args) throws IOException{
		if(obj instanceof CharSequence){
			boolean written = false;
			if(args.length > 0){
				if(args[0] instanceof Integer){
					writeString(obj.toString(), (int) args[0]);
					written = true;
				}
			}
			if(!written){
				writeString(obj.toString());
			}
		}
		else if(obj instanceof Byte){
			writeByte((byte) (Byte) obj);
		}
		else if(obj instanceof Short){
			writeShort((short) (Short) obj);
		}
		else if(obj instanceof Integer){
			writeInt((int) (Integer) obj);
		}
		else if(obj instanceof Long){
			writeLong((long) (Long) obj);
		}
		else if(obj instanceof Float){
			writeFloat((float) (Float) obj);
		}
		else if(obj instanceof Double){
			writeDouble((double) (Double) obj);
		}
		else{
			writeUnknownType(obj, args);
		}
	}
	protected <T> void writeUnknownType(T obj, Object[] args) throws IOException{
		throw new UnsupportedOperationException(String.format("Unknown BSF object type %s", obj.getClass().getName()));
	}

	public OutputStream getOutputStream(){
		return os;
	}
}
