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

    // 例如dbName = "student";outPutfileName="\\output.xls"
    public static void exportDbToExl(String dbName, String outPutfileName) {
        Connection conn = getConnection();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        String sql = "select * from " + dbName;

        try {

            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            ResultSetMetaData rm = rs.getMetaData();

            List<Map> tableContent = DbUtils.rsToList(rs);

            JxlUtils.jxlListToExl(tableContent, outPutfileName);

            pstmt.close();
            rs.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void importExlToDb(String tableName, String inPutFileName) {
        List<Map> list = JxlUtils.jxlExlToList(inPutFileName);

        Connection conn = getConnection();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        String sql = "insert into " + tableName + " ";
        // INSERT INTO table_name (列1, 列2,...) VALUES (值1, 值2,....)

        List<String> columnslist = new ArrayList<String>();
        List<String> valueslist = new ArrayList<String>();
        // 提取表头
        if (list.size() >= 1) {
            Map map = list.get(0);
            Set keySet = map.keySet();
            for (Object keyName : keySet) {
                columnslist.add(keyName.toString());
                valueslist.add("?");
            }
        } else {
            return;
        }
        String columnsStr = columnslist.toString().substring(1,
                columnslist.toString().indexOf("]"));
        String valuesStr = valueslist.toString().substring(1,
                valueslist.toString().indexOf("]"));
        //因为excel文件中的表头是中文且与db字段不对应，故直接指定
        sql = sql + "(transf, bay, occur_time, content, signal_type, event_type, prov_bay, bay_id, prov_device, device_id, prov_template, template_id, action_attr, action_attr_id, signal_fid) values (" + valuesStr + ")";

        try {
            pstmt = conn.prepareStatement(sql);
            // 写入数据库
            for (int i = 0; i < list.size(); i++) {
                Map map = list.get(i);

                for (int j = 0; j < columnslist.size(); j++) {
                    pstmt.setString(j + 1, map.get(columnslist.get(j))
                            .toString());
                }

                pstmt.addBatch();

                if (i % 200 == 0) {
                    pstmt.executeBatch();
                    conn.commit();
                }
            }

            pstmt.executeBatch();
            conn.commit();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //导入数据库完成

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
