package com.dobrunov.zktreeutil;

import com.dobrunov.zktreeutil.dumpfile.zkDumpFile;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.Semaphore;

/**
 * @author kostapc
 * created: 05.09.2018
 */
public class zkUpload {

  private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(zkUpload.class);

  private final ZooKeeper zk;
  private final zkDumpFile dumpFile;
  //private final Semaphore semaphore;
    private final Object flag = new Object();

  public zkUpload(String connectString, zkDumpFile dumpFile) {
    try {
      /*semaphore = new Semaphore(1);
      semaphore.acquire();*/
      this.zk = new ZooKeeper(connectString, 10000, this::wather);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
    this.dumpFile = dumpFile;
  }

  public void upload() {
    try {
        synchronized (flag) {
            flag.wait();
        }
      writeNode(
          "",dumpFile.getRootNode()
      );
    } catch (InterruptedException e) {
      throw new IllegalStateException(e);
    }
  }

  private void wather(WatchedEvent watchedEvent) {
    logger.info("wather called: "+watchedEvent);
    if(watchedEvent.getState().equals(Watcher.Event.KeeperState.SyncConnected)) {
        //semaphore.release();
        synchronized (flag) {
            flag.notifyAll();
        }
    }
  }

  private void writeNode(String inPath, TreeNode<zNode> node) throws InterruptedException {
      zNode zn = node.data;
      String path = inPath;
      if(zn.name!=null && zn.name.length()>0) {
        path = inPath+"/"+node.data.name;
        logger.info("writing node: \""+zn.name+"\" ("+path+" # "+inPath+")");
        //semaphore.acquire();
        try {
          if(node.isLeaf()) {
            if (zk.exists(path, (e) -> {
              logger.warn("checking node " + zn.name + "...");
            }) != null) {
              zk.delete(path, -1);
            }
          }
          ACL acl = new ACL();
          acl.setId(new Id( "", UUID.randomUUID().toString()));
          acl.setPerms(0);
          zk.create(
                path, zn.data, Arrays.asList(acl), CreateMode.PERSISTENT
          );
          //semaphore.release();
        } catch (KeeperException | InterruptedException e) {
          logger.error("error while inserting node " + zn.path, e);
        }
      }
      if(node.children==null){
        return;
      }
      for (TreeNode<zNode> treeNode : node.children) {
        writeNode(path,treeNode);
      }
  }

}
