package com.hdr.customsenderplug;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebhookDto {
    private String metricName;
    private int pcode;
    private String level;
    private String metricValue;
    private long oid;
    private String title;
    private String message;
    private String uuid;
    private String metricThreshold;
    private String oname;
    private String projectName;
    private String status;
    private long time;
    private String msgGroup;
}
