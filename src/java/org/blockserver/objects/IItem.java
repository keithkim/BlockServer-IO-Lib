package org.blockserver.objects;

import java.util.Map;

public interface IItem{
	public int getID();
	public int getDamage();
	public int getCount();
	public Map<CharSequence, String> getMetadata();
	public class DummyItem implements IItem{
		private int id, damage, count;
		private Map<CharSequence, String> metadata;
		public DummyItem(int id, int damage, int count, Map<CharSequence, String> metadata){
			this.id = id;
			this.damage = damage;
			this.count = count;
			this.metadata = metadata;
		}
		@Override
		public int getID(){
			return id;
		}
		@Override
		public int getDamage(){
			return damage;
		}
		@Override
		public int getCount(){
			return count;
		}
		@Override
		public Map<CharSequence, String> getMetadata(){
			return metadata;
		}
	}

}
