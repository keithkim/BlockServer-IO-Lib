package org.blockserver.objects;

import java.util.Iterator;

import org.blockserver.io.ArrayIterator;

public interface IInventory<I extends IItem> extends Iterable<I>{
	public int getSize();
	public IItem[] getItems();

	public static class DummyInventory implements IInventory<IItem>{
		private IItem[] items;
		public DummyInventory(IItem[] items){
			this.items = items;
		}
		@Override
		public Iterator<IItem> iterator(){
			return new ArrayIterator<IItem>(items);
		}
		@Override
		public int getSize(){
			return items.length;
		}
		@Override
		public IItem[] getItems(){
			return items;
		}
	}
}
