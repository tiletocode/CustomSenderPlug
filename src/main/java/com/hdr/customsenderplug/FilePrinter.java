package com.hdr.customsenderplug;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class FilePrinter {
	public void print(WebhookDto dto) throws IOException {

		Config config = Config.getConfig();

		String outFormat = config.getString("webhook.message.format","#level | #oname | #message | #time");

		outFormat = StringUtils.replace(outFormat, "#level", dto.getLevel());
		outFormat = StringUtils.replace(outFormat, "#oname", dto.getOname());
		outFormat = StringUtils.replace(outFormat, "#message", dto.getMessage());
		outFormat = StringUtils.replace(outFormat, "#time", StringUtils.formatDate(dto.getTime(),
		Config.getConfig().getString("webhook.message.date.format", "yyyyMMddHHmmss")));

		String filePath = config.getString("webhook.file.path" + "webhook.file.extension", "out/outputFile.log");
		PrintWriter printWriter = new PrintWriter(new FileOutputStream(new File(filePath), true));
		printWriter.println(outFormat);
		printWriter.close();
		
		printToday(outFormat);
	}
	
	private void printToday(String str) throws IOException {
		Config config = Config.getConfig();

		long currentTimestamp = System.currentTimeMillis();
		String suffix = StringUtils.formatDate(currentTimestamp, config.getString("webhook.file.rolling.suffix", "yyyyMMdd"));
		String filePath = config.getString("webhook.file.path" + "." + suffix + "webhook.file.extension",
										   "out/outputFile" + "." + suffix + ".log");
		
		PrintWriter printWriter = new PrintWriter(new FileOutputStream(new File(filePath), true));
		printWriter.println(str);
		printWriter.close();
	}
}
