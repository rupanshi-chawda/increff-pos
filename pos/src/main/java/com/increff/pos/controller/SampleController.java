package com.increff.pos.controller;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import com.increff.pos.util.IOUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
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


	@Value("${project.path}")
	private String project_path;

	@ApiOperation(value = "Gets sample tsv files from resources")
	@GetMapping(value = "/sample/{fileName:.+}")
	public StreamingResponseBody getFile(@PathVariable("fileName") String fileName, HttpServletResponse response) {

		response.setContentType("text/csv");
		response.addHeader("Content-disposition:", "attachment; filename=" + fileName);
		String fileClasspath = project_path + "\\src\\main\\resources\\com.increff\\pos\\" + fileName;

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
