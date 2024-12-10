package com.hdr.customsenderplug;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class FilePrinter {

    public void printInfra(WebhookDto dto) throws IOException {
        Config config = Config.getConfig();

        String outFormat = formatOutput(dto, config);
        String dir = config.getString("webhook.file.infra.path", "out/outputFile_infra");
        String extension = config.getString("webhook.file.extension", ".log");
        String fullPath = dir + extension;

        writeToFile(outFormat, fullPath, "UTF-8");
        writeToRollingFile(outFormat, dir, extension, config);
    }
    public void printApm(WebhookDto dto) throws IOException {
        Config config = Config.getConfig();

        String outFormat = formatOutput(dto, config);
        String dir = config.getString("webhook.file.apm.path", "out/outputFile_apm");
        String extension = config.getString("webhook.file.extension", ".log");
        String fullPath = dir + extension;

        writeToFile(outFormat, fullPath, "UTF-8");
        writeToRollingFile(outFormat, dir, extension, config);
    }
    public void printDb(WebhookDto dto) throws IOException {
        Config config = Config.getConfig();

        String outFormat = formatOutput(dto, config);
        String dir = config.getString("webhook.file.db.path", "out/outputFile_db");
        String extension = config.getString("webhook.file.extension", ".log");
        String fullPath = dir + extension;

        writeToFile(outFormat, fullPath, "UTF-8");
        writeToRollingFile(outFormat, dir, extension, config);
    }
    public void printK8s(WebhookDto dto) throws IOException {
        Config config = Config.getConfig();

        String outFormat = formatOutput(dto, config);
        String dir = config.getString("webhook.file.k8s.path", "out/outputFile_k8s");
        String extension = config.getString("webhook.file.extension", ".log");
        String fullPath = dir + extension;

        writeToFile(outFormat, fullPath, "UTF-8");
        writeToRollingFile(outFormat, dir, extension, config);
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



