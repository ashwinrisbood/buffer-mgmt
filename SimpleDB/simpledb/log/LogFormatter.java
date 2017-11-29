package simpledb.log;

import simpledb.file.*;
import simpledb.buffer.PageFormatter;
import static simpledb.file.Page.*;

public class LogFormatter implements PageFormatter{

	public void format(Page p) {
		p.setString(0, "abc");
	}
}
