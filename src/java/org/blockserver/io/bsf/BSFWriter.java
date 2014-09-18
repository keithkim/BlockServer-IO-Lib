package org.blockserver.io.bsf;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.blockserver.io.BinaryWriter;
import org.blockserver.objects.IInventory;
import org.blockserver.objects.IItem;

public class BSFWriter extends BinaryWriter{
	private BSF.Type type;
	private BSF.Version version;

	public BSFWriter(OutputStream os, BSF.Type type) throws IOException{
		this(os, type, BSF.Version.newestVersion());
	}
	public BSFWriter(OutputStream os, BSF.Type type, BSF.Version version) throws IOException{
		super(os);
		this.type = type;
		this.version = version;
		init();
	}
	protected void init() throws IOException{
		writeBytes(BSF.HEADER);
		writeShort(version.getID());
		writeShort(type.getID());
	}

	@Override
	public void close() throws IOException{
		writeBytes(BSF.FOOTER);
		super.close();
	}

	public void writeAll(Map<String, Object> args) throws IOException{
		type.write(this, args);
	}

	public <T> void writeList(Class<T> tclazz, List<T> list, Object... args) throws IOException{
		writeInt(list.size());
		for(T t: list){
			writeUnknownType(t, args);
		}
	}
	public <K, V> void writeMap(Map<K, V> map, Object... args) throws IOException{
		writeInt(map.size());
		for(Map.Entry<K, V> entry: map.entrySet()){
			writeUnknownType(entry.getKey(), args);
			writeUnknownType(entry.getValue(), args);
		}
	}
	public void writeItem(IItem item) throws IOException{
		writeShort((short) item.getID());
		writeByte((byte) item.getDamage());
		writeByte((byte) item.getCount());
		writeMap(item.getMetadata());
	}
	public void writeInventory(IInventory<? extends IItem> inv) throws IOException{
		writeInt(inv.getSize());
		for(IItem item: inv){
			writeItem(item);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> void writeUnknownType(T obj, Object[] args) throws IOException{
		if(obj instanceof IInventory){
			writeInventory((IInventory<? extends IItem>) obj);
		}
		else if(obj instanceof IItem){
			writeItem((IItem) obj);
		}
		else{
			super.writeUnknownType(obj, args);
		}
	}
}
