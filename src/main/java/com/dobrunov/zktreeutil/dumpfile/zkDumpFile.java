package com.dobrunov.zktreeutil.dumpfile;

import com.dobrunov.zktreeutil.TreeNode;
import com.dobrunov.zktreeutil.zNode;

/**
 * @author Konstantin.Lychagin
 * created: 05.09.2018
 */
public interface zkDumpFile {
  TreeNode<zNode> getRootNode();
}
