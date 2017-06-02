package org.xyc.showsome.pecan.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

/**
 * 场景描述
 在分布式应用中, 我们经常同时启动多个server, 调用方(client)选择其中之一发起请求.
 分布式应用必须考虑高可用性和可扩展性: server的应用进程可能会崩溃, 或者server本身也可能会宕机. 当server不够时, 也有可能增加server的数量. 总而言之, server列表并非一成不变, 而是一直处于动态的增减中.
 那么client如何才能实时的更新server列表呢? 解决的方案很多, 本文将讲述利用ZooKeeper的解决方案.

 思路
 启动server时, 在zookeeper的某个znode(假设为/sgroup)下创建一个子节点. 所创建的子节点的类型应该为ephemeral, 这样一来, 如果server进程崩溃, 或者server宕机, 与zookeeper连接的session就结束了, 那么其所创建的子节点会被zookeeper自动删除. 当崩溃的server恢复后, 或者新增server时, 同样需要在/sgroup节点下创建新的子节点.
 对于client, 只需注册/sgroup子节点的监听, 当/sgroup下的子节点增加或减少时, zookeeper会通知client, 此时client更新server列表.

 AppServer的逻辑非常简单, 只须在启动时, 在zookeeper的"/sgroup"节点下新增一个子节点即可.
 */
public class AppServer {
    private String groupNode = "sgroup";
    private String subNode = "sub";

    /**
     * 连接zookeeper
     * @param address server的地址
     */
    public void connectZookeeper(String address) throws Exception {
        ZooKeeper zk = new ZooKeeper("192.168.200.9:2181", 5000, new Watcher() {
            public void process(WatchedEvent event) {
                // 不做处理
            }
        });
        // 在"/sgroup"下创建子节点
        // 子节点的类型设置为EPHEMERAL_SEQUENTIAL, 表明这是一个临时节点, 且在子节点的名称后面加上一串数字后缀
        // 将server的地址数据关联到新创建的子节点上
        String createdPath = zk.create("/" + groupNode + "/" + subNode, address.getBytes("utf-8"),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("create: " + createdPath);
    }

    /**
     * server的工作逻辑写在这个方法中
     * 此处不做任何处理, 只让server sleep
     */
    public void handle() throws InterruptedException {
        Thread.sleep(Long.MAX_VALUE);
    }

    /**
     * 怎么测试server和client？
     * 先跑client,会打印：server list updated: []，其实就是client在监测zookeeper的某路径，主线程就一直在sleep
     * 再跑server，server就是在创建一个目录，然后给目录赋值，server每创建一个目录并赋值后，client那头会一直实时的打印server加的目录的值
     * 每跑一个server，就是创建一个目录，client就会一直打印一条消息
     * 比如server执行（as.connectZookeeper("666")），打印：create: /sgroup/sub0000000016
     * client打印：server list updated: [666]
     * server再执行（as.connectZookeeper("888");），打印：create: /sgroup/sub0000000017
     * client打印：server list updated: [888, 666]
     * server再执行（as.connectZookeeper("888");），打印：create: /sgroup/sub0000000018
     * client打印：server list updated: [888, 888, 666]
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // 在参数中指定server的地址
//        if (args.length == 0) {
//            System.err.println("The first argument must be server address");
//            System.exit(1);
//        }

        AppServer as = new AppServer();
        as.connectZookeeper("888");

        as.handle();
    }
}
