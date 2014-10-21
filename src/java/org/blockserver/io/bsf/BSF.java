package org.blockserver.io.bsf;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.blockserver.objects.IInventory;
import org.blockserver.objects.IItem;

public final class BSF{
	public final static byte[] HEADER = {
		0x50, (byte) 0xb5, (byte) 0xff
	};
	public final static byte[] FOOTER = {
		(byte) 0xe0, (byte) 0xb5, (byte) 0xff
	};
	public final static String P_CASE_NAME = "caseName";
	public final static String P_VECTORS = "vectors";
	public final static String P_WORLD_NAME = "worldName";
	public final static String P_I_INVENTORY = "dummyInventory";
	public final static String LI_SPAWN_X = "spawn-x";
	public final static String LI_SPAWN_Y = "spawn-y";
	public final static String LI_SPAWN_Z = "spawn-z";
	public final static String LI_GENERATOR = "generator";
	public final static String LI_GENERATION_OPTS = "generation-opts";

	public static class InvalidBSFFileException extends IOException{
		private static final long serialVersionUID = -1036754379219128367L;
		public InvalidBSFFileException(int lack){
			this(String.format("Unexpected end of file: %d more bytes required", lack));
		}
		public InvalidBSFFileException(String message){
			super("Error parsing BSF file: ".concat(message));
		}
	}

	public static enum Type{
		PLAYER(0x00){
			@Override
			public Map<String, Object> read(BSFReader reader) throws IOException{
				Map<String, Object> map = new HashMap<String, Object>(4);
				map.put(P_CASE_NAME, reader.readString(1));
				double[] vectors = new double[3];
				for(int i = 0; i < 3; i++){
					vectors[i] = reader.readDouble();
				}
				map.put(P_VECTORS, vectors);
				map.put(P_WORLD_NAME, reader.readString(1));
				map.put(P_I_INVENTORY, reader.readInventory());
				return map;
			}
			@Override
			@SuppressWarnings("unchecked")
			public void write(BSFWriter writer, Map<String, Object> args) throws IOException{
				writer.writeString((String) args.get(P_CASE_NAME), 1);
				for(double v: (double[]) args.get(P_VECTORS)){
					writer.writeDouble(v);
				}
				writer.writeString((String) args.get(P_WORLD_NAME), 1);
				writer.writeInventory((IInventory<? extends IItem>) args.get(P_I_INVENTORY));
			}
		},
		LEVEL_INDEX(0x01){
			@Override
			public Map<String, Object> read(BSFReader reader)
					throws IOException{
				double spawnX = reader.readDouble();
				double spawnY = reader.readDouble();
				double spawnZ = reader.readDouble();
				String generator = reader.readString();
				byte[] generationOpts = reader.read(reader.readInt());
				Map<String, Object> out = new HashMap<String, Object>(3);
				out.put(LI_SPAWN_X, spawnX);
				out.put(LI_SPAWN_Y, spawnY);
				out.put(LI_SPAWN_Z, spawnZ);
				out.put(LI_GENERATOR, generator);
				out.put(LI_GENERATION_OPTS, generationOpts);
				return out;
			}
			@Override
			public void write(BSFWriter writer, Map<String, Object> args)
					throws IOException{
				writer.writeDouble((double) args.get(LI_SPAWN_X));
				writer.writeDouble((double) args.get(LI_SPAWN_Y));
				writer.writeDouble((double) args.get(LI_SPAWN_Z));
				writer.writeString((String) args.get(LI_GENERATOR));
				writer.write((byte[]) args.get(LI_GENERATION_OPTS));
			}
		};

		private int id;

		private Type(int id){
			this.id = id;
		}

		public short getID(){
			return (short) id;
		}
		public abstract Map<String, Object> read(BSFReader reader) throws IOException;
		public abstract void write(BSFWriter writer, Map<String, Object> args) throws IOException;

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
		 HOMO_HABILIS			(0)
		,EUSELASIA_ORFITA		(1)
//		,KENICHTHYS_CAMPBELLI	(2)
//		,AMOEBA_PROTEUS			(3)
		;

		private int id;

		private Version(int id){
			this.id = id;
		}

		public short getID(){
			System.out.println(String.format("%s: %d", name(), ordinal()));
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

		public static Version get(short b){
			for(Version v: values()){
				if(v.getID() == b){
					return v;
				}
			}
			System.out.println(String.format("Cannot find version %d", b));
			return null;
		}
		public static Version newestVersion(){
			return EUSELASIA_ORFITA;
		}
	}
}
