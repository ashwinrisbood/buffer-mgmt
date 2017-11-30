package simpledb.log;

import java.util.Iterator;
import java.nio.ByteBuffer;

import simpledb.server.SimpleDB;

public class TestLogMgr {
	
	public static void main(String[] args) {
		
		// test1: Log management by Page
		SimpleDB.initFileMgr("test1");
		LogMgrOrig logMgrPage = new LogMgrOrig("testLog1");
		logMgrPage.append(new Object[] {"123"});
		
		// test2: Log management by Buffer
		SimpleDB.initFileMgr("test2");
		LogMgr logMgrBuffer = new LogMgr("testLog2");
		logMgrBuffer.append(new Object[] {"123"});
		
		// Check if Log management by Page & Buffer get the same results
		
		ByteBuffer pageByteBuffer = logMgrPage.getByteBuffer();
		ByteBuffer bufferByteByffer = logMgrBuffer.getByteBuffer();
		
		int checkPageBuffer = pageByteBuffer.compareTo(bufferByteByffer);
		if (checkPageBuffer == 0) {
			System.out.println("** Log management by Page & Buffer got the same results! **");
		}
	}
}
