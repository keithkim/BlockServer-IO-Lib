package org.blockserver.io.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.blockserver.io.bsf.BSF.Type;
import org.blockserver.io.bsf.BSFReader;
import org.blockserver.io.bsf.BSFWriter;

public class IOLib_Tests{
	public static void main(String[] args){
		File file = new File(".", "test.tmp");
		testByte(file, (byte) 0);
		testByte(file, (byte) 1);
		testByte(file, (byte) 2);
		testByte(file, (byte) 255);
		// TODO more
	}
	private static void testByte(File file, byte b){
		try{
			BSFWriter writer = new BSFWriter(new FileOutputStream(file, false), Type.PLAYER);
			writer.writeByte(b);
			writer.close();
			BSFReader reader = new BSFReader(new FileInputStream(file));
			byte read = reader.readByte();
			reader.close();
			if(read != b){
				System.exit(3);
			}
		}
		catch(IOException e){
			e.printStackTrace();
			System.exit(2);
		}
	}
}
