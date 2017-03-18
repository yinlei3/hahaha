package com.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolCommon extends ThreadPoolExecutor {
	/** 线程池大小 */
	private static int threadPoolSizeDefault = 10;
	private static ThreadPoolCommon threadPoolCommon;

	public ThreadPoolCommon(int poolSize) {
		super(poolSize, poolSize, 0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>());
	}

	public static ThreadPoolCommon getFixedThreadPool() {
		return getFixedThreadPool(threadPoolSizeDefault);
	}

	public static ThreadPoolCommon getFixedThreadPool(int threadPoolSize) {
		threadPoolCommon = new ThreadPoolCommon(threadPoolSize);
		return threadPoolCommon;
	}
}
