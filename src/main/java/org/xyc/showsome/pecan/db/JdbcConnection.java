package org.xyc.showsome.pecan.db;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class JdbcConnection {

    private static String url = "jdbc:oracle:thin:@192.168.200.28:1521:orcl";
    private static String username = "cpic_dev";
    private static String password = "cpic_dev";
    public static Connection connection;

    public static int insert(String paramStr) {
        try {
            //初始化驱动包
            //成功加载后，会将Driver类的实例注册到DriverManager类中。
            Class.forName("oracle.jdbc.driver.OracleDriver");
            //根据数据库连接字符，名称，密码生成connection
            connection = DriverManager.getConnection(url, username, password);

            String sql = "insert into t_pub_batch_job (TASK_ID, BUSINESS_ID, TASK_STATUS, SUBMITTER_NAME, CLIENT_SERVICE_BEAN_NAME, PARAM_JSON_VALUE, PARAM_CLASS_NAME, CLIENT_SERVICE_METHOD_NAME, TASK_TYPE, BUSINESS_NO) "
                    + "values (999999, 35226, '9', ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, "192.168.200.19");
            pstmt.setString(2, "ruleEngine");
//            pstmt.setString(4, "{\"abc\":123}");
            pstmt.setString(3, paramStr);
            pstmt.setString(4, "com.cccis.common.rules.vo.VehicleAuditParamVo");
            pstmt.setString(5, "auditVehicle");
            pstmt.setString(6, "01");
            pstmt.setString(7, "derrick-test-18");

            int i = pstmt.executeUpdate();

            return i;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static Clob stringToClob(String str) {
        if (null == str)
            return null;
        else {
            try {
                Clob c = new javax.sql.rowset.serial.SerialClob(str
                        .toCharArray());
                return c;
            } catch (Exception e) {
                return null;
            }
        }
    }
}
