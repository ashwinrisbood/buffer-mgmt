package simpledb.log;

import java.util.Iterator;

import simpledb.server.SimpleDB;
/**
 * Task 2: newly created for testing logmgr with buffer
 * @author Team number Q
 */
public class TestLogMgr {
	
	public static void main(String[] args) {
		
		SimpleDB.initFileMgr("test2");
		LogMgr logMgr = new LogMgr("testLog");
		
		logMgr.append(new Object[] {"123"});
		
	}
}
