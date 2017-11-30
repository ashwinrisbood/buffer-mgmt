package simpledb.log;

import simpledb.server.SimpleDB;
import simpledb.buffer.Buffer;
import simpledb.buffer.BufferMgr;
import simpledb.file.*;
import static simpledb.file.Page.*;
import java.util.*;

import java.nio.charset.StandardCharsets;
import java.nio.charset.Charset;
import java.nio.ByteBuffer;

/**
 * The low-level log manager.
 * This log manager is responsible for writing log records
 * into a log file.
 * A log record can be any sequence of integer and string values.
 * The log manager does not understand the meaning of these
 * values, which are written and read by the
 * {@link simpledb.tx.recovery.RecoveryMgr recovery manager}.
 * @author Edward Sciore
 */

/**
 * Task 2: updated to let LogMgr use buffer pool instead of page for logging
 * @author Team number Q
 */
public class LogMgr implements Iterable<BasicLogRecord> {
   /**
    * The location where the pointer to the last integer in the page is.
    * A value of 0 means that the pointer is the first value in the page.
    */
   public static final int LAST_POS = 0;
   
   //public static final int LOGBUFFERLENGTH = 4;

   private String logfile;
   /**
    * Task 2: initializing the log bufferMgr
    * @author Team number Q
    */
   //UPDATED HERE
   //private Page mypage = new Page(); //allocate mypage in memory
   private BufferMgr mybuffMgr = new BufferMgr(8);
   //private BufferMgr mybuffMgr = new SimpleDB.bufferMgr(8);
   private static Buffer mybuf = new Buffer();
   private Block currentblk;
   private int currentpos;
   
   /**
    * Task 2: initializing the logFormatter
    * @author Team number Q
    */
   //UPDATED HERE
   private LogFormatter fmtr = new LogFormatter();

   /**
    * Creates the manager for the specified log file.
    * If the log file does not yet exist, it is created
    * with an empty first block.
    * This constructor depends on a {@link FileMgr} object
    * that it gets from the method
    * {@link simpledb.server.SimpleDB#fileMgr()}.
    * That object is created during system initialization.
    * Thus this constructor cannot be called until
    * {@link simpledb.server.SimpleDB#initFileMgr(String)}
    * is called first.
    * @param logfile the name of the log file
    */
   
   /**
    * Task 2: update the log manager to use buffer to handle disk writes
    * @author Team number Q
    */
   public LogMgr(String logfile) {
      this.logfile = logfile;
      System.out.println("logfile: " + logfile);
      int logsize = SimpleDB.fileMgr().size(logfile); // number of blocks in logfile
      System.out.println("logsize: " + logsize);
      if (logsize == 0)
         appendNewBlock();
      else {
         currentblk = new Block(logfile, logsize-1); // next log block in reverse order
         //UPDATED HERE
         //mypage.read(currentblk); //populates mypage with contents of currentblk
         System.out.println("currentblk: " + currentblk.toString());
         //mybuf = mybuffMgr.pin(currentblk);
         mybuf = mybuffMgr.pinNew(logfile, fmtr);
         
         String ts = mybuf.getString(0);
         System.out.println("Test dummy String: " + ts);
         
         currentpos = getLastRecordPosition() + INT_SIZE; // integer value at offset of mypage + INT_SIZE
         System.out.println("currentpos: " + currentpos);
         
         printLogPageBuffer();
      }
   }
   
   /**
    * Ensures that the log records corresponding to the
    * specified LSN has been written to disk.
    * All earlier log records will also be written to disk.
    * @param lsn the LSN of a log record
    */
   public void flush(int lsn) {
      if (lsn >= currentLSN()) //earlier log records have larger block number? 
         flush();
   }

   /**
    * Returns an iterator for the log records,
    * which will be returned in reverse order starting with the most recent.
    * @see java.lang.Iterable#iterator()
    */
   public synchronized Iterator<BasicLogRecord> iterator() {
      flush();
      return new LogIterator(currentblk);
   }

   /**
    * Appends a log record to the file.
    * The record contains an arbitrary array of strings and integers.
    * The method also writes an integer to the end of each log record whose value
    * is the offset of the corresponding integer for the previous log record.
    * These integers allow log records to be read in reverse order.
    * @param rec the list of values
    * @return the LSN of the final value
    */
   
   /**
    * Task 2: updated to use append a log to the buffer
    * @author Team number Q
    */
   public synchronized int append(Object[] rec) {
      int recsize = INT_SIZE;  // 4 bytes for the integer that points to the previous log record
      for (Object obj : rec)
         recsize += size(obj);
      System.out.println("currentpos: " + currentpos);
      System.out.println("recsize: " + recsize);
      System.out.println("BLOCK_SIZE: " + BLOCK_SIZE);
      if (currentpos + recsize >= BLOCK_SIZE){ // the log record doesn't fit,
         //flush();        // so move to the next block.
         appendNewBlock();
      }
      for (Object obj : rec)
         appendVal(obj);
      finalizeRecord();
      return currentLSN(); //LSN of the most recent log record
   }
   
   /**
    * Adds the specified value to the page at the position denoted by
    * currentpos.  Then increments currentpos by the size of the value.
    * @param val the integer or string to be added to the page
    */
   /**
    * Task 2: updated to set the log value to the buffer
    * @author Team number Q
    */
   //UPDATED HERE
   private void appendVal(Object val) {
	   System.out.println("Value to be set: " + val);
	   if (val instanceof String) {
		   //mypage.setString(currentpos, (String)val);
		   mybuf.setString(currentpos, (String)val, -1, -1);
		   System.out.println("Setting as String");
	   }
	   else {
		   //mypage.setInt(currentpos, (Integer)val);
		   mybuf.setInt(currentpos, (Integer)val, -1, -1);
		   System.out.println("Setting as Int");
	   }
	   currentpos += size(val);
	   System.out.println("currentpos after setting: " + currentpos);
   }

   /**
    * Calculates the size of the specified integer or string.
    * @param val the value
    * @return the size of the value, in bytes
    */
   private int size(Object val) {
      if (val instanceof String) {
         String sval = (String) val;
         return STR_SIZE(sval.length());
      }
      else
         return INT_SIZE;
   }

   /**
    * Returns the LSN of the most recent log record.
    * As implemented, the LSN is the block number where the record is stored.
    * Thus every log record in a block has the same LSN.
    * @return the LSN of the most recent log record
    */
   private int currentLSN() {
	   return currentblk.number();
   }

   /**
    * Writes the current page to the log file.
    */
   /**
    * Task 2: updated to let buffer manager to manage write to disk
    * @author Team number Q
    */
   private void flush() {
	   //UPDATED HERE
	   //mypage.write(currentblk);
	   mybuffMgr.flushAll(-1);
   }

   /**
    * Clear the current page, and append it to the log file.
    */
   /**
    * Task 2: updated to use log buffer with pin and unpin
    * @author Team number Q
    */
   private void appendNewBlock() {
	   //UPDATED HERE
	   //setLastRecordPosition(0);
	   currentpos = INT_SIZE;
	   //currentblk = mypage.append(logfile);
	   if (mybuf != null) {
		   mybuffMgr.unpin(mybuf);
	   }
	   mybuf = mybuffMgr.pinNew(logfile, fmtr);
	   currentblk = mybuf.block();
   }

   /**
    * Sets up a circular chain of pointers to the records in the page.
    * There is an integer added to the end of each log record
    * whose value is the offset of the previous log record.
    * The first four bytes of the page contain an integer whose value
    * is the offset of the integer for the last log record in the page.
    */
   /**
    * Task 2: updated for the last log and testing
    * @author Team number Q
    */
   private void finalizeRecord() {
	   //UPDATED HERE
	   //mypage.setInt(currentpos, getLastRecordPosition());
	   mybuf.setInt(currentpos, getLastRecordPosition(), -1, -1);
	   setLastRecordPosition(currentpos);
	   currentpos += INT_SIZE;
	   System.out.println("Test finalizeRecord: ");
	   printLogPageBuffer();
   }
   
   /**
    * Edited for task 2 to get from log buffer
    * @author Team number Q
    */
   private int getLastRecordPosition() {
	   //UPDATED HERE
	   //return mypage.getInt(LAST_POS);
	   return mybuf.getInt(LAST_POS);
   }
   
  /**
   * Edited for task 2 to set dummy number on log buffer
   * @author Team number Q
   */
   private void setLastRecordPosition(int pos) {
	   //UPDATED HERE
	   //mypage.setInt(LAST_POS, pos);
	   mybuf.setInt(LAST_POS, pos, -1, -1);
   }
   
   	/**
    * For part 2 testing, print out the result of log page buffer
    * @author Team number Q
    */
   	static void printLogPageBuffer(){
	   
	      System.out.println("***********************************************************");
	      System.out.println("  Buffer number pinned to the log block: " + mybuf.getPins());
	      ByteBuffer byteBuffer = mybuf.getContents();
	      
	      System.out.println("  Contents of buffer: ");
	      readBuffer(byteBuffer);
	      System.out.println("***********************************************************");
	   }
   	
    /**
     * Task 2: to print log buffer
     * @author Team number Q
     */
   	static void readBuffer(ByteBuffer destValue)
	{
   		destValue.rewind();
   		while (destValue.hasRemaining())
   		     System.out.println(destValue.get());
    }
}
