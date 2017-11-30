# Systems Project - Buffer & Log Management
lru-k and logging in simpleDB

## Unity ID :

+ Yu-Lun Hong (yhong3)
+ Ashwin Risbood (arisboo)
+ Huy Tu (hqtu)
+ Xi Yang (yxi2)

## List of files Changed/Created :

```
├── simpledb\
|   ├── buffer\ 
|       ├── Buffer.java
|       ├── BufferMgr.java
|       ├── BasicBufferMgr.java
|       ├── TestBufferMgr1.java (new)
|       ├── TestBufferMgr2.java (new)
|   ├── file\ 
|       ├── Page.java 
|   ├── log\ 
|       ├── BasicLogRecord.java
|       ├── LogFormatter.java (new)
|       ├── LogIterator.java
|       ├── LogMgr.java
|       ├── TestLogMgr.java (new)
```

## Testing: 
### Testing for Part 1 - Buffer Replacement Policy

1. Create a list of files-blocks.
2. Check the number of available buffers initially. All but one should be available as only
one of them has been pinned by the logmgr yet.
3. Keep pinning buffers one by one and check the number of available buffers.
4. Unpin a few buffers and add more buffers to see if you are still getting an exception or not.
5. When all buffers have been pinned, if pin request is made again, throw an exception
6. Try to pin a new buffer again, and check your replacement policy while seeing which
currently unpinned buffer is replaced.

### Testing for Part 2 - Log Management Policy
(optional)
