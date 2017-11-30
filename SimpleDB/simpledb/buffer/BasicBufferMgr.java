package simpledb.buffer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import simpledb.file.*;

/**
 * Manages the pinning and unpinning of buffers to blocks.
 * @author Edward Sciore
 *
 */
/**
 * changes:-
* hash map for buffer pool
* map to store history of refrences
* timeout value for difference between 2 references to the page
* @author team Q
*/
public class BasicBufferMgr {
	// private Buffer[] bufferpool;   replacing the standard array
    public static HashMap<Block,Buffer> bufferPoolMap;     
    private HashMap<Block, Long[]> HIST;					
    private HashMap<Block, Long> las;						
    int bufferpool;
	private int numAvailable;								
	int timeout = 15;										 
   
   
   /**
    * Creates a buffer manager having the specified number 
    * of buffer slots.
    * This constructor depends on both the {@link FileMgr} and
    * {@link simpledb.log.LogMgr LogMgr} objects 
    * that it gets from the class
    * {@link simpledb.server.SimpleDB}.
    * Those objects are created during system initialization.
    * Thus this constructor cannot be called until 
    * {@link simpledb.server.SimpleDB#initFileAndLogMgr(String)} or
    * is called first.
    * @param numbuffs the number of buffer slots to allocate
    */
	/**
	 * changes:-
	* Initialization of all the maps
	* @author team Q
	*/
   BasicBufferMgr(int numbuffs) {						
      bufferpool = numbuffs;
      numAvailable = numbuffs;
      bufferPoolMap = new LinkedHashMap<Block,Buffer>(numbuffs);
      HIST= new HashMap<Block, Long[]>();
      las= new HashMap<Block, Long>();
   }
   
   /**
    * Flushes the dirty buffers modified by the specified transaction.
    * @param txnum the transaction's id number
    */
   /**
    * changes:-
   * changed to flush buffers from map.
   * @author team Q
   */
   synchronized void flushAll(int txnum) {				
	   for (Entry<Block, Buffer> Entry : bufferPoolMap.entrySet()) {
    	 Buffer buff=Entry.getValue();
    	 if (buff.isModifiedBy(txnum))
    		 buff.flush();
     }
   }
   
   /**
    * Pins a buffer to the specified block. 
    * If there is already a buffer assigned to that block
    * then that buffer is used;  
    * otherwise, an unpinned buffer from the pool is chosen.
    * Returns a null value if there are no available buffers.
    * @param blk a reference to a disk block
    * @return the pinned buffer
    */
   /**
   * changes:-
   * edited to insert/remove buffers in the hashmap 
   * @author team Q
   */
   synchronized Buffer pin(Block blk,long time) {
      Buffer buff = findExistingBuffer(blk);
      if (buff == null) {
         buff = chooseUnpinnedBuffer(time);
         if (buff == null)
            return null;
         bufferPoolMap.remove(buff.block());
         buff.assignToBlock(blk);
      }
      if (!buff.isPinned())
         numAvailable--;
      buff.pin();
      
      bufferPoolMap.put(blk, buff);
      
      long present_time=time;
      if(las.containsKey(blk))
      {
    	  long past=las.get(blk);
    	  if(present_time - past < timeout)
    	  {
    		  Long[] history = HIST.get(blk);
    		  history[1]=history[0];
    		  history[0]=time;
    		  HIST.replace(blk,  history);
    	  }
      }  
      else
    	  {
    		  Long[] history= new Long[2];
    		  history[0]=time;
    		  history[1]=Long.MAX_VALUE;
    		  HIST.put(blk,  history);
    	  }
      		las.put(blk,present_time);
      		return buff;
   }
   
   synchronized Buffer pin(Block blk){
		return pin(blk, System.currentTimeMillis());
	}
   
   /**
    * Allocates a new block in the specified file, and
    * pins a buffer to it. 
    * Returns null (without allocating the block) if 
    * there are no available buffers.
    * @param filename the name of the file
    * @param fmtr a page formatter object, used to format the new block
    * @retur
    * n the pinned buffer
    */
   /**
    * changes:-
   * chooseunpinnedBuffer just takes a parameter which is the system time
   * @author team Q
   */
   synchronized Buffer pinNew(String filename, PageFormatter fmtr) {
      Buffer buff = chooseUnpinnedBuffer(System.currentTimeMillis());
      if (buff == null)
         return null;
      buff.assignToNew(filename, fmtr);
      numAvailable--;
      buff.pin();
      return buff;
   }
   
   /**
    * Unpins the specified buffer.
    * @param buff the buffer to be unpinned
    */
   synchronized void unpin(Buffer buff) {
      buff.unpin();
      if (!buff.isPinned())
         numAvailable++;
   }
   
   /**
    * Returns the number of available (i.e. unpinned) buffers.
    * @return the number of available buffers
    */
   int available() {
      return numAvailable;
   }
   /**
    * changes:-
   * just a lookup on the bufferpool instead of traversing the bufferpool
   * @author team Q
   */
   private Buffer findExistingBuffer(Block blk) {			
      return bufferPoolMap.get(blk);
   }
   /**
    * changes:-
   * changed to choose unpinned buffer to replace using lru-k
   * @author team Q
   */
   private Buffer chooseUnpinnedBuffer(long time) 
   {
      if(bufferPoolMap.size()<bufferpool)
      {
    	  return new Buffer();
      }
      else
      {
    	  long present = time;
    	  long max = present;
    	  Buffer newBuff=null;
    	  for (Entry<Block, Buffer> Entry : bufferPoolMap.entrySet()) 
    	  {
    		  	Buffer buff=Entry.getValue();
    		  	long last= las.get(buff.block());
    		  	long lastaccess = find_last_access(buff.block());
    		  	
    		  	if(lastaccess>max && !buff.isPinned() && (present - last) < timeout )
    		  	{
    		  		 max = lastaccess;
    		  		 newBuff = buff;
    		  	}
    		  	
    	  }
    	  return newBuff;
      }      
   }
   /**
   * added to find the last access to the block
   * @author team Q
   */
   private long find_last_access(Block block)
   {
	   Long[] history=HIST.get(block);
	   return history[1];
   }
   /**
   * changes:-
   * added to print blocks in the buffer pool
   * @author team Q
   */
   public static void print(){
	   for(Map.Entry<Block, Buffer> Entry:bufferPoolMap.entrySet())
	   {
	    		  System.out.print(Entry.getKey().fileName()+" ");
	   } 
	      System.out.println();
	}
}
