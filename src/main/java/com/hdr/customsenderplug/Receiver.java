package com.hdr.customsenderplug;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class Receiver extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		// 응답 상태를 200(OK)으로 설정
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().write("Webhook Service Running.");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Config config = Config.getConfig();

		StringBuffer jb = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null)
				jb.append(line);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		String requestBody = jb.toString();
		ObjectMapper om = new ObjectMapper();
		WebhookDto dto = om.readValue(requestBody, WebhookDto.class);

		// null check
		String nullReplace = config.getString("webhook.message.nullreplace", "empty_value");
		if (dto.getLevel() == null) {
			dto.setLevel(nullReplace);
		}
		if (dto.getOname() == null) {
			dto.setOname(nullReplace);
		}
		if (dto.getMessage() == null) {
			dto.setMessage(nullReplace);
		}
		// 메시지가 빈 로그는 Title -> Message
		if (dto.getMessage().equals("")) {
			dto.setMessage(dto.getTitle());
			if (dto.getTitle().startsWith("LOG")) {
				dto.setMessage(dto.getMessage().replace("LOG", "LOG:"));
			}
		}

		// seperator 뒤에 문자열 검출 시 hostname컬럼 치환
		String message = dto.getMessage();
		int idx = message.indexOf(config.getString("webhook.message.seperator", "@"));
		if ( idx > 0 ) {
			String msgWithoutLabel = message.substring(0, idx);
			String label = message.substring(idx + 1);

			dto.setOname(label);
			dto.setMessage(msgWithoutLabel);
		}
		// oname에 host_ip추가
		/**
		 * String message = dto.getMessage();
		 * String oname = dto.getOname();
		 * int idx = message.indexOf(config.getString("webhook.message.seperator",
		 * "@"));
		 * 
		 * if (idx > 0) {
		 * String messageFix = message.substring(0, idx);
		 * String hostip = message.substring(idx + 1);
		 * 
		 * dto.setOname(oname + "(" + hostip + ")");
		 * dto.setMessage(messageFix);
		 * }
		 */

		// Warning -> Major
		if (dto.getLevel().equals("Warning")) {
			dto.setLevel(config.getString("webhook.message.warnreplace", "Major"));
		}

		FilePrinter printer = new FilePrinter();
		printer.print(dto);
	}
}
