package org.blockserver.io.tests;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.blockserver.io.bsf.BSF;
import org.blockserver.io.bsf.BSF.Type;
import org.blockserver.io.bsf.BSFReader;
import org.blockserver.io.bsf.BSFWriter;

import org.junit.Test;

public class IOLib_Tests{
	
	@Test
	public void test(){
		File file = new File(".", "test.tmp");
//		file.deleteOnExit();
		testByte(file, (byte) 0);
		testByte(file, (byte) 1);
		testByte(file, (byte) 2);
		testByte(file, (byte) 255);
		testShort(file, (short) 0);
		testShort(file, (short) 1);
		testShort(file, (short) 2);
		testShort(file, (short) 0xFFFF);
		testTriad(file, 0);
		testTriad(file, 1);
		testTriad(file, 2);
		//testTriad(file, 0xFFFFF);
		testInt(file, 0);
		testInt(file, 1);
		testInt(file, 2);
		testInt(file, 0xFFFFFFFF);
		testLong(file, 0);
		testLong(file, 1);
		testLong(file, 2);
		testLong(file, 0xFFFFFFFFFFFFFFFFL);
		testFloat(file, 0f);
		testFloat(file, 1f);
		testFloat(file, -1f);
		testFloat(file, -1.5f);
		testFloat(file, -10000000000000f);
		testDouble(file, 0d);
		testDouble(file, 1d);
		testDouble(file, -1d);
		testDouble(file, -1.5d);
		testDouble(file, -10000000000000d);
		testString(file, "");
		testString(file, "abc");
		testString(file, "\0x00");
		System.out.println("All OK! :)");
		System.exit(0);
	}
	private static void testByte(File file, byte b){
		System.out.println(String.format("Writing byte %d to %s...", b, file));
		try{
			BSFWriter writer = new BSFWriter(new FileOutputStream(file, false), Type.PLAYER);
			writer.writeByte(b);
			writer.close();
			try{
				BSFReader reader = new BSFReader(new FileInputStream(file));
				byte read = reader.readByte();
				reader.close();
				if(read != b){
					System.out.println(String.format("Expected byte %d, written and read "
							+ "as %d", b, read));
					throw new BSF.InvalidBSFFileException("Byte doesn't match");
				}
				System.out.println("Success!");
			}
			catch(BSF.InvalidBSFFileException e){
				dumpError(file, e);
				return;
			}
		}
		catch(Exception e){
			e.printStackTrace();
			System.exit(2);
		}
	}
	private static void testShort(File file, short s){
		System.out.println(String.format("Writing short %d to %s...", s, file));
		try{
			BSFWriter writer = new BSFWriter(new FileOutputStream(file, false), Type.PLAYER);
			writer.writeShort(s);
			writer.close();
			try{
				BSFReader reader = new BSFReader(new FileInputStream(file));
				short read = reader.readShort();
				reader.close();
				if(read != s){
					System.out.println(String.format("Expected short %d, written and read "
							+ "as %d", s, read));
					throw new BSF.InvalidBSFFileException("Short doesn't match");
				}
				System.out.println("Success!");
			}
			catch(BSF.InvalidBSFFileException e){
				dumpError(file, e);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			System.exit(2);
		}
	}
	private static void testTriad(File file, int t){
		System.out.println(String.format("Writing triad %d to %s...", t, file));
		try{
			BSFWriter writer = new BSFWriter(new FileOutputStream(file, false), Type.PLAYER);
			writer.writeTriad(t);
			writer.close();
			try{
				BSFReader reader = new BSFReader(new FileInputStream(file));
				int read = reader.readTriad();
				reader.close();
				if(read != t){
					System.out.println(String.format("Expected triad %d, written and read "
							+ "as %d", t, read));
					throw new BSF.InvalidBSFFileException("Triad doesn't match");
				}
				System.out.println("Success!");
			}
			catch(BSF.InvalidBSFFileException e){
				dumpError(file, e);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			System.exit(2);
		}
	}
	private static void testInt(File file, int i){
		System.out.println(String.format("Writing int %d to %s...", i, file));
		try{
			BSFWriter writer = new BSFWriter(new FileOutputStream(file, false), Type.PLAYER);
			writer.writeInt(i);
			writer.close();
			try{
				BSFReader reader = new BSFReader(new FileInputStream(file));
				int read = reader.readInt();
				reader.close();
				if(read != i){
					System.out.println(String.format("Expected int %d, written and read "
							+ "as %d", i, read));
					throw new BSF.InvalidBSFFileException("Int doesn't match");
				}
				System.out.println("Success!");
			}
			catch(BSF.InvalidBSFFileException e){
				dumpError(file, e);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			System.exit(2);
		}
	}
	private static void testLong(File file, long l){
		System.out.println(String.format("Writing long %d to %s...", l, file));
		try{
			BSFWriter writer = new BSFWriter(new FileOutputStream(file, false), Type.PLAYER);
			writer.writeLong(l);
			writer.close();
			try{
				BSFReader reader = new BSFReader(new FileInputStream(file));
				long read = reader.readLong();
				reader.close();
				if(read != l){
					System.out.println(String.format("Expected long %d, written and read "
							+ "as %d", l, read));
					throw new BSF.InvalidBSFFileException("Long doesn't match");
				}
				System.out.println("Success!");
			}
			catch(BSF.InvalidBSFFileException e){
				dumpError(file, e);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			System.exit(2);
		}
	}
	private static void testFloat(File file, float f){
		System.out.println(String.format("Writing float %d to %s...", f, file));
		try{
			BSFWriter writer = new BSFWriter(new FileOutputStream(file, false), Type.PLAYER);
			writer.writeFloat(f);
			writer.close();
			try{
				BSFReader reader = new BSFReader(new FileInputStream(file));
				float read = reader.readFloat();
				reader.close();
				if(read != f){
					System.out.println(String.format("Expected float %d, written and read "
							+ "as %d", f, read));
					throw new BSF.InvalidBSFFileException("Float doesn't match");
				}
				System.out.println("Success!");
			}
			catch(BSF.InvalidBSFFileException e){
				dumpError(file, e);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			System.exit(2);
		}
	}
	private static void testDouble(File file, double d){
		System.out.println(String.format("Writing double %d to %s...", d, file));
		try{
			BSFWriter writer = new BSFWriter(new FileOutputStream(file, false), Type.PLAYER);
			writer.writeDouble(d);
			writer.close();
			try{
				BSFReader reader = new BSFReader(new FileInputStream(file));
				double read = reader.readDouble();
				reader.close();
				if(read != d){
					System.out.println(String.format("Expected double %d, written and read "
							+ "as %d", d, read));
					throw new BSF.InvalidBSFFileException("Double doesn't match");
				}
				System.out.println("Success!");
			}
			catch(BSF.InvalidBSFFileException e){
				dumpError(file, e);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			System.exit(2);
		}
	}
	private static void testString(File file, String s){
		System.out.println(String.format("Writing string %s to %s...", s, file));
		try{
			BSFWriter writer = new BSFWriter(new FileOutputStream(file, false), Type.PLAYER);
			writer.writeString(s);
			writer.close();
			try{
				BSFReader reader = new BSFReader(new FileInputStream(file));
				String read = reader.readString();
				reader.close();
				if(read != s){
					System.out.println(String.format("Expected string %s, written and read "
							+ "as %s", s, read));
					throw new BSF.InvalidBSFFileException("String doesn't match");
				}
				System.out.println("Success!");
			}
			catch(BSF.InvalidBSFFileException e){
				dumpError(file, e);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			System.exit(2);
		}
	}
	private static Object[] toHexString(InputStream is) throws IOException{
		StringBuilder b = new StringBuilder(is.available() * 2 + 2);
		b.append("0x");
		List<Byte> list = new ArrayList<Byte>(is.available());
		int c;
		while((c = is.read()) != -1){
			list.add((byte) c);
			String hex = Integer.toHexString(c);
			while(hex.length() < 2){
				hex = "0" + hex;
			}
			b.append(hex);
		}
		byte[] array = new byte[list.size()];
		int i = 0;
		for(Byte bite: list){
			array[i++] = bite;
		}
		return new Object[]{b.toString(), array};
	}
	private static void dumpError(File file, BSF.InvalidBSFFileException e){
		e.printStackTrace();
		try{
			System.out.print("Buffer at " + file.getCanonicalPath() + ": ");
			byte[] buffer = file_get_contents(file);
			System.out.println(Arrays.toString(buffer));
			System.out.println("(" + (String) toHexString(new ByteArrayInputStream(buffer))[0] + ")");
			System.err.println("Test Failure.");
		}
		catch(IOException e1){
			e1.printStackTrace();
		}
		System.exit(1);
	}
	private static byte[] file_get_contents(File file){
		try{
			InputStream is = new FileInputStream(file);
			byte[] buffer = new byte[is.available()];
			is.read(buffer);
			is.close();
			return buffer;
		}
		catch(IOException e){
			e.printStackTrace();
			return null;
		}
	}
}
