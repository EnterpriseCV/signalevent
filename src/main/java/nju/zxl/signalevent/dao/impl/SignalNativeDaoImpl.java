package nju.zxl.signalevent.dao.impl;

import nju.zxl.signalevent.dao.SignalNativeDao;
import nju.zxl.signalevent.domain.Signal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Service
public class SignalNativeDaoImpl implements SignalNativeDao{

    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String username;
    @Value("${spring.datasource.password}")
    String password;

    public SignalNativeDaoImpl(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean saveSignalList(List<Signal> slist) {
        Connection con;
        try {
            con= DriverManager.getConnection(url,username,password);
             for(Signal s:slist){
                PreparedStatement ps = con.prepareStatement("insert into `signal`(xh,jg_bh,sb_bh,info_bh,act_bh) values (?,?,?,?,?)");
                ps.setInt(1,s.getXh());
                ps.setInt(2,s.getJg_bh());
                ps.setInt(3,s.getSb_bh());
                ps.setInt(4,s.getInfo_bh());
                ps.setInt(5,s.getAct_bh());
                ps.execute();
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
