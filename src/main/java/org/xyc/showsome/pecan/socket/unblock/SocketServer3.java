package org.xyc.showsome.pecan.socket.unblock;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;

/**
 * created by wks on date: 2017/6/2
 *
 * 这样一来，我们利用JAVA实现了完整的“非阻塞IO”模型：让TCP连接和数据读取这两个过程，都变成了“非阻塞”方式了。
 * 然并卵，这种处理方式实际上并没有解决accept方法、read方法阻塞的根本问题。根据上文的叙述，accept方法、read方法阻塞的根本问题是底层接受数据报文时的“同步IO”工作方式。这两次改进过程，只是解决了IO操作的两步中的第一步：将程序层面的阻塞方式变成了非阻塞方式。
 */
public class SocketServer3 {

    static {
        BasicConfigurator.configure();
    }

    private static Object xWait = new Object();

    /**
     * 日志
     */
    private static final Log LOGGER = LogFactory.getLog(SocketServer3.class);

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(83);
            serverSocket.setSoTimeout(100);
            while(true) {
                Socket socket = null;
                try {
                    socket = serverSocket.accept();
                } catch(SocketTimeoutException e1) {
                    //===========================================================
                    //      执行到这里，说明本次accept没有接收到任何TCP连接
                    //      主线程在这里就可以做一些事情，记为X
                    //===========================================================
                    synchronized (SocketServer3.xWait) {
                        SocketServer3.LOGGER.info("这次没有从底层接收到任何TCP连接，等待10毫秒，模拟事件X的处理时间");
                        SocketServer3.xWait.wait(10);
                    }
                    continue;
                }

                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();
                Integer sourcePort = socket.getPort();
                int maxLen = 2048;
                byte[] contextBytes = new byte[maxLen];
                int realLen;
                StringBuffer message = new StringBuffer();
                //下面我们收取信息（设置成非阻塞方式，这样read信息的时候，又可以做一些其他事情）
                socket.setSoTimeout(10);
                BIORead:while(true) {
                    try {
                        while((realLen = in.read(contextBytes, 0, maxLen)) != -1) {
                            message.append(new String(contextBytes , 0 , realLen));
                            /*
                             * 我们假设读取到“over”关键字，
                             * 表示客户端的所有信息在经过若干次传送后，完成
                             * */
                            if(message.indexOf("over") != -1) {
                                break BIORead;
                            }
                        }
                    } catch(SocketTimeoutException e2) {
                        //===========================================================
                        //      执行到这里，说明本次read没有接收到任何数据流
                        //      主线程在这里又可以做一些事情，记为Y
                        //===========================================================
                        SocketServer3.LOGGER.info("这次没有从底层接收到任务数据报文，等待10毫秒，模拟事件Y的处理时间");
                        continue;
                    }
                }
                //下面打印信息
                SocketServer3.LOGGER.info("服务器收到来自于端口：" + sourcePort + "的信息：" + message);

                //下面开始发送信息
                out.write("回发响应信息！".getBytes());

                //关闭
                out.close();
                in.close();
                socket.close();
            }
        } catch(Exception e) {
            SocketServer3.LOGGER.error(e.getMessage(), e);
        } finally {
            if(serverSocket != null) {
                serverSocket.close();
            }
        }
    }
}
