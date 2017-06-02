package org.xyc.showsome.pecan.zookeeper.sample3;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class ZkConnect {
    private ZooKeeper zk;

    private CountDownLatch connSignal = new CountDownLatch(1);

    /**
     * host should be 127.0.0.1:3000,127.0.0.1:3001,127.0.0.1:3002
     *
     * One of the parameters to the ZooKeeper client library call to create a ZooKeeper session is the session timeout in milliseconds. The client sends a requested timeout, the server responds with the timeout that it can give the client. The current implementation requires that the timeout be a minimum of 2 times the tickTime (as set in the server configuration) and a maximum of 20 times the tickTime. The ZooKeeper client API allows access to the negotiated timeout.
     * 关于超时时间，在new一个zookeeper时传递的超时时间不是最终的，最终服务端会给一个合适的超时时间。一般介于2倍的ticktime和20倍的ticktime之间。这个称为negotiated timeout
     * @param host
     * @return
     * @throws Exception
     */
    public ZooKeeper connect(String host) throws Exception {
        zk = new ZooKeeper(host, 1000 * 60 * 10, new Watcher() {
            public void process(WatchedEvent event) {
                if (event.getState() == KeeperState.SyncConnected) {
                    connSignal.countDown();
                }
            }
        });
        connSignal.await();
        return zk;
    }

    public void close() throws InterruptedException {
        zk.close();
    }

    public void createNode(String path, byte[] data) throws Exception {
        zk.create(path, data, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    public void updateNode(String path, byte[] data) throws Exception {
        zk.setData(path, data, zk.exists(path, true).getVersion());
    }

    public void deleteNode(String path) throws Exception {
        zk.delete(path, zk.exists(path, true).getVersion());
    }

    public static void main(String args[]) throws Exception {
        ZkConnect connector = new ZkConnect();
        ZooKeeper zk = connector.connect("192.168.200.9:2181");
        String newNode = "/deepakDate";
        connector.createNode(newNode, new Date().toString().getBytes());
        List<String> zNodes = zk.getChildren("/", true);
        for (String zNode : zNodes) {
            System.out.println("ChildrenNode " + zNode);
        }
        Stat stat = zk.exists(newNode, true);
        byte[] data = zk.getData(newNode, true, zk.exists(newNode, true));
        System.out.println("GetData before setting");
        for (byte dataPoint : data) {
            System.out.print((char) dataPoint);
        }

        System.out.println("GetData after setting");
        connector.updateNode(newNode, "Modified data".getBytes());
        data = zk.getData(newNode, true, zk.exists(newNode, true));
        for (byte dataPoint : data) {
            System.out.print((char) dataPoint);
        }
        connector.deleteNode(newNode);
    }

}
