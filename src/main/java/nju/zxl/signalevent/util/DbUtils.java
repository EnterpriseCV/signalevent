package nju.zxl.signalevent.util;

import java.sql.*;
import java.util.*;


public class DbUtils {

    static Connection conn = null;
    public static Connection getConnection() {

        try {
            Class.forName(PropertyUtils.dbDriver);
            conn = DriverManager.getConnection(PropertyUtils.dbUrl, PropertyUtils.dbUserName, PropertyUtils.dbPassword);
            //conn.setAutoCommit(false);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }

    public static void releaseDB(ResultSet resultSet, Statement statement,
                                 Connection connection) {

        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<Map> rsToList(ResultSet rs) throws SQLException {
        if (rs == null)
            return Collections.EMPTY_LIST;
        ResultSetMetaData md = rs.getMetaData(); // 得到结果集(rs)的结构信息，比如字段数、字段名等
        int columnCount = md.getColumnCount(); // 返回此 ResultSet 对象中的列数
        List<Map> list = new ArrayList();
        Map rowData = new LinkedHashMap();
        while (rs.next()) {
            rowData = new LinkedHashMap(columnCount);
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(md.getColumnName(i), rs.getObject(i));
            }
            list.add(rowData);
        }
        return list;
    }
}
