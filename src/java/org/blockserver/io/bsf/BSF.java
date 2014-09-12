package org.blockserver.io.bsf;

import java.io.IOException;
import java.util.Locale;

public final class BSF{
	public final static byte[] HEADER = {
		0x50, (byte) 0xb5, (byte) 0xff
	};
	public final static byte[] FOOTER = {
		(byte) 0xe0, (byte) 0xb5, (byte) 0xff
	};

	@SuppressWarnings("serial")
	public static class InvalidBSFFileException extends IOException{
		public InvalidBSFFileException(int lack){
			this(String.format("Unexpected end of file: %d more bytes required", lack));
		}

		public InvalidBSFFileException(String message){
			super("Error parsing BSF file: ".concat(message));
		}
	}

	public static enum Type{
		PLAYER(0x00);

		private int id;

		Type(int id){
			this.id = id;
		}

		public short getID(){
			return (short) id;
		}

		public static Type get(int id){
			for(Type t: values()){
				if(t.getID() == id){
					return t;
				}
			}
			throw new IndexOutOfBoundsException();
		}
	}

	public static enum Version{
		HOMO_HABILIS		(0),
		KENICHTHYS_CAMPBELLI(1),
//		AMOEBA_PROTEUS		(2)
		;

		private int id;

		Version(int id){
			this.id = id;
		}

		public short getID(){
			return (short) id;
		}
		public char getVersionString(){
			return (char) id;
		}
		public String getName(){
			char[] name = new char[name().toLowerCase(Locale.US).length()];
			name().getChars(0, name.length, name, 0);
			String out = new String();
			boolean cap = true;
			for(int i = 0; i < name.length; i++){
				char c = name[i];
				if(c == '_'){
					c = ' ';
				}
				out += cap ? Character.toUpperCase(c):c;
				if(c == ' '){
					cap = true;
				}
			}
			return out;
		}

		public static Version get(byte b){
			for(Version v: values()){
				if(v.getID() == b){
					return v;
				}
			}
			return null;
		}
		public static Version newestVersion(){
			return HOMO_HABILIS;
		}
	}
}
