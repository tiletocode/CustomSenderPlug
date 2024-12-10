package com.hdr.customsenderplug;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Scheduler {

	@Scheduled(cron = "0 0 0 * * ?")
	public void cronJob() {
		Config config = Config.getConfig();
		try {
			log.info("[File Rolling CronJob] is running");

			String dirInfra = config.getString("webhook.file.infra.path", "out/outputFile_infra");
			String dirApm = config.getString("webhook.file.apm.path", "out/outputFile_apm");
			String dirDb = config.getString("webhook.file.db.path", "out/outputFile_db");
			String dirK8s = config.getString("webhook.file.infra.path", "out/outputFile_k8s");
			String extension = config.getString("webhook.file.extension", ".log");

			processFileRolling(dirInfra, extension, config);
			processFileRolling(dirApm, extension, config);
			processFileRolling(dirDb, extension, config);
			processFileRolling(dirK8s, extension, config);

			log.info("[File Rolling CronJob] finished");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	private void processFileRolling(String dir, String extension, Config config) throws IOException {
		String originPath = dir + extension;
		File printFile = new File(originPath);
		if (printFile.exists()) {
			Files.write(Paths.get(originPath), new byte[0], StandardOpenOption.TRUNCATE_EXISTING);
		}

		long currentTimestamp = System.currentTimeMillis();
		long oneDay = 1000L * 60 * 60 * 24;
		int targetSize = config.getInt("webhook.file.rolling.size", 30);
		if (targetSize < 2) {
			log.warn("webhook.file.rolling.size set to default value : 2");
			targetSize = 2;
		}

		long targetTimestamp = currentTimestamp - (oneDay * targetSize);

		String dot = ".";
		String dateFormat = config.getString("webhook.file.rolling.suffix", "yyyyMMdd");
		String targetSuffix = StringUtils.formatDate(targetTimestamp, dateFormat);
		String targetFilePath = dir + dot + targetSuffix + extension;
		File targetFile = new File(targetFilePath);
		if (targetFile.exists()) {
			targetFile.delete();
			log.info("Deleted file: {}", targetFilePath);
		}
	}
}
