package com.start;

import com.bio.SpliderForDoubanBio;

/**
 * 爬虫程序启动类
 * 利用了Jsoup来进行抓取
 * @author yinlei
 *
 */
public class SpliderForDoubanStart {

	/**
	 * 程序入口
	 */
	public static void main(String[] args) {
		SpliderForDoubanBio bio  = new  SpliderForDoubanBio();
		// 初始化
		bio.init();
		// 执行
		bio.process();
	}

}
