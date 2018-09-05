package com.dobrunov.zktreeutil;

import com.dobrunov.zktreeutil.dumpfile.zkDumpFile;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;

import java.util.Arrays;

/**
 * @author kostapc
 * created: 05.09.2018
 */
public class zkUpload {

  final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(zkUpload.class);

  private final ZooKeeper zk;
  private final zkDumpFile dumpFile;

  public zkUpload(ZooKeeper zk, zkDumpFile dumpFile) {
    this.zk = zk;
    this.dumpFile = dumpFile;
  }

  public void upload() {
    writeNode(
        dumpFile.getRootNode()
    );
  }

  private void writeNode(TreeNode<zNode> node) {
    if(node.isLeaf()) {
      zNode zn = node.data;
      try {
        if(zk.exists(zn.path,null)!=null) {
          zk.delete(zn.path, -1);
        }
        ACL acl = new ACL();
        zk.create(
            zn.path, zn.data, Arrays.asList(acl), CreateMode.PERSISTENT
        );
      } catch (KeeperException | InterruptedException e) {
        logger.error("error while inserting node "+zn.path, e);
      }
    } else {
      for (TreeNode<zNode> treeNode : node) {
        writeNode(treeNode);
      }
    }
  }

}
