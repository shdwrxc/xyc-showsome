package org.xyc.showsome.pecan.zookeeper.sample2;

import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Test Out Zookeeper API example Client
 *
 * ZKClientTest Test Class
 *
 * @author Binu George
 * @since 2016
 * @version 1.0
 * http://www.java.globinch.com. All rights reserved
 */
public class ZKClientTest {

    private static ZKClientManagerImpl zkmanager = new ZKClientManagerImpl();
    // ZNode Path
    private String path = "/gbZnode";
    byte[] data = "Client Data".getBytes();

    /**
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for
     * {@link com.globinch.zoo.client.ZKClientManagerImpl#create(String, byte[])}
     * .
     *
     * @throws InterruptedException
     * @throws KeeperException
     */
    @Test
    public void testCreate() throws KeeperException, InterruptedException {
        // data in byte array

        zkmanager.create(path, data);
        Stat stat = zkmanager.getZNodeStats(path);
        assertNotNull(stat);
        zkmanager.delete(path);
    }

    /**
     * Test method for
     * {@link com.globinch.zoo.client.ZKClientManagerImpl#getZNodeStats(String)}
     * .
     *
     * @throws InterruptedException
     * @throws KeeperException
     */
    @Test
    public void testGetZNodeStats() throws KeeperException,
            InterruptedException {
        zkmanager.create(path, data);
        Stat stat = zkmanager.getZNodeStats(path);
        assertNotNull(stat);
        assertNotNull(stat.getVersion());
        zkmanager.delete(path);

    }

    /**
     * Test method for
     * {@link com.globinch.zoo.client.ZKClientManagerImpl#getZNodeData(String)}
     * .
     * @throws InterruptedException
     * @throws KeeperException
     */
    @Test
    public void testGetZNodeData() throws KeeperException, InterruptedException {
        zkmanager.create(path, data);
        String data = (String)zkmanager.getZNodeData(path,false);
        assertNotNull(data);
        zkmanager.delete(path);
    }

    /**
     * Test method for
     * {@link com.globinch.zoo.client.ZKClientManagerImpl#update(String, byte[])}
     * .
     * @throws InterruptedException
     * @throws KeeperException
     */
    @Test
    public void testUpdate() throws KeeperException, InterruptedException {
        zkmanager.create(path, data);
        String data = "Updated Data";
        byte[] dataBytes = data.getBytes();
        zkmanager.update(path, dataBytes);
        String retrivedData = (String)zkmanager.getZNodeData(path,false);
        assertNotNull(retrivedData);
        zkmanager.delete(path);
    }

    /**
     * Test method for
     * {@link com.globinch.zoo.client.ZKClientManagerImpl#getZNodeChildren(String)}
     * .
     * @throws InterruptedException
     * @throws KeeperException
     */
    @Test
    public void testGetZNodeChildren() throws KeeperException, InterruptedException {
        zkmanager.create(path, data);
        List<String> children= zkmanager.getZNodeChildren(path);
        assertNotNull(children);
        zkmanager.delete(path);
    }

    /**
     * Test method for
     * {@link com.globinch.zoo.client.ZKClientManagerImpl#delete(String)}
     * .
     * @throws InterruptedException
     * @throws KeeperException
     */
    @Test
    public void testDelete() throws KeeperException, InterruptedException {
        zkmanager.create(path, data);
        zkmanager.delete(path);
        Stat stat = zkmanager.getZNodeStats(path);
        assertNull(stat);
    }

}
