package com.hdr.customsenderplug.receiver;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdr.customsenderplug.Config;
import com.hdr.customsenderplug.FilePrinter;
import com.hdr.customsenderplug.WebhookDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReceiverInfra extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		// 응답 상태를 200(OK)으로 설정
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().write("Webhook Service(INFRA) Running.");
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
		log.info("Received webhook request (INFRA): {}", requestBody);
		
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

		String message = dto.getMessage();
		int idx = message.indexOf(config.getString("webhook.message.seperator", "@"));
		if ( idx > 0 ) {
			// seperator 뒤에 문자열 검출 시 hostname컬럼 치환
			String msgWithoutLabel = message.substring(0, idx);
			String label = message.substring(idx + 1);

			dto.setOname(label);
			dto.setMessage(msgWithoutLabel);
		}

		// Warning -> Major
		if (dto.getLevel().equals("Warning")) {
			dto.setLevel(config.getString("webhook.message.warnreplace", "Major"));
		}

		//제품 별 메시지그룹 설정
		dto.setMsgGroup(config.getString("webhook.group.infra", "WHATAP_INFRA"));
		
		try {
			FilePrinter printer = new FilePrinter();
			printer.printInfra(dto);
		} catch (IOException e) {
			log.error("File writing failed in ReceiverInfra: {}", e.getMessage(), e);
			// HTTP 응답은 정상적으로 처리 (파일 쓰기 실패가 webhook 응답에 영향주지 않도록)
		}
	}
}
