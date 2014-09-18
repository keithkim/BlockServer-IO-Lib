package org.blockserver.io.bsf;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.blockserver.io.BinaryReader;
import org.blockserver.objects.IInventory;
import org.blockserver.objects.IItem;

public class BSFReader extends BinaryReader{
	private BSF.Version version;
	private BSF.Type type;
	public BSFReader(InputStream is) throws IOException{
		super(is);
		init();
	}
	protected void init() throws IOException{
		validate(read(BSF.HEADER.length), BSF.HEADER, "header");
		version = BSF.Version.get(readByte());
		type = BSF.Type.get(readByte());
		if(version == null || type == null){
			throw new BSF.InvalidBSFFileException("Invalid/Unsupported version/type");
		}
	}

	@Override
	public void close() throws IOException{
		validate(read(BSF.FOOTER.length), BSF.FOOTER, "footer");
		super.close();
	}

	public Map<String, Object> readAll() throws IOException{
		return type.read(this);
	}

	public <T> List<T> readList(Class<T> clazz) throws IOException{
		return readList(clazz, new Object[0]);
	}
	public <T> List<T> readList(Class<T> clazz, Object[] args) throws IOException{
		int size = readInt();
		List<T> buffer = new ArrayList<T>(size);
		for(int i = 0; i < size; i++){
			buffer.add(getUnknownTypeValue(clazz, args));
		}
		return buffer;
	}
	public <K, V> Map<K, V> readMap(Class<K> kclass, Class<V> vclass) throws IOException{
		return readMap(kclass, vclass, new Object[0]);
	}
	public <K, V> Map<K, V> readMap(Class<K> kclass, Class<V> vclass, Object[] args) throws IOException{
		int size = readInt();
		Map<K, V> buffer = new HashMap<K, V>(size);
		for(int i = 0; i < size; i++){
			K key = getUnknownTypeValue(kclass, args);
			V value = getUnknownTypeValue(vclass, args);
			buffer.put(key, value);
		}
		return buffer;
	}
	public IItem readItem() throws IOException{
		short id = readShort();
		byte damage = readByte();
		byte count = readByte();
		Map<CharSequence, String> metadata = readMap(CharSequence.class, String.class);
		return new IItem.DummyItem(id, damage, count, metadata);
	}
	public IInventory.DummyInventory readInventory() throws IOException{
		List<IItem> buffer = readList(IItem.class);
		IItem[] result = new IItem[buffer.size()];
		buffer.toArray(result);
		return new IInventory.DummyInventory(result);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected <T> T getUnknownTypeValue(Class<T> clazz, Object[] args) throws IOException{
		if(clazz.equals(IInventory.class)){
			return (T) readInventory();
		}
		else if(clazz.equals(IItem.class)){
			return (T) readItem();
		}
		return super.getUnknownTypeValue(clazz, args);
	}

	protected void validate(byte[] testant, byte[] expected, String name) throws IOException{
		if(!Arrays.equals(testant, expected)){
			throw new BSF.InvalidBSFFileException(String.format(
					"Incorrect %s: %s. Expected: %s", name,
					Arrays.toString(testant), Arrays.toString(expected)));
		}
	}
	@Override
	protected IOException getUEOFException(int needed){
		return new BSF.InvalidBSFFileException(needed);
	}
}
