package com.dcsoft.aims.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSourceFactory;

public class DataSourceUtils {
	
	private static Map<String, DataSource> dataSourceMap = new HashMap<String, DataSource>();
	
	public static DataSource init(String username, String password, String url) throws Exception {
		// 创建连接池
		Map<String, String> properties = new HashMap<String, String>();
		properties.put("username", username);
		properties.put("password", password);
		properties.put("url", url);
		properties.put("initialSize", "5");
		properties.put("minIdle", "1");
		properties.put("maxActive", "10");
		properties.put("poolPreparedStatements", "true");
		return DruidDataSourceFactory.createDataSource(properties);
	}
	
	public static DataSource getDataSource(Params params) throws Exception {
		Map<String, String> dbParams = params.getDbParams();
		String host = dbParams.get("target_db_host");
		String port = dbParams.get("target_db_port");
		String username = dbParams.get("target_db_user");
		String password = dbParams.get("target_db_password");
		String dbType = dbParams.get("target_db_type");
		String dbName = dbParams.get("target_db_name");
		// 连接池map中的key ip : 端口 : 数据库名 : 用户名 : 密码
		StringBuffer keyBuffer = new StringBuffer();
		keyBuffer.append(host).append(":").append(port).append(":").append(dbName).append(":")
		    .append(username).append(":").append(password);
		String key = keyBuffer.toString();
		if (key != null && !"".equals(key)) {
			DataSource dataSource = dataSourceMap.get(key);
			if (null == dataSource) {
				// 连接数据库的url
				StringBuffer urlBuffer = new StringBuffer();
				switch (dbType) {
					case "oracle" :
						urlBuffer.append("jdbc:oracle:thin:@").append(host).append(":").append(port).append(":").append(dbName);
						break;
					default : 
						break;
				}
				String url = urlBuffer.toString();
				dataSource = init(username, password, url);
				dataSourceMap.put(key, dataSource);
			}
			return dataSource;
		}
        return null;
	}

    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
            }
        }
    }
    
    public static void close(Connection conn, Statement stmt) {
        close(conn, stmt, null);
    }
    
}
