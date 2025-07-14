package com.hdr.customsenderplug;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FilePrinter {

    public void printInfra(WebhookDto dto) throws IOException {
        Config config = Config.getConfig();

        String outFormat = formatOutput(dto, config);
        String dir = config.getString("webhook.file.infra.path", "out/outputFile_infra");
        String extension = config.getString("webhook.file.extension", ".log");
        String fullPath = dir + extension;

        try {
            writeToFile(outFormat, fullPath, "UTF-8");
            writeToRollingFile(outFormat, dir, extension, config);
            log.info("File written (INFRA) - Path: {}, Content: {}", fullPath, outFormat);
        } catch (IOException e) {
            log.error("Failed to write file (INFRA) - Path: {}, Content: {}, Error: {}", 
                     fullPath, outFormat, e.getMessage(), e);
            throw e; // 예외를 다시 던져서 상위에서도 처리할 수 있도록 함
        }
    }
    public void printApm(WebhookDto dto) throws IOException {
        Config config = Config.getConfig();

        String outFormat = formatOutput(dto, config);
        String dir = config.getString("webhook.file.apm.path", "out/outputFile_apm");
        String extension = config.getString("webhook.file.extension", ".log");
        String fullPath = dir + extension;

        try {
            writeToFile(outFormat, fullPath, "UTF-8");
            writeToRollingFile(outFormat, dir, extension, config);
            log.info("File written (APM) - Path: {}, Content: {}", fullPath, outFormat);
        } catch (IOException e) {
            log.error("Failed to write file (APM) - Path: {}, Content: {}, Error: {}", 
                     fullPath, outFormat, e.getMessage(), e);
            throw e;
        }
    }
    public void printApmPod(WebhookDto dto) throws IOException {
        Config config = Config.getConfig();

        String outFormat = formatOutput(dto, config);
        String dir = config.getString("webhook.file.apmpod.path", "out/outputFile_apm");
        String extension = config.getString("webhook.file.extension", ".log");
        String fullPath = dir + extension;

        try {
            writeToFile(outFormat, fullPath, "UTF-8");
            writeToRollingFile(outFormat, dir, extension, config);
            log.info("File written (APM Pod) - Path: {}, Content: {}", fullPath, outFormat);
        } catch (IOException e) {
            log.error("Failed to write file (APM Pod) - Path: {}, Content: {}, Error: {}", 
                     fullPath, outFormat, e.getMessage(), e);
            throw e;
        }
    }
    public void printDb(WebhookDto dto) throws IOException {
        Config config = Config.getConfig();

        String outFormat = formatOutput(dto, config);
        String dir = config.getString("webhook.file.db.path", "out/outputFile_db");
        String extension = config.getString("webhook.file.extension", ".log");
        String fullPath = dir + extension;

        try {
            writeToFile(outFormat, fullPath, "UTF-8");
            writeToRollingFile(outFormat, dir, extension, config);
            log.info("File written (DB) - Path: {}, Content: {}", fullPath, outFormat);
        } catch (IOException e) {
            log.error("Failed to write file (DB) - Path: {}, Content: {}, Error: {}", 
                     fullPath, outFormat, e.getMessage(), e);
            throw e;
        }
    }
    public void printK8s(WebhookDto dto) throws IOException {
        Config config = Config.getConfig();

        String outFormat = formatOutput(dto, config);
        String dir = config.getString("webhook.file.k8s.path", "out/outputFile_k8s");
        String extension = config.getString("webhook.file.extension", ".log");
        String fullPath = dir + extension;

        try {
            writeToFile(outFormat, fullPath, "UTF-8");
            writeToRollingFile(outFormat, dir, extension, config);
            log.info("File written (K8S) - Path: {}, Content: {}", fullPath, outFormat);
        } catch (IOException e) {
            log.error("Failed to write file (K8S) - Path: {}, Content: {}, Error: {}", 
                     fullPath, outFormat, e.getMessage(), e);
            throw e;
        }
    }

    private String formatOutput(WebhookDto dto, Config config) {
        String outFormat = config.getString("webhook.message.format", "#level | #oname | #group | #message | #time ");
        outFormat = StringUtils.replace(outFormat, "#level", dto.getLevel());
        outFormat = StringUtils.replace(outFormat, "#oname", dto.getOname());
        outFormat = StringUtils.replace(outFormat, "#group", dto.getMsgGroup());
        outFormat = StringUtils.replace(outFormat, "#message", dto.getMessage());
        outFormat = StringUtils.replace(outFormat, "#time", StringUtils.formatDate(dto.getTime(),
                config.getString("webhook.message.date.format", "yyyyMMddHHmmss")));
        return outFormat;
    }

    private void writeToFile(String content, String fullPath, String encoding) throws IOException {
        try (PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(fullPath), true), encoding))) {
            printWriter.println(content);
        }
    }

    private void writeToRollingFile(String content, String dir, String extension, Config config) throws IOException {
        long currentTimestamp = System.currentTimeMillis();
        String suffix = StringUtils.formatDate(currentTimestamp, config.getString("webhook.file.rolling.suffix", "yyyyMMdd"));
        String rollingPath = dir + "." + suffix + extension;

        try (PrintWriter printWriter = new PrintWriter(new FileOutputStream(new File(rollingPath), true))) {
            printWriter.println(content);
        }
    }
}



