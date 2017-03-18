package com.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.start.Global;

public class DBUtil {
	/**
	 * 数据入表
	 * 
	 * @param sql
	 *            sql
	 */
	public static void insertBook(String sql) {
		Connection conn = null;
		PreparedStatement pst = null;
		try {
			Class.forName(Global.DRIVER);// 指定连接类型
			conn = DriverManager.getConnection(Global.DB_URL, Global.USER,
					Global.PASSWORD);// 获取连接
			pst = conn.prepareStatement(sql);// 准备执行语句
			pst.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 数据查询
	 * 
	 * @param sql
	 *            sql
	 * @return 查询结果
	 */
	public static List<HashMap<String, String>> queryBook(String sql) {
		Connection conn = null;
		PreparedStatement pst = null;
		try {
			Class.forName(Global.DRIVER);// 指定连接类型
			conn = DriverManager.getConnection(Global.DB_URL, Global.USER,
					Global.PASSWORD);// 获取连接
			pst = conn.prepareStatement(sql);// 准备执行语句
			return convertList(pst.executeQuery(sql));
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		} finally {
			try {
				conn.close();
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * ResultSet转list
	 * @param rs 结果集
	 * @return
	 * @throws SQLException
	 */
	private static List<HashMap<String, String>> convertList(ResultSet rs) throws SQLException{
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		// 获取键名
		ResultSetMetaData md = rs.getMetaData();
		// 获取行的数量
		int columnCount = md.getColumnCount();
		
		while (rs.next()) {
			HashMap<String, String> rowData = new HashMap<String, String>();
			for (int i = 1; i <= columnCount; i++) {
				rowData.put(md.getColumnName(i), rs.getObject(i).toString());
			}
			list.add(rowData);
		}
		return list;
	}
}
