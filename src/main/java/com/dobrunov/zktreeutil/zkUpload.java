package com.dobrunov.zktreeutil;

import com.dobrunov.zktreeutil.dumpfile.zkDumpFile;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;

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
        "",dumpFile.getRootNode()
    );
  }

  private void writeNode(String inPath, TreeNode<zNode> node) {
      zNode zn = node.data;
      String path = inPath;
      if(zn.name!=null && zn.name.length()>0) {
        path = inPath+"/"+node.data.name;
        logger.info("writing node: \""+zn.name+"\" ("+path+" # "+inPath+")");
        try {
          if(node.isLeaf()) {
            if (zk.exists(path, (e) -> {
              logger.warn("checking node " + zn.name + "...");
            }) != null) {
              zk.delete(path, -1);
            }
          }
          ACL acl = new ACL();
          acl.setId(new Id());
          zk.create(
                path, zn.data, Arrays.asList(acl), CreateMode.PERSISTENT
          );
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
