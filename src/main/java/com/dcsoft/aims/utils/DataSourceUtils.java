package com.dcsoft.aims.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSourceFactory;

public class DataSourceUtils {
	
	private static Map<String, DataSource> dataSourceMap = new HashMap<String, DataSource>();
	
	public static DataSource init(String driverClassName, String username, String password, String url, String connectionProperties) throws Exception {
		// 创建连接池
		Map<String, String> properties = new HashMap<String, String>();
		properties.put("driverClassName", driverClassName);
		properties.put("username", username);
		properties.put("password", password);
		properties.put("url", url);
		properties.put("initialSize", "5");
		properties.put("minIdle", "1");
		properties.put("maxActive", "10");
		properties.put("poolPreparedStatements", "true");
		// 修改字符集
		if (connectionProperties != null && !"".equals(connectionProperties)) {
			properties.put("connectionProperties", connectionProperties);
		}
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
		// 数据库字符集
		String encoding = dbParams.get("target_db_encoding");
		// 连接方式,针对oracle存在SID,SERVER_NAME两种连接情况进行相应处理
		String connectMode = dbParams.get("target_db_connectMode");
		// 连接池map中的key ip : 端口 : 数据库名 : 用户名 : 密码
		StringBuffer keyBuffer = new StringBuffer();
		keyBuffer.append(host).append(":").append(port).append(":").append(dbName).append(":")
		    .append(username).append(":").append(password);
		String key = keyBuffer.toString();
		if (key != null && !"".equals(key)) {
			DataSource dataSource = dataSourceMap.get(key);
			if (null == dataSource) {
				String driverClassName = null;
				// 需要进行转码的配置
				StringBuffer connectionPropertiesBuffer = new StringBuffer();
				// 连接数据库的url
				StringBuffer urlBuffer = new StringBuffer();
				switch (dbType) {
					case "oracle" :
						driverClassName = "oracle.jdbc.OracleDriver";
						if (encoding != null && !"".equals(encoding)) {
							// 针对US7ASII字符集特殊处理 url增加jdbc:wrap-jdbc:filters=encoding: 连接池增加配置 connectionProperties
							urlBuffer.append("jdbc:wrap-jdbc:filters=encoding:");
							connectionPropertiesBuffer.append("serverEncoding=").append(encoding).append(";clientEncoding=GBK;defaultRowPrefetch=50;bigStringTryClob=true");
						}
						/* 
						 * url默认按照SID拼接
						 * jdbc:oracle:thin:@localhost:1521:SID 
						 * jdbc:oracle:thin:@//localhost:1521:SERVICENAME
						 * jdbc:oracle:thin:@TNSName
						 */
						if (connectMode != null && !"".equals(connectMode)) {
							if ("SERVICE_NAME".equals(connectMode)) {
								urlBuffer.append("jdbc:oracle:thin:@//").append(host).append(":").append(port).append("/").append(dbName);
							} else if ("TNS_NAME".equals(connectMode)) {
								urlBuffer.append("jdbc:oracle:thin:@").append(dbName);
							} else {
								urlBuffer.append("jdbc:oracle:thin:@").append(host).append(":").append(port).append(":").append(dbName);
							}
						} else {
							urlBuffer.append("jdbc:oracle:thin:@").append(host).append(":").append(port).append(":").append(dbName);
						}
						break;
					default : 
						break;
				}
				String connectionProperties = connectionPropertiesBuffer.toString();
				String url = urlBuffer.toString();
				dataSource = init(driverClassName, username, password, url, connectionProperties);
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
    
    /**
     * 通过驱动包获取connection连接
     * @param params
     * @return
     * @throws Exception
     */
    public static Connection getConnection(Params params) throws Exception {
		Map<String, String> dbParams = params.getDbParams();
		String host = dbParams.get("target_db_host");
		String port = dbParams.get("target_db_port");
		String username = dbParams.get("target_db_user");
		String password = dbParams.get("target_db_password");
		String dbType = dbParams.get("target_db_type");
		String dbName = dbParams.get("target_db_name");
		String connectMode = dbParams.get("target_db_connectMode");
		
		String driverClassName = null;
		StringBuffer urlBuffer = new StringBuffer();
		switch (dbType) {
			case "oracle" :
				driverClassName = "oracle.jdbc.OracleDriver";
				if (connectMode != null && !"".equals(connectMode)) {
					if ("SERVICE_NAME".equals(connectMode)) {
						urlBuffer.append("jdbc:oracle:thin:@//").append(host).append(":").append(port).append("/").append(dbName);
					} else if ("TNS_NAME".equals(connectMode)) {
						urlBuffer.append("jdbc:oracle:thin:@").append(dbName);
					} else {
						urlBuffer.append("jdbc:oracle:thin:@").append(host).append(":").append(port).append(":").append(dbName);
					}
				} else {
					urlBuffer.append("jdbc:oracle:thin:@").append(host).append(":").append(port).append(":").append(dbName);
				}
				break;
			default : 
				break;
		}
		
		String url = urlBuffer.toString();
		Class.forName(driverClassName);
		
    	return DriverManager.getConnection(url, username, password);
    }
    
}
