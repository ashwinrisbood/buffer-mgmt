package simpledb.buffer;

import simpledb.file.Block;
import simpledb.server.SimpleDB;

/**
 * added to test Task 1: Buffer Replacement Policy
 * @author team Q
 */
public class Test2 {

	/**
	 * Assuming the size of bufferpool is 8
	 * added to test Task 1
	 *  case 1: Create a list of files-blocks.
	 *  case 2: Check the number of available buffers initially.
	 *  case 3: Keep pinning buffers one by one and check the number of available buffers.
	 *  case 4: Unpin a few buffers and see if you are still getting an exception or not.
	 *  case 5: When all buffers have been pinned, if pin request is made again, throw an exception
	 *  case 6: Try to pin a new buffer again, and check your replacement policy 
	 *          while seeing which currently unpinned buffer is replaced. 
     * @author team Q
	 */
	public static void main(String[] args) {
		SimpleDB.initFileLogAndBufferMgr("test2");
		int numOfBuffers = 8;
		BufferMgr buffer = new BufferMgr(numOfBuffers);
		// Test 1
		System.out.println(" *** Test 1 *** ");
		Block blk1 = new Block("blk1",1);
		Block blk2 = new Block("blk2",2);
		Block blk3 = new Block("blk3",3);
		Block blk4 = new Block("blk4",4);
		Block blk5 = new Block("blk5",5);
		Block blk6 = new Block("blk6",6);
		Block blk7 = new Block("blk7",7);
		Block blk8 = new Block("blk8",8);
		Block blk9 = new Block("blk9",9);
		Block blk10 = new Block("blk10",10);
		
		System.out.println("1. Created File Blocks.");
		
		// Test 2
<<<<<<< HEAD
		System.out.println("2. Check the number of available buffers initially: " + buffer.available());
=======
		System.out.println(" *** Test 2 *** ");
		System.out.println("Check the number of available buffers initially: " + buffer.available());
>>>>>>> db541c2319f78db879838e7dfb2a3a418e403a5d
		System.out.println();
		
		
		// Test 3
		System.out.println("3. Keep pinning buffers one by one and check the number of available buffers.");
		Buffer buf1 = buffer.pin(blk1);
		System.out.println("Available buffers : " + buffer.available());
		Buffer buf2 = buffer.pin(blk2);
		System.out.println("Available buffers : " + buffer.available());
		Buffer buf3 = buffer.pin(blk3);
		System.out.println("Available buffers : " + buffer.available());
		Buffer buf4 = buffer.pin(blk4);
		System.out.println("Available buffers : " + buffer.available());
		Buffer buf5 = buffer.pin(blk5);
		System.out.println("Available buffers : " + buffer.available());
		Buffer buf6 = buffer.pin(blk6);
		System.out.println("Available buffers : " + buffer.available());
		Buffer buf7 = buffer.pin(blk7);
		System.out.println("Available buffers : " + buffer.available());
		Buffer buf8 = buffer.pin(blk8);
		System.out.println("Available buffers : " + buffer.available() + "\n");
		
		//System.out.println(buf8.getInt(0));
		
		
		// Test 3
		System.out.println(" *** Test 3 *** ");
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
		System.out.println("Available buffers : " + buffer.available());
		
		// Test 4
		Buffer[] newb = new Buffer[2];
		newb = test4(buffer, buf1, buf2, blk9, blk10);
		
		// Test 5
	    test5(buffer, blk1);
	    
	    // Test 6
	    System.out.println("6. Try to pin a new buffer again, and check your replacement policy \r\n" + 
				"while seeing which currently unpinned buffer is replaced. ");
	    buf4 = buffer.pin(blk4);
		buf3 = buffer.pin(blk3);
		buf4 = buffer.pin(blk4);
		buf7 = buffer.pin(blk7);
		buf5 = buffer.pin(blk5);
		buffer.unpin(newb[1]);
		buffer.unpin(newb[0]);
		buffer.unpin(buf8);
		buffer.unpin(buf7);
		buffer.unpin(buf6);
		buffer.unpin(buf5);
		buffer.unpin(buf4);
		buffer.unpin(buf7);
		buffer.unpin(buf4);
		buffer.unpin(buf3);
		buffer.unpin(buf3);
		buf1 = buffer.pin(blk1);
		buf2 = buffer.pin(blk2);
		BasicBufferMgr.print();
		System.out.println();
	}
	
	/**
	 * added to test case 5
	 * @author team Q
	 */
	public static void test5(BufferMgr buffer, Block blk) {
		try {
			// Should throw exception
			System.out.println("5. When all buffers have been pinned, if pin request is made again, throw an exception");
			
			Buffer newbuff = buffer.pin(blk);
			BasicBufferMgr.print();
			System.out.println("Unexpected : Exception Expected.");
			System.out.println();
		} catch (simpledb.buffer.BufferAbortException e) {
			// Expected 
			System.out.println("Buffer Abort Exception thrown - TimedOut");
			System.out.println("Successfully throw exception when attempting to pin a block with no available buffer.");
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * added to test case 4
	 * @author team Q
	 */
	public static Buffer[] test4(BufferMgr buffer, Buffer buf1, Buffer buf2, Block blk9, Block blk10) {
		try {
			System.out.println();
			System.out.println("4. Unpin a few buffers and see if you are still getting an exception or not.");
			System.out.println("Unpinned buffer 1");
			buffer.unpin(buf1);
			System.out.println("Unpinned buffer 2");
			buffer.unpin(buf2);	
			System.out.println("Available buffers : " + buffer.available());
			Buffer newbuff1 = buffer.pin(blk9);
			Buffer newbuff2 = buffer.pin(blk10);
			System.out.println("Available buffers : " + buffer.available());
			BasicBufferMgr.print();	
			System.out.println("Successfully pinned buffers after unpinning a few of them.");
			System.out.println();
			Buffer[] b = {newbuff1, newbuff2};
			return b;
		} catch (simpledb.buffer.BufferAbortException e) {
			// Unexpected 
			System.out.println("Buffer Abort Exception thrown - TimedOut");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
