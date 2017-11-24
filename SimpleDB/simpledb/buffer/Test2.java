package simpledb.buffer;

import simpledb.file.Block;
import simpledb.server.SimpleDB;
/*pin(blk1), pin(blk2), pin(blk3), pin(blk4), pin(blk5),
pin(blk6), pin(blk7), pin(blk8), pin(blk4), pin(blk2),
pin(blk7), pin(blk1), unpin(blk8), unpin(blk7),
unpin(blk6), unpin(blk5), unpin(blk4), unpin(blk1),
unpin(blk7), unpin(blk4), unpin(blk2), unpin(blk2).
1 1
2 0
3 1
4 0
5 0
6 0
7 0
8 0
*/
public class Test2 {

	public static void main(String[] args) {
		SimpleDB.initFileLogAndBufferMgr("test2");
		int numOfBuffers = 8;
		BufferMgr buffer = new BufferMgr(numOfBuffers);
		// Test 1
		System.out.println(buffer.available());
		System.out.println();
		
		Block blk1 = new Block("blk1",1);
		Block blk2 = new Block("blk2",2);
		Block blk3 = new Block("blk3",3);
		Block blk4 = new Block("blk4",4);
		Block blk5 = new Block("blk5",5);
		Block blk6 = new Block("blk6",6);
		Block blk7 = new Block("blk7",7);
		Block blk8 = new Block("blk8",8);
		Block blk9 = new Block("blk9",9);
		
		// Test 2
		Buffer buf1 = buffer.pin(blk1);
		System.out.println("Available buffers : " + buffer.available() + "\n");
		Buffer buf2 = buffer.pin(blk2);
		System.out.println("Available buffers : " + buffer.available() + "\n");
		Buffer buf3 = buffer.pin(blk3);
		System.out.println("Available buffers : " + buffer.available() + "\n");
		Buffer buf4 = buffer.pin(blk4);
		System.out.println("Available buffers : " + buffer.available() + "\n");
		Buffer buf5 = buffer.pin(blk5);
		System.out.println("Available buffers : " + buffer.available() + "\n");
		Buffer buf6 = buffer.pin(blk6);
		System.out.println("Available buffers : " + buffer.available() + "\n");
		Buffer buf7 = buffer.pin(blk7);
		System.out.println("Available buffers : " + buffer.available() + "\n");
		Buffer buf8 = buffer.pin(blk8);
		System.out.println("Available buffers : " + buffer.available() + "\n");
		
		// Test 3
		try {
			// Should throw exception
			System.out.println("No available buffer, yet attempting to pin a block.");
			Buffer buff = buffer.pin(blk8);
			System.out.println("Unexpected : Exception Expected.");
		} catch (simpledb.buffer.BufferAbortException e) {
			// Expected behavior.
			System.out.println("****** Buffer Abort Exception thrown - TimedOut ******");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		buf4 = buffer.pin(blk4);
		buf2 = buffer.pin(blk2);
		buf4 = buffer.pin(blk4);
		buf7 = buffer.pin(blk7);
		buf1 = buffer.pin(blk1);
		buffer.unpin(buf8);
		buffer.unpin(buf7);
		buffer.unpin(buf6);
		buffer.unpin(buf5);
		buffer.unpin(buf4);
		buffer.unpin(buf1);
		buffer.unpin(buf7);
		buffer.unpin(buf4);
		buffer.unpin(buf2);
		buffer.unpin(buf2);
		Buffer buf9 = buffer.pin(blk9);
		//buffer.unpin(buf);
		
		BasicBufferMgr.print();
		
		
		
		System.out.println();
	}
}
