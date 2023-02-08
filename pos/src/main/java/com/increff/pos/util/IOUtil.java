package com.increff.pos.util;

import java.io.Closeable;
import java.io.IOException;

public class IOUtil {

	public static void closeQuietly(Closeable c) throws ApiException {
		if (c == null) {
			return;
		}
		try {
			c.close();
		} catch (IOException e) {
			throw new ApiException(e.getMessage());
		}
	}

}
