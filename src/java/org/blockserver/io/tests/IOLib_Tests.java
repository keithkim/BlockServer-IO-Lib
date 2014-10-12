package org.blockserver.io.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.blockserver.io.bsf.BSF;
import org.blockserver.io.bsf.BSF.Type;
import org.blockserver.io.bsf.BSFReader;
import org.blockserver.io.bsf.BSFWriter;

public class IOLib_Tests{
	public static void main(String[] args){
		File file = new File(".", "test.tmp");
		file.deleteOnExit();
		testByte(file, (byte) 0);
		testByte(file, (byte) 1);
		testByte(file, (byte) 2);
		testByte(file, (byte) 255);
		// TODO more
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
				e.printStackTrace();
				String output = "Buffer: 0x";
				FileInputStream is = new FileInputStream(file);
				int c;
				while((c = is.read()) != -1){
					output += Integer.toHexString(c);
				}
				is.close();
				System.out.println(output);
				System.exit(3);
				return;
			}
		}
		catch(Exception e){
			e.printStackTrace();
			System.exit(2);
		}
		finally{
			file.delete();
		}
	}
}
