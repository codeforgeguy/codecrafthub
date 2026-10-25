package com.codecrafthub.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Course {
    private Long id;
    private String name;
    private String description;

    @JsonProperty("target_date")
    private String targetDate;

    private String status;

    @JsonProperty("created_at")
    private String createdAt;

    public Course() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTargetDate() { return targetDate; }
    public void setTargetDate(String targetDate) { this.targetDate = targetDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}