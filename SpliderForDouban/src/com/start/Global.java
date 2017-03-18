package com.start;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.util.ThreadPoolCommon;

/**
 * 爬虫抓取程序 常量类
 * @author yinlei
 *
 */
public class Global {
	public static ThreadPoolCommon G_ProcessThreadPool = null;
	/** 抓取编程的书籍url，所以这边写死，后期可配 */
	public static final String URL = "https://book.douban.com/tag/编程";
	/** 分页用 */
	public static final String START = "?start=";
	/** 表示按评分从大到小排列  */
	public static final String TYPE = "&type=S";
	/** COOKIES集合*/
	public static Map<String, String> COOKIES = new HashMap<String, String>();
	/** 抓取的url集合 */
	public static List<String> URL_LIST = new ArrayList<String>();

	public static String USER = "";
	public static String PASSWORD = "";
	public static String DB_URL = "";
	public static String DRIVER = "";
	/** 线程完成计数器 */
	public static int COUNT;
}
