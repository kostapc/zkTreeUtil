package com.dobrunov.zktreeutil;

import com.dobrunov.zktreeutil.dumpfile.zkDumpFile;
import com.dobrunov.zktreeutil.dumpfile.zkSimpleDumpFile;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * @author kostapc
 * created: 05.09.2018
 */
public class zkImportFromFile {

  public static void main(String[] args) throws IOException {
    zkDumpFile dumpFile = new zkSimpleDumpFile("D:\\home\\documents\\aaaaaaaaa.zk");
    dumpFile.getRootNode();
    System.out.println("breakpoint");
    /*ZooKeeper zk = new ZooKeeper("localhost:2181", 10000, null);
    zkUpload upload = new zkUpload(zk, dumpFile);
    upload.upload();*/
  }

}
