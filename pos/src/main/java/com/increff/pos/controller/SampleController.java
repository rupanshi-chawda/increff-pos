package com.increff.pos.controller;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import com.increff.pos.util.IOUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import static org.hibernate.internal.util.io.StreamCopier.BUFFER_SIZE;

@Controller
public class SampleController {

	//Spring ignores . (dot) in the path. So we need fileName:.+
	//See https://stackoverflow.com/questions/16332092/spring-mvc-pathvariable-with-dot-is-getting-truncated
	@GetMapping(value = "/sample/{fileName:.+}")
	public StreamingResponseBody getFile(@PathVariable("fileName") String fileName, HttpServletResponse response) throws IOException {

		response.setContentType("text/csv");
		response.addHeader("Content-disposition:", "attachment; filename=" + fileName);
		String fileClasspath = "C:\\Users\\KIIT\\Downloads\\increff-pos\\pos\\src\\main\\resources\\com.increff\\pos\\" + fileName;
		System.out.println(fileClasspath);


//		InputStream is = new FileInputStream(fileClasspath);
//		File f = new File(fileClasspath);

		// copy it to response's OutputStream
//		try {
//			IOUtils.copy(Objects.requireNonNull(is), response.getOutputStream());
//			response.flushBuffer();
//		} finally {
//			IOUtil.closeQuietly(is);
////		}
//
//		try {
//			OutputStream out =  new FileOutputStream(fileClasspath);
//			if(out != null){
//				out.write(is.read());
//			}
//		} catch (IOException e) {
//			throw e;
//		}
		return outputStream -> {
			int bytesRead;
			byte[] buffer = new byte[BUFFER_SIZE];
			InputStream inputStream = new FileInputStream(fileClasspath);
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
		};
	}

}
