package loon.live2d.io;

import loon.LSystem;
import loon.live2d.base.*;
import loon.live2d.id.*;
import loon.live2d.type.*;
import loon.utils.ArrayByte;
import loon.utils.TArray;

public class BReader {
	
	ArrayByte buffers;
	byte[] _bytes;
	int _skip;
	int _flag;
	int _ver;
	TArray<Object> objs;

	public BReader(final ArrayByte in) {
		this._bytes = new byte[1000];
		this._skip = 0;
		this._flag = 0;
		this._ver = 0;
		this.objs = new TArray<Object>();
		this.buffers = in;
	}

	public BReader(final ArrayByte in, final int version) {
		this._bytes = new byte[1000];
		this._skip = 0;
		this._flag = 0;
		this.objs = new TArray<Object>();
		this._ver = version;
	}

	public int getVersion() {
		return this._ver;
	}

	public void setVersion(final int g) {
		this._ver = g;
	}

	public int readDataSize() {
		return readDataSize(this.buffers);
	}

	public static int readDataSize(final ArrayByte in) {
		try {
			final int read = in.readByte();
			if ((read & 0x80) == 0x0) {
				return read & 0xFF;
			}
			final int read2;
			if (((read2 = in.readByte()) & 0x80) == 0x0) {
				return (read & 0x7F) << 7 | (read2 & 0x7F);
			}
			final int read3;
			if (((read3 = in.readByte()) & 0x80) == 0x0) {
				return (read & 0x7F) << 14 | (read2 & 0x7F) << 7
						| (read3 & 0xFF);
			}
			final int read4;
			if (((read4 = in.readByte()) & 0x80) == 0x0) {
				return (read & 0x7F) << 21 | (read2 & 0x7F) << 14
						| (read3 & 0x7F) << 7 | (read4 & 0xFF);
			}
			throw new RuntimeException("Not supported  _");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}

	public double readDouble() {
		try {
			this.reset();
			return buffers.readDouble();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}

	public float readerFloat() {
		try {
			this.reset();
			return buffers.readFloat();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}

	public int readInt() {
		try {
			this.reset();
			return buffers.readInt();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}

	public byte readByte() {
		try {
			this.reset();
			return (byte) this.buffers.readByte();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}

	public short readShort() {
		try {
			this.reset();
			return buffers.readShort();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}

	public long readLong() {
		try {
			this.reset();
			return buffers.readLong();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}

	public boolean readExist() {
		try {
			this.reset();
			return this.buffers.readByte() != 0;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public String readString() {
		try {
			this.reset();
			final int size = this.readDataSize();
			if (this._bytes.length < size) {
				this._bytes = new byte[size];
			}
			this.buffers.read(this._bytes, 0, size);
			return new String(this._bytes, 0, size, LSystem.ENCODING);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public int[] readInts() {
		this.reset();
		final int b = this.readDataSize();
		final int[] array = new int[b];
		for (int i = 0; i < b; ++i) {
			array[i] = this.readInt();
		}
		return array;
	}

	public float[] readFloats() {
		this.reset();
		final int b = this.readDataSize();
		final float[] array = new float[b];
		for (int i = 0; i < b; ++i) {
			array[i] = this.readerFloat();
		}
		return array;
	}

	public double[] readDoubles() {
		this.reset();
		final int b = this.readDataSize();
		final double[] array = new double[b];
		for (int i = 0; i < b; ++i) {
			array[i] = this.readDouble();
		}
		return array;
	}

	public Object reader() {
		return this.reader(-1);
	}

	public Object reader(int b) {
		this.reset();
		if (b < 0) {
			b = this.readDataSize();
		}
		if (b != 33) {
			final Object c = this.getID(b);
			this.objs.add(c);
			return c;
		}
		final int _skip = this.readInt();
		if (_skip >= 0 && _skip < this.objs.size) {
			return this.objs.get(_skip);
		}
		throw new RuntimeException("illegal refNo @Breader");
	}

	public Object getID(final int n) {
		if (n == 0) {
			return null;
		}
		if (n == 50) {
			return DrawDataID.getID(this.readString());
		}
		if (n == 51) {
			return BaseDataID.getID(this.readString());
		}
		if (n == 134) {
			return PartsDataID.getID(this.readString());
		}
		if (n == 60) {
			return ParamID.getID(this.readString());
		}
		if (n >= 48) {
			final IOBase b = (IOBase) loon.live2d.io.IOType.b(n);
			if (b != null) {
				b.readV2(this);
				return b;
			}
			return null;
		} else {
			switch (n) {
			case 1: {
				return this.readString();
			}
			case 10: {
				return new LDColor(this.readInt(), true);
			}
			case 11: {
				return new LDRectF((float) this.readDouble(),
						(float) this.readDouble(), (float) this.readDouble(),
						(float) this.readDouble());
			}
			case 12: {
				return new LDRectF(this.readerFloat(), this.readerFloat(),
						this.readerFloat(), this.readerFloat());
			}
			case 13: {
				return new LDPointF((float) this.readDouble(),
						(float) this.readDouble());
			}
			case 14: {
				return new LDPointF(this.readerFloat(), this.readerFloat());
			}
			case 15: {
				final int b2 = this.readDataSize();
				final TArray<Object> list = new TArray<Object>(b2);
				for (int i = 0; i < b2; ++i) {
					list.add(this.reader());
				}
				return list;
			}
			case 17: {
				return new Indexs((float) this.readDouble(),
						(float) this.readDouble(), (float) this.readDouble(),
						(float) this.readDouble(), (float) this.readDouble(),
						(float) this.readDouble());
			}
			case 21: {
				return new LDRect(this.readInt(), this.readInt(),
						this.readInt(), this.readInt());
			}
			case 22: {
				return new LDPoint(this.readInt(), this.readInt());
			}
			case 23: {
				throw new RuntimeException("Not Implemented ");
			}
			case 16:
			case 25: {
				return this.readInts();
			}
			case 26: {
				return this.readDoubles();
			}
			case 27: {
				return this.readFloats();
			}
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
			case 18:
			case 19:
			case 20:
			case 24:
			case 28: {
				throw new RuntimeException(
						"not impl : readObject() of 2-9 ,18,19,20,24,28");
			}
			default: {
				throw new RuntimeException("not impl : readObject() NO DEF");
			}
			}
		}
	}

	public boolean readBool() {
		try {
			if (this._skip == 0) {
				this._flag = this.buffers.readByte();
			} else if (this._skip == 8) {
				this._flag = this.buffers.readByte();
				this._skip = 0;
			}
			return (this._flag >> 7 - this._skip++ & 0x1) == 0x1;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public int getSkip() {
		return _skip;
	}

	public void setSkip(int s) {
		this._skip = s;
	}

	private void reset() {
		if (this._skip != 0) {
			this._skip = 0;
		}
	}
}
