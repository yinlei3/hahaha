package com.bio;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.dao.SpliderForDoubanDao;
import com.start.Global;
import com.util.ThreadPoolCommon;


/**
 * 爬虫程序 业务层
 * @author yinlei
 *
 */
public class SpliderForDoubanBio {
	/**
	 * 初始化参数
	 */
	public void init() {
		Global.FILE = "豆瓣编程.xls";
		// 初始化mysql相关参数
		Global.USER = "admin";
		Global.PASSWORD = "admin";
		Global.DB_URL = "jdbc:mysql://localhost/admin";
		Global.DRIVER = "com.mysql.jdbc.Driver";
		// 初始化线程池 ,默认线程池大小为10，后期可配
		Global.G_ProcessThreadPool = ThreadPoolCommon.getFixedThreadPool();
		// 初始化cookies  这边参照第三方
		//book.douban.com
		Global.COOKIES.put("__utma", "81379588.1625906329.1478780180.1478780180.1478780180.1");
		Global.COOKIES.put("__utmb", "81379588.1.10.1478780180");
		Global.COOKIES.put("__utmc", "81379588");
		Global.COOKIES.put("__utmz", "81379588.1478780180.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none)");
		Global.COOKIES.put("_pk_id.100001.3ac3", "b8e7b1931da4acd1.1478780181.1.1478780181.1478780181.");
		Global.COOKIES.put("_pk_ses.100001.3ac3", "*");
		//douban.com
		Global.COOKIES.put("bid", "MvEsSVNL_Nc");
		//read.douban.com
		Global.COOKIES.put("_ga", "GA1.3.117318709.1478747468");
		Global.COOKIES.put("_pk_id.100001.a7dd", "ce6e6ea717cbd043.1478769904.1.1478769904.1478769904.");
		Global.COOKIES.put("_pk_ref.100001.a7dd", "%5B%22%22%2C%22%22%2C1478769904%2C%22https%3A%2F%2Fbook.douban.com%2"
				+ "Fsubject_search%3Fsearch_text%3D%25E6%258E%25A8%25E8%258D%2590%25E7%25B3%25BB%25E7%25BB%259F%25"
				+ "E5%25AE%259E%25E8%25B7%25B5%26cat%3D1001%22%5D");
		//www.douban.com
		Global.COOKIES.put("_pk_id.100001.8cb4", "237bb6b49215ebbc.1478749116.2.1478774039.1478749120.");
		Global.COOKIES.put("_pk_ref.100001.8cb4", "%5B%22%22%2C%22%22%2C1478773525%2C%22https%3A%2F%2Fwww.baidu."
				+ "com%2Flink%3Furl%3DlQ4OMngm1b6fAWeomMO7xq6PNbBlxyhdnHqz9mIYN9-ycRbjZvFb1NQyQ7hqzvI46-WThP"
				+ "6A_Qo7oTQNP-98pa%26wd%3D%26eqid%3Da24e155f0000e9610000000258244a0c%22%5D");
	}

	/**
	 * 运行抓取和解析
	 */
	public void process(){
		try {
			Global.URL_LIST = getBookUrl();
			// 这边采用多线程 进行页面的信息读取
			for(String url : Global.URL_LIST){
				Global.G_ProcessThreadPool.execute(new SpliderForDoubanThread(url));
			}

			for(;;){
				// 所有url遍历完成（即所有线程完成）
				if(Global.URL_LIST.size() == Global.COUNT){
					SpliderForDoubanDao  dao = new  SpliderForDoubanDao();
					//查询表，根据评分排序，取前40条，通过poi生成Excel文件
					List<HashMap<String, String>> list = dao.queryBook();
					if(null == list || list.isEmpty() ){
						System.out.println("未查询到数据，退出");
						break;
					}
					createExcelFile(Global.FILE, list);
					break;
				} else {
					// 防止循环导致程序僵死
					Thread.sleep(3000);
				}
			}
		} catch (Exception e) {
			System.out.println("抓取数据异常，退出");
		}
	}

	
	/**
	 * 获取抓取书的url
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<String> getBookUrl() throws Exception {
		// 计数用
		int count = 0;
		// 抓取url的集合
		List<String> urlList = new ArrayList();
		for(;;){
			
			// https://book.douban.com/tag/编程?start=0&type=S
			// 设置模拟浏览器参数并抓取当前url对应jsp
			Document doc = Jsoup.connect("https://book.douban.com/tag/编程" + Global.START + count + Global.TYPE)
					.header("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)").cookies(Global.COOKIES)
					.timeout(3000).get();
			Elements newsHeadlines = doc.select("ul").select("h2").select("a");

			for (Element e : newsHeadlines) {
				System.out.println(e.attr("href"));
				urlList.add(e.attr("href"));
			}
			// 基于豆瓣分页，下一页的start参数count+当前页面的书籍数
			count += newsHeadlines.size();
			System.out.println("共抓取url个数：" + count);
			//每抓取一次 休眠1s
			Thread.sleep(1000);
			// 页面显示 没有找到符合条件的图书，终止抓取
			if (newsHeadlines.size() == 0) {
				System.out.println("end");
				break;
			}	
		}
		return urlList;
	}

	/**
	 * 根据poi生成Excel
	 * @param excelfile
	 * @param list
	 * @throws Exception
	 */
	private void createExcelFile(String excelfile,
			List<HashMap<String, String>> list) throws Exception {
		// 标题栏
		String[] title = { "序号", "书名", "评分", "评价人数", "作者", "出版社", "出版日期", "价格" };
		FileOutputStream fos = new FileOutputStream(excelfile);;
		// 创建工作薄
		HSSFWorkbook wb = new HSSFWorkbook();
		// 创建工作表
		HSSFSheet sheet = wb.createSheet();
		// 设置工作表名
		wb.setSheetName(0, "sheet0");

		HSSFRow row = null;

		// 第一行为标题栏
		row = sheet.createRow(0);// 新增一行
		for (int x = 0; x < title.length; x++) {
			row.createCell(x).setCellValue(title[x]);
		}
		row = null;

		for (int i = 0; i < list.size(); i++) {
			Map<String, String> map = list.get(i);
			row = sheet.createRow(i + 1);// 新增一行

			row.createCell(0).setCellValue(map.get(i + 1));
			row.createCell(1).setCellValue(map.get("title"));
			row.createCell(2).setCellValue(map.get("score"));
			row.createCell(3).setCellValue(map.get("rating_sum"));
			row.createCell(4).setCellValue(map.get("author"));
			row.createCell(5).setCellValue(map.get("press"));
			row.createCell(6).setCellValue(map.get("price"));

			row = null;
			map = null;
		}

		wb.write(fos);
		fos.close();

	}
}
