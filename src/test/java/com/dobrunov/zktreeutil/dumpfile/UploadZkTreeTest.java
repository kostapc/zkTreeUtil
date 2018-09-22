package com.dobrunov.zktreeutil.dumpfile;

import com.dobrunov.zktreeutil.zkUpload;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;

import java.io.File;
import java.io.IOException;

/**
 * 2018-09-22
 *
 * @author KostaPC
 * c0f3.net
 */
public class UploadZkTreeTest {
    private static int zookeeperPort = 2181;

    @ClassRule
    public static GenericContainer zookeeper = new GenericContainer("zookeeper").withExposedPorts(zookeeperPort);

    @Test
    public void testFileUpload() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("example.zk").getFile());

        zkDumpFile dumpFile = new zkSimpleDumpFile(file.getAbsolutePath());
        dumpFile.getRootNode();
        String connectString = zookeeper.getContainerIpAddress()+":"+zookeeper.getMappedPort(zookeeperPort);
        System.out.println("connect string: "+connectString);
        zkUpload upload = new zkUpload(connectString, dumpFile);
        upload.upload();
        System.out.println("breakpoint!");
    }
}
