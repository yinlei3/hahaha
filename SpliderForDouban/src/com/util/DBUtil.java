package com.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
	public static ResultSet queryBook(String sql) {
		Connection conn = null;
		PreparedStatement pst = null;
		try {
			Class.forName(Global.DRIVER);// 指定连接类型
			conn = DriverManager.getConnection(Global.DB_URL, Global.USER,
					Global.PASSWORD);// 获取连接
			pst = conn.prepareStatement(sql);// 准备执行语句
			return pst.executeQuery(sql);
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
}
