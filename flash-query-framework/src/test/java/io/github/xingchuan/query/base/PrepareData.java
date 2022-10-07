package io.github.xingchuan.query.base;

import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author xingchuan.qxc
 * @since 1.0
 */
public class PrepareData {

    private Logger logger = LoggerFactory.getLogger(PrepareData.class);

    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static final String DRIVER_CLASS = "org.h2.Driver";

    private static final String JDBC_URL = "jdbc:h2:mem:sample_db;DB_CLOSE_DELAY=-1";

    public void startServer() throws SQLException {
        Server tcpServer = Server.createTcpServer("-tcpAllowOthers");
        Server start = tcpServer.start();
        int port = start.getPort();
        logger.info("h2 start at port {} ", port);

    }


    public void preparedData(List<String> sqlList) throws Exception {
        Class.forName(DRIVER_CLASS);
        Connection conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
        Statement statement = conn.createStatement();
        for (String sql : sqlList) {
            statement.executeUpdate(sql);
        }
        statement.executeLargeBatch();

        conn.commit();
        conn.close();
    }
}
