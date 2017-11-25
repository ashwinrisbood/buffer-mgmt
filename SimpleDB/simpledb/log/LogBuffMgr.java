package simpledb.log;

import java.util.Map;

import simpledb.buffer.Buffer;
import simpledb.buffer.BufferMgr;
import simpledb.file.Block;
import simpledb.server.SimpleDB;

public class LogBuffMgr {
	public LogMgr mainmgr;
	public static BufferMgr lgbuffMgr=new BufferMgr(Integer.MAX_VALUE);
	
	 public Buffer addlog(Block temp) {
		 Buffer logbuff=new Buffer();
	         if(lgbuffMgr.bufferMgr.bufferPoolMap.containsKey(temp))
	         {
	 	 		mainmgr.flush();
	 	 		lgbuffMgr.bufferMgr.bufferPoolMap.clear();
	        	lgbuffMgr.bufferMgr.bufferPoolMap.put(temp, logbuff);
	 		 }
	         else
	         {
	        	 lgbuffMgr.bufferMgr.bufferPoolMap.put(temp, logbuff);
	         }
	         return logbuff;
	     }
	 public static void print()
	 {
		 for(Map.Entry<Block,Buffer> entry:lgbuffMgr.bufferMgr.bufferPoolMap.entrySet())
		 {
			 System.out.print(entry.getKey().fileName()+" ");
		 }
		 System.out.println();
	 }
}
