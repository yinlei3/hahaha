
package com.util;

import java.util.List;

/**
 * @author fangchao (Ailk No.)
 * @version 1.0
 * @since 2014-4-2
 * @category com.linkage.itms.commom
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class StringUtil
{

	public static boolean isEmpty(String input)
	{
		return input == null || input.trim().length() == 0;
	}

	public static boolean notEmpty(String input)
	{
		return !isEmpty(input);
	}

	public static String nvl(String input)
	{
		return nvl(input, "");
	}

	public static String nvl(String input, String defaultValue)
	{
		return input == null ? defaultValue : input;
	}
	
	/**
	 * 将list中内容用“,”分隔符组合一个字符串是java String类split反向操作
	 *
	 * @param list
	 * @return String
	 * @author yanhj
	 * @date 2006-2-6
	 */
	public static String weave(List list) {
		StringBuffer sb = new StringBuffer(100);
		if (list.size() != 0) {
			sb.append("'").append(list.get(0)).append("'");

			for (int i = 1; i < list.size(); i++) {
				sb.append(",'").append(list.get(i)).append("'");
			}
		}

		return sb.toString();
	}
}
