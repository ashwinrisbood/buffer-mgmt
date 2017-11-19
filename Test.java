package simpledb.buffer;

import simpledb.file.Block;
import simpledb.server.SimpleDB;

public class Test {

	public static void main(String[] args) {
		SimpleDB.initFileLogAndBufferMgr("test1");
		int numOfBuffers = 4;
		BufferMgr buffer = new BufferMgr(numOfBuffers);
		Block x = new Block("X",1);
		Block z = new Block("Z",2);
		Block y = new Block("Y",3);
		Block a = new Block("A",4);
		Block b = new Block("B",5);
		
		Buffer buf = buffer.pin(x,(long)2);
		buffer.unpin(buf);
		
		buf = buffer.pin(z,(long)5);
		buffer.unpin(buf);
		
		buf = buffer.pin(y,(long)10);
		buffer.unpin(buf);
		
		buf = buffer.pin(a,(long)15);
		buffer.unpin(buf);
		
		buf = buffer.pin(b,(long)20);
		buffer.unpin(buf);
		
		buf = buffer.pin(y,(long)25);
		buffer.unpin(buf);
		
		buf = buffer.pin(a,(long)30);
		buffer.unpin(buf);
		
		buf = buffer.pin(z,(long)35);
		buffer.unpin(buf);
		
		buf = buffer.pin(x,(long)40);
		buffer.unpin(buf);
		
		BasicBufferMgr.print();
	}
}
