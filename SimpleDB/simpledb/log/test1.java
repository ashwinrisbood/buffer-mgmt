package simpledb.log;

import simpledb.buffer.Buffer;
import simpledb.buffer.BufferAbortException;
import simpledb.buffer.BufferMgr;
import simpledb.file.Block;
import simpledb.server.SimpleDB;

public class test1 {
	public static void main(String args[])
	{
	SimpleDB.initFileLogAndBufferMgr("test1");
	Block blk1=new Block("A", 1);
	Block blk2=new Block("B", 2);
	LogBuffMgr logbuffmgr=new LogBuffMgr();
	try {
		Buffer buf1=logbuffmgr.addlog(blk1);
		buf1.setInt(4, 1, 1, 1);
		buf1.setString(4, "val1", 1, 1);
		//buf1.setInt(4, 1, 1, 1);
		//buf1.setString(4, "val1", 1, 1);
		//Buffer buf2=logbuffmgr.lgbuffMgr.pin(blk2);
	//	buf1.setInt(8, 2, 2, 2);
		//buf1.setString(8, "val2", 2, 2);
		//logbuffmgr.print();
	}catch (BufferAbortException e) {
		System.out.println("\nBufferAbortException: " + e.getStackTrace());
	}
}
}