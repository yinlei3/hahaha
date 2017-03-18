package com.dao;

import com.util.DBUtil;

public class SpliderForDoubanDao {

	/**
	 * 入book表
	 * 
	 * @param title
	 *            书名
	 * @param score
	 *            评分
	 * @param rating_sum
	 *            rating_sum
	 * @param author
	 *            作者
	 * @param press
	 *            出版社
	 * @param date
	 *            出版日期
	 * @param price
	 *            价格
	 */
	public void insertBook(String title, String score, String rating_sum,
			String author, String press, String date, String price) {
		String sql = "insert into book values ('" + title + "', '" + score
				+ "', '" + rating_sum + "', '" + author + "', '" + press
				+ "', '" + date + "', '" + price + "')";
		DBUtil.insertBook(sql);
	}
}
