# socket

NIO 、AIO 、Codec 、Protocol、UseCase

Performance、Scalability、Availability


## NIO API

Buffer  <br>
```
abstract class Buffer {
 int capacity();
 int position();
 Buffer position(int newPosition);
 int limit();
 Buffer limit(int newLimit);
 Buffer mark();
 Buffer reset();
 Buffer clear();
 Buffer flip();
 Buffer rewind();
 int remaining();
 boolean hasRemaining();
 boolean isReadOnly();
}

abstract class ByteBuffer extends Buffer {
 static ByteBuffer allocateDirect(int capacity);
 static ByteBuffer allocate(int capacity);
 static ByteBuffer wrap(byte[] src, int offset, int len);
 static ByteBuffer wrap(byte[] src);
 boolean isDirect();
 ByteOrder order();
 ByteBuffer order(ByteOrder bo);
 ByteBuffer slice();
 ByteBuffer duplicate();
 ByteBuffer compact();
 ByteBuffer asReadOnlyBuffer();
 byte get();
 byte get(int index);
 ByteBuffer get(byte[] dst, int offset, int length);
 ByteBuffer get(byte[] dst);
 ByteBuffer put(byte b);
 ByteBuffer put(int index, byte b);
 ByteBuffer put(byte[] src, int offset, int length);
 ByteBuffer put(ByteBuffer src);
 ByteBuffer put(byte[] src);
 char getChar();
 char getChar(int index);
 ByteBuffer putChar(char value);
 ByteBuffer putChar(int index, char value);
 CharBuffer asCharBuffer();
 short getShort();
 short getShort(int index);
 ByteBuffer putShort(short value);
 ByteBuffer putShort(int index, short value);
 ShortBuffer asShortBuffer();
 int getInt();
 int getInt(int index);
 ByteBuffer putInt(int value);
 ByteBuffer putInt(int index, int value);
 IntBuffer asIntBuffer();
 long getLong();
 long getLong(int index);
 ByteBuffer putLong(long value);
 ByteBuffer putLong(int index, long value);
 LongBuffer asLongBuffer();
 float getFloat();
 float getFloat(int index);
 ByteBuffer putFloat(float value);
 ByteBuffer putFloat(int index, float value);
 FloatBuffer asFloatBuffer();
 double getDouble();
 double getDouble(int index);
 ByteBuffer putDouble(double value);
 ByteBuffer putDouble(int index, double value);
 DoubleBuffer asDoubleBuffer();
}
```

Channels <br>
Selector  <br>
SelectionKey  <br>
NetworkInterface <br>


## NIO raw examples


## Network Services 

Scalability Goals

Divide and Conquer

Event-driven Designs

### Reactor Pattern

Single threaded version

Multithreaded


## Netty


