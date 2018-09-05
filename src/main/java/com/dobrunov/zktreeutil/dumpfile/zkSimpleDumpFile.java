package com.dobrunov.zktreeutil.dumpfile;

import com.dobrunov.zktreeutil.TreeNode;
import com.dobrunov.zktreeutil.zNode;
import com.dobrunov.zktreeutil.zkUpload;
import org.apache.zookeeper.data.Stat;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author kostapc
 * created: 05.09.2018
 */
public class zkSimpleDumpFile implements zkDumpFile {
  private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(zkUpload.class);

  private static final String COMMENT_STR = "#";
  private static final String CMD_PREFIX = "cmd=";
  private static final String PATH_PREFIX = "path=";
  private static final String VAL_PREFIX = "val=";
  private static final Integer ZERO = 0;

  private String path;
  private TreeNode<zNode> root = new TreeNode<>(null);

  public zkSimpleDumpFile(String path) {
    this.path = path;
    this.root.data = new zNode("","/", null);
    this.root.data.path = "/";
  }

  @Override
  public TreeNode<zNode> getRootNode() {
    parse(path);
    return root;
  }

  public void parse(String file) {
    try(BufferedReader br = new BufferedReader(new FileReader(file))) {
      String lineStr;
      while ((lineStr = br.readLine()) != null) {
        lineStr = lineStr.trim();
        parseLine(lineStr);
      }
    } catch (IOException ioeEx) {
      logger.error("Error read script file:", ioeEx);
    }
  }

  private void parseLine(String line) {
    if(line != null && !line.isEmpty()) {
      if(!COMMENT_STR.equals(line.substring(ZERO, COMMENT_STR.length()))) {
        int pathPos = line.indexOf(PATH_PREFIX);
        int valPos = line.indexOf(VAL_PREFIX);

        byte[] val = null;
        if(valPos >= ZERO) {
          val = line.substring(valPos + VAL_PREFIX.length(), line.length()).trim().getBytes();
        } else {
          valPos = line.length();
        }

        //String cmd = line.substring(cmdPos + CMD_PREFIX.length(), pathPos).trim();
        String path = line.substring(pathPos + PATH_PREFIX.length(), valPos).trim();
        if(path.equals("/")) {
          return;
        }
        //path = path.substring(0, path.lastIndexOf('/')+1);
        String name = path.substring(path.lastIndexOf('/')+1,path.length());
        //String[] parts = path.split("/");
        TreeNode<zNode> node = findByPath(path);
        zNode zn = new zNode(name, path, val);
        node.addChild(zn);

      }
    }
  }

  private TreeNode<zNode> findByPath(String path) {
    String[] levels = path.split("/");
    TreeNode<zNode> start = root;
    for (String level : levels) {
      if(start.children==null) {
        return start;
      }
      for (TreeNode<zNode> child : start.children) {
        if(child.data.name.equals(level)) {
          start = child;
          break;
        }
      }
    }
    return start;
  }

}
