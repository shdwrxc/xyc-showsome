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
 * 非阻塞模式
 * 这里我们针对了SocketServer增加了阻塞等待时间，实际上只实现了非阻塞IO模型中的第一步：监听连接状态的非阻塞。通过运行代码，我们可以发现read()方法还是被阻塞的，说明socket套接字等待数据读取的过程，还是阻塞方式。
 *
 */
public class SocketServer2 {

    static {
        BasicConfigurator.configure();
    }

    private static Object xWait = new Object();

    /**
     * 日志
     */
    private static final Log LOGGER = LogFactory.getLog(SocketServer2.class);

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
                    //      执行到这里，说明本次accept没有接收到任何数据报文
                    //      主线程在这里就可以做一些事情，记为X
                    //===========================================================
                    synchronized (SocketServer2.xWait) {
                        SocketServer2.LOGGER.info("这次没有从底层接收到任务数据报文，等待10毫秒，模拟事件X的处理时间");
                        SocketServer2.xWait.wait(10);
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
                //下面我们收取信息（这里还是阻塞式的,一直等待，直到有数据可以接受）
                while((realLen = in.read(contextBytes, 0, maxLen)) != -1) {
                    message.append(new String(contextBytes , 0 , realLen));
                    /*
                     * 我们假设读取到“over”关键字，
                     * 表示客户端的所有信息在经过若干次传送后，完成
                     * */
                    if(message.indexOf("over") != -1) {
                        break;
                    }
                }
                //下面打印信息
                SocketServer2.LOGGER.info("服务器收到来自于端口：" + sourcePort + "的信息：" + message);

                //下面开始发送信息
                out.write("回发响应信息！".getBytes());

                //关闭
                out.close();
                in.close();
                socket.close();
            }
        } catch(Exception e) {
            SocketServer2.LOGGER.error(e.getMessage(), e);
        } finally {
            if(serverSocket != null) {
                serverSocket.close();
            }
        }
    }
}
