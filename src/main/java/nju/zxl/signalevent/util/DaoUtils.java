package nju.zxl.signalevent.util;


import nju.zxl.signalevent.eo.HistoryData;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DaoUtils {


    // INSERT, UPDATE, DELETE 操作都可以包含在其中
    public void update(String sql, Object... args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DbUtils.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbUtils.releaseDB(null, preparedStatement, connection);
        }
    }

    // 查询一条记录, 返回对应的对象
    public <T> T get(Class<T> clazz, String sql, Object... args) {
        List<T> result = getForList(clazz, sql, args);
        if (result.size() > 0) {
            return result.get(0);
        }

        return null;
    }

    /**
     * 传入 SQL 语句和 Class 对象, 返回 SQL 语句查询到的记录对应的 Class 类的对象的集合
     *
     * @param clazz: 对象的类型
     * @param sql:   SQL 语句
     * @param args:  填充 SQL 语句的占位符的可变参数.
     * @return
     */
    public <T> List<T> getForList(Class<T> clazz,
                                  String sql, Object... args) {

        List<T> list = new ArrayList<>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            //1. 得到结果集
            connection = DbUtils.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }

            resultSet = preparedStatement.executeQuery();

            //2. 处理结果集, 得到 Map 的 List, 其中一个 Map 对象
            //就是一条记录. Map 的 key 为 reusltSet 中列的别名, Map 的 value
            //为列的值.
            List<Map<String, Object>> values =
                    handleResultSetToMapList(resultSet);

            //3. 把 Map 的 List 转为 clazz 对应的 List
            //其中 Map 的 key 即为 clazz 对应的对象的 propertyName,
            //而 Map 的 value 即为 clazz 对应的对象的 propertyValue
            list = transfterMapListToBeanList(clazz, values);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbUtils.releaseDB(resultSet, preparedStatement, connection);
        }

        return list;
    }

    /**
     * 转换List<Map> 为  List<T>
     *
     * @param clazz
     * @param values
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public <T> List<T> transfterMapListToBeanList(Class<T> clazz,
                                                  List<Map<String, Object>> values) throws Exception {

        List<T> result = new ArrayList<>();

        T bean = null;

        if (values.size() > 0) {
            for (Map<String, Object> m : values) {
                //通过反射创建一个其他类的对象
                bean = clazz.newInstance();

                for (Map.Entry<String, Object> entry : m.entrySet()) {
                    String propertyName = entry.getKey();
                    Object value = entry.getValue();

                    Field f = bean.getClass().getDeclaredField(propertyName);
                    f.setAccessible(true);
                    f.set(bean, value);

                    //BeanUtils.setProperty(bean, propertyName, value);
                }
                // 13. 把 Object 对象放入到 list 中.
                result.add(bean);
            }
        }

        return result;
    }

    /**
     * 处理结果集, 得到 Map 的一个 List, 其中一个 Map 对象对应一条记录
     *
     * @param resultSet
     * @return
     * @throws SQLException
     */
    public List<Map<String, Object>> handleResultSetToMapList(
            ResultSet resultSet) throws SQLException {
        // 5. 准备一个 List<Map<String, Object>>:
        // 键: 存放列的别名, 值: 存放列的值. 其中一个 Map 对象对应着一条记录
        List<Map<String, Object>> values = new ArrayList<>();

        List<String> columnLabels = getColumnLabels(resultSet);
        Map<String, Object> map = null;

        // 7. 处理 ResultSet, 使用 while 循环
        while (resultSet.next()) {
            map = new HashMap<>();

            for (String columnLabel : columnLabels) {
                Object value = resultSet.getObject(columnLabel);
                map.put(columnLabel, value);
            }

            // 11. 把一条记录的一个 Map 对象放入 5 准备的 List 中
            values.add(map);
        }
        return values;
    }

    /**
     * 获取结果集的 ColumnLabel 对应的 List
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    private List<String> getColumnLabels(ResultSet rs) throws SQLException {
        List<String> labels = new ArrayList<>();

        ResultSetMetaData rsmd = rs.getMetaData();
        for (int i = 0; i < rsmd.getColumnCount(); i++) {
            labels.add(rsmd.getColumnLabel(i + 1));
        }

        return labels;
    }

    // 返回某条记录的某一个字段的值 或 一个统计的值(一共有多少条记录等.)
    public <E> E getForValue(String sql, Object... args) {

        //1. 得到结果集: 该结果集应该只有一行, 且只有一列
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            //1. 得到结果集
            connection = DbUtils.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return (E) resultSet.getObject(1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DbUtils.releaseDB(resultSet, preparedStatement, connection);
        }
        //2. 取得结果

        return null;
    }
    public <E> List<E> getListForValue(String sql, Object... args) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<E> list = new ArrayList<E>();
        try {
            connection = DbUtils.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                list.add((E)resultSet.getObject(1));
            }
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DbUtils.releaseDB(resultSet, preparedStatement, connection);
        }

        return null;
    }

    public boolean insertList(List list){
        if(list.size()<1){
            return false;
        }
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        StringBuilder sql = new StringBuilder();

        try {
            connection = DbUtils.getConnection();
            connection.setAutoCommit(false);
            Class clazz = list.get(0).getClass();
            String tablename = clazz.getSimpleName();
            switch (tablename.toLowerCase()){
                case "andrule":tablename="and_rule";break;
                case "orrule":tablename="or_rule";break;
            }
            sql.append("insert into `"+tablename+"`");

            Field[] fls = clazz.getDeclaredFields();
            sql.append("(");
            for (int i = 0; i < fls.length; i++) {
                if(i>0){
                    sql.append(",");
                }
                sql.append(fls[i].getName());
            }
            sql.append(") values(");
            for (int i = 0; i < fls.length; i++) {
                if(i>0){
                    sql.append(",");
                }
                sql.append("?");
            }
            sql.append(")");
            preparedStatement = connection.prepareStatement(sql.toString());

            int stcount = 0;
            for(Object o:list){
                for(int i=0;i<fls.length;i++){
                    fls[i].setAccessible(true);
                    preparedStatement.setObject(i+1,fls[i].get(o));
                }
                preparedStatement.addBatch();
                stcount++;
                if(stcount>500){
                    stcount=0;
                    preparedStatement.executeBatch();
                }
            }
            preparedStatement.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            DbUtils.releaseDB(null, preparedStatement, connection);
        }
    }

    public boolean insertHistoryDataList(List<HistoryData> list){
        if(list.size()<1){
            return false;
        }
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        StringBuilder sql = new StringBuilder();

        try {
            connection = DbUtils.getConnection();
            connection.setAutoCommit(false);
            Class clazz = list.get(0).getClass();
            String tablename = "history_data";
            sql.append("insert into `"+tablename+"`");

            Field[] fl = clazz.getDeclaredFields();
            List<Field> fls = new ArrayList<Field>();

            for(int i=0;i<fl.length;i++){
                if(fl[i].getName().toLowerCase().equals("id")){
                    continue;
                }
                if(fl[i].getName().toLowerCase().equals("sourceid")){
                    continue;
                }
                if(fl[i].getName().toLowerCase().equals("trigger_tag")){
                    continue;
                }
                fls.add(fl[i]);
            }
            sql.append("(");
            for (int i = 0; i < fls.size(); i++) {
                if(i>0){
                    sql.append(",");
                }
                sql.append(fls.get(i).getName());
            }
            sql.append(") values(");
            for (int i = 0; i < fls.size(); i++) {
                if(i>0){
                    sql.append(",");
                }
                sql.append("?");
            }
            sql.append(")");
            preparedStatement = connection.prepareStatement(sql.toString());

            int stcount = 0;
            for(Object o:list){
                for(int i=0;i<fls.size();i++){
                    fls.get(i).setAccessible(true);
                    preparedStatement.setObject(i+1,fls.get(i).get(o));
                }
                preparedStatement.addBatch();
                stcount++;
                if(stcount>500){
                    stcount=0;
                    preparedStatement.executeBatch();
                }
            }
            preparedStatement.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            DbUtils.releaseDB(null, preparedStatement, connection);
        }
    }

}

