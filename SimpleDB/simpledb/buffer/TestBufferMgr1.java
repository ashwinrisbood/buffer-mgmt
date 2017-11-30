package simpledb.buffer;

import simpledb.file.Block;
import simpledb.server.SimpleDB;

/**
 * added to test Task 1: Buffer Replacement Policy
 * @author team Q
 */
public class TestBufferMgr1 {

	public static void main(String[] args) {
		SimpleDB.initFileLogAndBufferMgr("test1");
		int numOfBuffers = 4;
		BufferMgr buffer = new BufferMgr(numOfBuffers);
		Block x = new Block("X",1);
		Block z = new Block("Z",2);
		Block y = new Block("Y",3);
		Block a = new Block("A",4);
		Block b = new Block("B",5);
		
		Buffer buf = buffer.pin(x);
		buffer.unpin(buf);
		
		buf = buffer.pin(z);
		buffer.unpin(buf);
		
		buf = buffer.pin(y);
		buffer.unpin(buf);
		
		buf = buffer.pin(a);
		buffer.unpin(buf);
		
		buf = buffer.pin(b);
		buffer.unpin(buf);
		
		buf = buffer.pin(y);
		buffer.unpin(buf);
		
		buf = buffer.pin(a);
		buffer.unpin(buf);
		
		buf = buffer.pin(z);
		buffer.unpin(buf);
		
		buf = buffer.pin(x);
		buffer.unpin(buf);
		

		BasicBufferMgr.print();
	}
}
