package org.blockserver.io;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileStream implements Flushable, Closeable{
	private File file;
	private byte[] buffer;
	private int pointer;
	public FileStream(File file) throws IOException{
		this.file = file;
		InputStream is = new FileInputStream(file);
		buffer = new byte[is.available()];
		is.read(buffer);
		is.close();
	}
	public void seek(int pos){
		seek(pos, false);
	}
	public void seek(int pos, boolean relative){
		if(relative){
			pos += pointer;
		}
		if(pos < 0 || pos > buffer.length){
			throw new IndexOutOfBoundsException();
		}
		pointer = pos;
	}
	@Override
	public void flush() throws IOException{
		OutputStream os = new FileOutputStream(file, false);
		os.write(buffer);
		os.close();
	}
	@Override
	public void close() throws IOException{
		flush();
	}
}
