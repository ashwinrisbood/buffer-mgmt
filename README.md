# Systems Project - Buffer & Log Management

## Description:

### Task 1: Buffer Management
1.  Use  a  Map  data  structure  to  keep  track  of  the  buffer  pool
2.  Modify the buffer replacement policy to the LRU (K=2) - Least Recently Used (K) policy

### Task 2: Log Management
Modify SimpleDB to implement the design where the log manager pin a buffer to the last log block, and let the buffer manager handle the disk accesses. 


## Group Q - Unity ID :

+ Yu-Lun Hong (yhong3)
+ Ashwin Risbood (arisboo)
+ Huy Tu (hqtu)
+ Xi Yang (yxi2)

## List of files Changed/Created :

```
├── simpledb\
|   ├── buffer\ 
|       ├── Buffer.java             (revised)
|       ├── BufferMgr.java          (revised)
|       ├── BasicBufferMgr.java     (revised)
|       ├── TestBufferMgr1.java     (new)
|       ├── TestBufferMgr2.java     (new)
|   ├── file\ 
|       ├── Page.java               (revised)  
|   ├── log\ 
|       ├── BasicLogRecord.java     (revised)
|       ├── LogIterator.java        (revised)
|       ├── LogMgr.java             (revised)
|       ├── LogFormatter.java       (new)
|       ├── TestLogMgr.java         (new)
|       ├── BasicLogRecordOrig.java (orignal)		
|       ├── LogIteratorOrig.java    (orignal)		
|       ├── LogMgrOrig.java         (orignal)
```

## Testing: 
### Testing for Task 1 - Buffer Replacement Policy

1. Create a list of files-blocks.
2. Check the number of available buffers initially. All but one should be available as only
one of them has been pinned by the logmgr yet.
3. Keep pinning buffers one by one and check the number of available buffers.
4. Unpin a few buffers and add more buffers to see if you are still getting an exception or not.
5. When all buffers have been pinned, if pin request is made again, throw an exception
6. Try to pin a new buffer again, and check your replacement policy while seeing which
currently unpinned buffer is replaced.

### Testing for Task 2 - Log Management Policy

1. Create a logMgrPage log manager by original method with a new allocated page.
2. Append a string "123" to the page by setString().
3. Create a logMgrBuffer log manager by revised method with a buffer in bufferpool.
4. Append a string "123" to the buffer by setString().
5. Compare the contents in page and buffer, if they are the same, 
print "** Log management by Page & Buffer got the same results! **".
