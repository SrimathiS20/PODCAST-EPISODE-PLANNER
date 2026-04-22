package com.podcast.dto;

import java.util.List;

public class ScriptSegment {
    private String name;
    private List<String> host_lines;
    private String transition;

    public ScriptSegment() {}

    public ScriptSegment(String name, List<String> host_lines, String transition) {
        this.name = name;
        this.host_lines = host_lines;
        this.transition = transition;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<String> getHost_lines() { return host_lines; }
    public void setHost_lines(List<String> host_lines) { this.host_lines = host_lines; }

    public String getTransition() { return transition; }
    public void setTransition(String transition) { this.transition = transition; }
}
