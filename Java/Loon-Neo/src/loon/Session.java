/**
 * Copyright 2008 - 2015 The Loon Game Engine Authors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * @project loon
 * @author cping
 * @email：javachenpeng@yahoo.com
 * @version 0.5
 */
package loon;

import loon.utils.ArrayMap;
import loon.utils.ArrayMap.Entry;
import loon.utils.StringUtils;
import loon.utils.TArray;

/**
 * 游戏记录器，用于记录当前游戏数据
 */
public class Session {

	private Save _save;

	private boolean isPersisted = false;

	private String loadData() {
		return _save.getItem(name);
	}

	private void svaeData(String result) {
		_save.setItem(name, result);
	}

	private void removeData() {
		_save.removeItem(name);
	}

	public static Session load(String name) {
		return new Session(name);
	}

	private final char flag = '&';

	private final class Record {

		private String name;

		private String[] values;

		private boolean active;

		public Record(String name) {
			this.values = new String[0];
			this.name = name;
		}

		public int size() {
			if (values != null) {
				return values.length;
			}
			return 0;
		}

		public int decode(String[] parts, int n) {
			if (n >= parts.length) {
				return n;
			}
			active = "1".equals(parts[n++]);
			if (n >= parts.length) {
				return n;
			}
			int count = Integer.parseInt(parts[n++]);
			values = new String[count];
			for (int i = 0; i < count; i++) {
				if (n >= parts.length) {
					return n;
				}
				values[i] = parts[n++];
			}
			return n;
		}

		public String get(int index) {
			if (index < 0 || index >= values.length) {
				return null;
			} else {
				return values[index];
			}
		}

		public void set(int index, final String v) {
			final String value = StringUtils.replace(v, String.valueOf(flag),
					"+");
			if (index >= values.length) {
				int size = index + 1;
				String[] res = new String[size];
				System.arraycopy(values, 0, res, 0, values.length);
				this.values = res;
			}
			this.values[index] = value;
		}

		public String encode() {
			StringBuffer sbr = new StringBuffer(32);
			sbr.append(this.name);
			sbr.append(flag);
			sbr.append(this.active ? "1" : "0");
			sbr.append(flag);
			sbr.append(this.values.length);
			sbr.append(flag);
			for (int i = 0; i < this.values.length; i++) {
				sbr.append(this.values[i]);
				sbr.append(flag);
			}
			return sbr.toString();
		}

	}

	private String name;

	private ArrayMap records;

	private TArray<Record> recordsList;

	public Session(String name) {
		this(name, true);
	}

	public Session(String name, boolean gain) {
		if (name == null) {
			throw new RuntimeException("session name can not exist !");
		}
		try {
			this._save = LSystem._base.save();
			isPersisted = true;
		} catch (Exception ex) {
			isPersisted = false;
		}
		this.name = name;
		this.records = new ArrayMap(10);
		this.recordsList = new TArray<Record>(10);
		if (gain) {
			load();
		}
	}

	public boolean isPersisted() {
		return isPersisted;
	}

	public int loadEncodeSession(String encode) {
		if (encode != null && !"".equals(encode)) {
			String[] parts = StringUtils.split(encode, flag);
			return decode(parts, 0);
		}
		return -1;
	}

	public String getActiveID() {
		synchronized (recordsList) {
			for (int i = 0; i < recordsList.size; i++) {
				Record record = recordsList.get(i);
				if (record.active) {
					return record.name;
				}
			}
			return null;
		}
	}

	public String set(int index, String value) {
		String name = "session_name_" + System.currentTimeMillis();
		set(name, index, value);
		return name;
	}

	public String set(int index, int value) {
		return set(index, String.valueOf(value));
	}

	public String set(int index, float value) {
		return set(index, String.valueOf(value));
	}

	public String set(int index, boolean value) {
		return set(index, value ? "1" : "0");
	}

	public void set(String name, String value) {
		set(name, 0, value);
	}

	public void set(String name, int index, String value) {
		synchronized (recordsList) {
			Record record = (Record) records.get(name);
			if (record == null) {
				record = new Record(name);
				records.put(name, record);
				recordsList.add(record);
			}
			record.set(index, value);
		}
	}

	public void set(String name, int value) {
		set(name, 0, value);
	}

	public void set(String name, int index, int value) {
		set(name, index, String.valueOf(value));
	}

	public void set(String name, float value) {
		set(name, 0, value);
	}

	public void set(String name, int index, float value) {
		set(name, index, String.valueOf(value));
	}

	public void set(String name, boolean value) {
		set(name, 0, value ? "1" : "0");
	}

	public void set(String name, int index, boolean value) {
		set(name, index, value ? "1" : "0");
	}

	public void add(String name, String value) {
		synchronized (recordsList) {
			Record record = (Record) records.get(name);
			if (record == null) {
				record = new Record(name);
				records.put(name, record);
				recordsList.add(record);
			}
			int id = record.size();
			record.set(id++, value);
		}
	}

	public void add(String name, int value) {
		add(name, String.valueOf(value));
	}

	public void add(String name, float value) {
		add(name, String.valueOf(value));
	}

	public void add(String name, boolean value) {
		add(name, value ? "1" : "0");
	}

	public String get(String name, int index) {
		synchronized (recordsList) {
			Record record = (Record) records.get(name);
			if (record == null) {
				return null;
			} else {
				return record.get(index);
			}
		}
	}

	public int getInt(String name, int index) {
		String res = get(name, index);
		return res != null ? Integer.parseInt(res) : -1;
	}

	public float getFloat(String name, int index) {
		String res = get(name, index);
		return res != null ? Float.parseFloat(res) : -1;
	}

	public boolean getBoolean(String name, int index) {
		String res = get(name, index);
		return res != null ? ("1".equals(res) ? true : false) : false;
	}

	public String get(String name) {
		return get(name, 0);
	}

	public int getInt(String name) {
		return getInt(name, 0);
	}

	public float getFloat(String name) {
		return getFloat(name, 0);
	}

	public boolean getBoolean(String name) {
		return getBoolean(name, 0);
	}

	public void delete(String name) {
		synchronized (recordsList) {
			records.remove(name);
			for (int i = 0; i < recordsList.size; i++) {
				Record record = recordsList.get(i);
				if (record.name.equals(name)) {
					recordsList.removeIndex(i);
					i--;
				}
			}
		}
	}

	public int getCount(String name) {
		synchronized (recordsList) {
			Record record = (Record) records.get(name);
			if (record == null) {
				return 0;
			} else {
				return record.values.length;
			}
		}
	}

	public int getSize() {
		if (recordsList != null) {
			return recordsList.size;
		} else {
			return 0;
		}
	}

	public int decode(String[] parts, int n) {
		synchronized (recordsList) {
			records.clear();
			recordsList.clear();
			if (n >= parts.length) {
				return n;
			}

			int count = Integer.parseInt(parts[n++]);
			for (int i = 0; i < count; i++) {
				if (n >= parts.length) {
					return n;
				}
				Record record = new Record(parts[n++]);
				n = record.decode(parts, n);
				records.put(record.name, record);
				recordsList.add(record);
			}
			return n;
		}
	}

	public String encode() {
		synchronized (recordsList) {
			StringBuffer sbr = new StringBuffer();
			sbr.append(recordsList.size).append(flag).toString();
			for (int i = 0; i < recordsList.size; i++) {
				sbr.append((recordsList.get(i)).encode()).toString();
			}
			return sbr.toString();
		}
	}

	public boolean hasData(String name) {
		synchronized (recordsList) {
			return records.get(name) != null;
		}
	}

	public void activate(String name) {
		synchronized (recordsList) {
			Record record = new Record(name);
			record.active = true;
			records.put(name, record);
			recordsList.add(record);
		}
	}

	public void clear(String name) {
		synchronized (recordsList) {
			Record record = (Record) records.remove(name);
			if (record != null) {
				recordsList.remove(record);
			}
		}
	}

	public boolean isActive(String name) {
		synchronized (recordsList) {
			Record record = (Record) records.get(name);
			if (record != null) {
				return record.active;
			} else {
				return false;
			}
		}
	}

	public void reject(String name) {
		synchronized (recordsList) {
			clear(name);
			Record record = new Record(name);
			record.active = false;
			record.set(0, "1");
			records.put(name, record);
			recordsList.add(record);
		}
	}

	public String getSessionName() {
		return name;
	}

	public void save() {
		String result = encode();
		if (result != null && !"".equals(result)) {
			svaeData(result);
		}
	}

	public ArrayMap getRecords(int index) {
		ArrayMap result = new ArrayMap(records.size());
		for (int i = 0; i < records.size(); i++) {
			Entry entry = records.getEntry(i);
			result.put(entry.getKey(), ((Record) entry.getValue()).get(index));
		}
		return result;
	}

	public int load() {
		return loadEncodeSession(loadData());
	}

	public Session cpy() {
		return new Session(name);
	}

	public void dispose(String name) {
		synchronized (recordsList) {
			clear(name);
			Record record = new Record(name);
			record.active = false;
			records.put(name, record);
			recordsList.add(record);
		}
	}

	public void dispose() {
		try {
			if (records != null) {
				records.clear();
			}
			if (recordsList != null) {
				recordsList.clear();
			}
			removeData();
		} catch (Exception e) {
		}
	}
}
