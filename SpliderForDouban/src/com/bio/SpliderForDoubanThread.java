package com.bio;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.dao.SpliderForDoubanDao;
import com.start.Global;
import com.util.StringUtil;

/**
 * 读取抓取url页面的线程
 * 
 * @author yinlei
 * 
 */
public class SpliderForDoubanThread implements Runnable {

	private String url = "";
	private SpliderForDoubanDao dao  = new SpliderForDoubanDao();

	/**
	 * 构造器
	 * 
	 * @param url
	 *            链接
	 */
	public SpliderForDoubanThread(String url) {
		this.url = url;
	}

	@Override
	public void run() {
		try {
			// 模拟浏览器，获取对应url的jsp页面
			Document doc = Jsoup.connect(url).header("User-Agent","Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)")
					.cookies(Global.COOKIES).timeout(3000).get();
			Elements titleElement = doc.getElementsByClass("subject clearfix")
					.select("a");
			Elements scoreElement = doc.select("strong");
			Elements ratingSum = doc.getElementsByClass("rating_sum")
					.select("a").select("span");
			Elements authorElement = doc.getElementById("info").select("span")
					.first().select("a");
			Element pressElement = doc.getElementById("info");

			// 书名
			String title = titleElement.attr("title");
			// 评分
			String score = scoreElement.html();
			// 评价人数
			String rating_sum = ratingSum.html();
			// 作者
			String author = authorElement.html();
			// 出版社
			String press = pressElement.text();
			if (press.indexOf("出版社:") > -1) {
				press = pressElement.text().split("出版社:")[1].split(" ")[1];
			} else {
				press = "";
			}
			// 出版日期
			String date = pressElement.text();
			if (date.indexOf("出版年:") > -1) {
				date = pressElement.text().split("出版年:")[1].split(" ")[1];
			} else {
				date = "";
			}
			// 价格
			String price = pressElement.text();
			if (price.indexOf("定价:") > -1) {
				price = pressElement.text().split("定价:")[1].split(" ")[1];
				if (price.equals("CNY")) {
					price = pressElement.text().split("定价:")[1].split(" ")[2];
				}
			} else {
				price = "";
			}
			// 打印页面信息
			System.out.println(title +"-"+score+"-"+rating_sum+"-"+author+"-"+press+"-"+date+"-"+price);
			 //评价人数大于1000插入数据到数据库
			if (!StringUtil.isEmpty(rating_sum) && Integer.parseInt(rating_sum) >= 1000) {
				dao.insertBook(title, score, rating_sum, author, press, date, price);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 计数
			Global.COUNT ++;
		}

	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
