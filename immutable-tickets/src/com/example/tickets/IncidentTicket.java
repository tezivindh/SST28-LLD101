package com.example.tickets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * INTENTION: A ticket should be an immutable record-like object.
 *
 * CURRENT STATE (BROKEN ON PURPOSE):
 * - mutable fields
 * - multiple constructors
 * - public setters
 * - tags list can be modified from outside
 * - validation is scattered elsewhere
 *
 * TODO (student): refactor to immutable + Builder.
 */
public final class IncidentTicket {

    private final String id;
    private final String reporterEmail;
    private final String title;

    private final String description;
    private final String priority; // LOW, MEDIUM, HIGH, CRITICAL
    private final List<String> tags;
    private final String assigneeEmail;
    private final boolean customerVisible;
    private final Integer slaMinutes; // optional
    private final String source; // e.g. "CLI", "WEBHOOK", "EMAIL"

    private IncidentTicket(Builder b) {
        this.id = b.id;
        this.reporterEmail = b.reporterEmail;
        this.title = b.title;
        this.description = b.description;
        this.priority = b.priority;
        this.tags = Collections.unmodifiableList(new ArrayList<>(b.tags));
        this.assigneeEmail = b.assigneeEmail;
        this.customerVisible = b.customerVisible;
        this.slaMinutes = b.slaMinutes;
        this.source = b.source;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    public String getId() {
        return id;
    }

    public String getReporterEmail() {
        return reporterEmail;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPriority() {
        return priority;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getAssigneeEmail() {
        return assigneeEmail;
    }

    public boolean isCustomerVisible() {
        return customerVisible;
    }

    public Integer getSlaMinutes() {
        return slaMinutes;
    }

    public String getSource() {
        return source;
    }

    @Override
    public String toString() {
        return "IncidentTicket{" +
                "id='" + id + '\'' +
                ", reporterEmail='" + reporterEmail + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", priority='" + priority + '\'' +
                ", tags=" + tags +
                ", assigneeEmail='" + assigneeEmail + '\'' +
                ", customerVisible=" + customerVisible +
                ", slaMinutes=" + slaMinutes +
                ", source='" + source + '\'' +
                '}';
    }

    public static final class Builder {

        private String id;
        private String reporterEmail;
        private String title;
        private String description;
        private String priority;
        private final List<String> tags = new ArrayList<>();
        private String assigneeEmail;
        private boolean customerVisible;
        private Integer slaMinutes;
        private String source;

        private Builder() {}

        private Builder(IncidentTicket t) {
            this.id = t.id;
            this.reporterEmail = t.reporterEmail;
            this.title = t.title;
            this.description = t.description;
            this.priority = t.priority;
            this.tags.addAll(t.tags);
            this.assigneeEmail = t.assigneeEmail;
            this.customerVisible = t.customerVisible;
            this.slaMinutes = t.slaMinutes;
            this.source = t.source;
        }

        public Builder id(String id) { this.id = id; return this; }
        public Builder reporterEmail(String e) { this.reporterEmail = e; return this; }
        public Builder title(String title) { this.title = title; return this; }
        public Builder description(String d) { this.description = d; return this; }
        public Builder priority(String p) { this.priority = p; return this; }
        public Builder addTag(String tag) { this.tags.add(tag); return this; }
        public Builder tags(List<String> tags) { this.tags.clear(); this.tags.addAll(tags); return this; }
        public Builder assigneeEmail(String e) { this.assigneeEmail = e; return this; }
        public Builder customerVisible(boolean v) { this.customerVisible = v; return this; }
        public Builder slaMinutes(Integer s) { this.slaMinutes = s; return this; }
        public Builder source(String source) { this.source = source; return this; }

        public IncidentTicket build() {
            Validation.requireTicketId(id);
            Validation.requireEmail(reporterEmail, "reporterEmail");
            Validation.requireNonBlank(title, "title");
            Validation.requireMaxLen(title, 80, "title");
            Validation.requireOneOf(priority, "priority", "LOW", "MEDIUM", "HIGH", "CRITICAL");
            if (assigneeEmail != null && !assigneeEmail.isEmpty()) {
                Validation.requireEmail(assigneeEmail, "assigneeEmail");
            }
            Validation.requireRange(slaMinutes, 5, 7200, "slaMinutes");
            return new IncidentTicket(this);
        }
    }
}
