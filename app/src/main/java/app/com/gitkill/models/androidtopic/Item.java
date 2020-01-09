package app.com.gitkill.models.androidtopic;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({
    "full_name",
    "html_url",
    "description",
    "created_at",
    "updated_at",
    "pushed_at",
    "clone_url",
    "svn_url",
    "stargazers_count",
    "watchers_count",
    "language",
    "has_issues",
    "has_projects",
    "has_downloads",
    "has_wiki",
    "has_pages",
    "forks_count",
    "mirror_url",
    "disabled",
    "open_issues_count",
    "license",
    "forks",
    "open_issues",
    "watchers",
    "default_branch",
    "score"
})
public class Item {

    @JsonProperty("full_name")
    private String full_name;
    @JsonProperty("html_url")
    private String html_url;
    @JsonProperty("description")
    private String description;
    @JsonProperty("created_at")
    private String created_at;
    @JsonProperty("updated_at")
    private String updated_at;
    @JsonProperty("pushed_at")
    private String pushed_at;
    @JsonProperty("clone_url")
    private String clone_url;
    @JsonProperty("svn_url")
    private String svn_url;
    @JsonProperty("stargazers_count")
    private Integer stargazers_count;
    @JsonProperty("watchers_count")
    private Integer watchers_count;
    @JsonProperty("language")
    private String language;
    @JsonProperty("has_issues")
    private Boolean has_issues;
    @JsonProperty("has_projects")
    private Boolean has_projects;
    @JsonProperty("has_downloads")
    private Boolean has_downloads;
    @JsonProperty("has_wiki")
    private Boolean has_wiki;
    @JsonProperty("has_pages")
    private Boolean has_pages;
    @JsonProperty("forks_count")
    private Integer forks_count;
    @JsonProperty("mirror_url")
    private Object mirror_url;
    @JsonProperty("disabled")
    private Boolean disabled;
    @JsonProperty("open_issues_count")
    private Integer open_issues_count;
    @JsonProperty("license")
    private License license;
    @JsonProperty("forks")
    private Integer forks;
    @JsonProperty("open_issues")
    private Integer open_issues;
    @JsonProperty("watchers")
    private Integer watchers;
    @JsonProperty("default_branch")
    private String default_branch;
    @JsonProperty("score")
    private Double score;

    @JsonProperty("full_name")
    public String getFullName() {
        return full_name;
    }

    @JsonProperty("full_name")
    public void setFullName(String full_name) {
        this.full_name = full_name;
    }

    @JsonProperty("html_url")
    public String getHtmlUrl() {
        return html_url;
    }

    @JsonProperty("html_url")
    public void setHtmlUrl(String html_url) {
        this.html_url = html_url;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("created_at")
    public String getCreatedAt() {
        return created_at;
    }

    @JsonProperty("created_at")
    public void setCreatedAt(String created_at) {
        this.created_at = created_at;
    }

    @JsonProperty("updated_at")
    public String getUpdatedAt() {
        return updated_at;
    }

    @JsonProperty("updated_at")
    public void setUpdatedAt(String updated_at) {
        this.updated_at = updated_at;
    }

    @JsonProperty("pushed_at")
    public String getPushedAt() {
        return pushed_at;
    }

    @JsonProperty("pushed_at")
    public void setPushedAt(String pushed_at) {
        this.pushed_at = pushed_at;
    }

    @JsonProperty("clone_url")
    public String getCloneUrl() {
        return clone_url;
    }

    @JsonProperty("clone_url")
    public void setCloneUrl(String clone_url) {
        this.clone_url = clone_url;
    }

    @JsonProperty("svn_url")
    public String getSvnUrl() {
        return svn_url;
    }

    @JsonProperty("svn_url")
    public void setSvnUrl(String svn_url) {
        this.svn_url = svn_url;
    }

    @JsonProperty("stargazers_count")
    public Integer getStargazersCount() {
        return stargazers_count;
    }

    @JsonProperty("stargazers_count")
    public void setStargazersCount(Integer stargazers_count) {
        this.stargazers_count = stargazers_count;
    }

    @JsonProperty("watchers_count")
    public Integer getWatchersCount() {
        return watchers_count;
    }

    @JsonProperty("watchers_count")
    public void setWatchersCount(Integer watchers_count) {
        this.watchers_count = watchers_count;
    }

    @JsonProperty("language")
    public String getLanguage() {
        return language;
    }

    @JsonProperty("language")
    public void setLanguage(String language) {
        this.language = language;
    }

    @JsonProperty("has_issues")
    public Boolean getHasIssues() {
        return has_issues;
    }

    @JsonProperty("has_issues")
    public void setHasIssues(Boolean has_issues) {
        this.has_issues = has_issues;
    }

    @JsonProperty("has_projects")
    public Boolean getHasProjects() {
        return has_projects;
    }

    @JsonProperty("has_projects")
    public void setHasProjects(Boolean has_projects) {
        this.has_projects = has_projects;
    }

    @JsonProperty("has_downloads")
    public Boolean getHasDownloads() {
        return has_downloads;
    }

    @JsonProperty("has_downloads")
    public void setHasDownloads(Boolean has_downloads) {
        this.has_downloads = has_downloads;
    }

    @JsonProperty("has_wiki")
    public Boolean getHasWiki() {
        return has_wiki;
    }

    @JsonProperty("has_wiki")
    public void setHasWiki(Boolean has_wiki) {
        this.has_wiki = has_wiki;
    }

    @JsonProperty("has_pages")
    public Boolean getHasPages() {
        return has_pages;
    }

    @JsonProperty("has_pages")
    public void setHasPages(Boolean has_pages) {
        this.has_pages = has_pages;
    }

    @JsonProperty("forks_count")
    public Integer getForksCount() {
        return forks_count;
    }

    @JsonProperty("forks_count")
    public void setForksCount(Integer forks_count) {
        this.forks_count = forks_count;
    }

    @JsonProperty("mirror_url")
    public Object getMirrorUrl() {
        return mirror_url;
    }

    @JsonProperty("mirror_url")
    public void setMirrorUrl(Object mirror_url) {
        this.mirror_url = mirror_url;
    }

    @JsonProperty("disabled")
    public Boolean getDisabled() {
        return disabled;
    }

    @JsonProperty("disabled")
    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    @JsonProperty("open_issues_count")
    public Integer getOpenIssuesCount() {
        return open_issues_count;
    }

    @JsonProperty("open_issues_count")
    public void setopen_issuesCount(Integer open_issues_count) {
        this.open_issues_count = open_issues_count;
    }

    @JsonProperty("license")
    public License getLicense() {
        return license;
    }

    @JsonProperty("license")
    public void setLicense(License license) {
        this.license = license;
    }

    @JsonProperty("forks")
    public Integer getForks() {
        return forks;
    }

    @JsonProperty("forks")
    public void setForks(Integer forks) {
        this.forks = forks;
    }

    @JsonProperty("open_issues")
    public Integer getOpenIssues() {
        return open_issues;
    }

    @JsonProperty("open_issues")
    public void setOpenIssues(Integer open_issues) {
        this.open_issues = open_issues;
    }

    @JsonProperty("watchers")
    public Integer getWatchers() {
        return watchers;
    }

    @JsonProperty("watchers")
    public void setWatchers(Integer watchers) {
        this.watchers = watchers;
    }

    @JsonProperty("default_branch")
    public String getDefaultBranch() {
        return default_branch;
    }

    @JsonProperty("default_branch")
    public void setDefaultBranch(String default_branch) {
        this.default_branch = default_branch;
    }

    @JsonProperty("score")
    public Double getScore() {
        return score;
    }

    @JsonProperty("score")
    public void setScore(Double score) {
        this.score = score;
    }
}
