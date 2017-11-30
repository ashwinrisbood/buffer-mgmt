package simpledb.buffer;

import simpledb.file.Block;
import simpledb.server.SimpleDB;

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
		

		//buf.setInt(4, 4, 4, 4);
		//System.out.println(buf.getInt(4));
		//buf.setString(4, "str", 4, 4);
		//System.out.println(buf.getString(4));
		BasicBufferMgr.print();
	}
}
