package simpledb.log;

import java.util.Iterator;

import simpledb.server.SimpleDB;

public class TestLogMgr {
	
	public static void main(String[] args) {
		
		SimpleDB.initFileMgr("test2");
		LogMgr logMgr = new LogMgr("testLog");
		
		logMgr.append(new Object[] {"1","2","3"});
		
	}
}
