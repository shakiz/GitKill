package app.com.gitlib.models.repositories;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "node_id",
        "name",
        "full_name",
        "html_url",
        "description",
        "created_at",
        "updated_at",
        "pushed_at",
        "git_url",
        "ssh_url",
        "clone_url",
        "svn_url",
        "size",
        "stargazers_count",
        "watchers_count",
        "language",
        "has_issues",
        "has_projects",
        "has_downloads",
        "has_wiki",
        "has_pages",
        "forks_count",
        "open_issues_count",
        "license",
        "forks",
        "open_issues",
        "watchers",
        "default_branch"
})
public class Repo {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("node_id")
    private String nodeId;
    @JsonProperty("name")
    private String name;
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
    @JsonProperty("git_url")
    private String git_url;
    @JsonProperty("ssh_url")
    private String ssh_url;
    @JsonProperty("clone_url")
    private String clone_url;
    @JsonProperty("svn_url")
    private String svn_url;
    @JsonProperty("size")
    private Integer size;
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
    @JsonProperty("open_issues_count")
    private Integer open_issues_count;
    @JsonProperty("license")
    private Object license;
    @JsonProperty("forks")
    private Integer forks;
    @JsonProperty("open_issues")
    private Integer open_issues;
    @JsonProperty("watchers")
    private Integer watchers;
    @JsonProperty("default_branch")
    private String default_branch;

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("node_id")
    public String getNodeId() {
        return nodeId;
    }

    @JsonProperty("node_id")
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("full_name")
    public String getFullName() {
        return full_name;
    }

    @JsonProperty("full_name")
    public void setFullName(String fullName) {
        this.full_name = fullName;
    }

    @JsonProperty("html_url")
    public String getHtmlUrl() {
        return html_url;
    }

    @JsonProperty("html_url")
    public void setHtmlUrl(String htmlUrl) {
        this.html_url = htmlUrl;
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
    public void setCreatedAt(String createdAt) {
        this.created_at = createdAt;
    }

    @JsonProperty("updated_at")
    public String getUpdatedAt() {
        return updated_at;
    }

    @JsonProperty("updated_at")
    public void setUpdatedAt(String updatedAt) {
        this.updated_at = updatedAt;
    }

    @JsonProperty("pushed_at")
    public String getPushedAt() {
        return pushed_at;
    }

    @JsonProperty("pushed_at")
    public void setPushedAt(String pushedAt) {
        this.pushed_at = pushedAt;
    }

    @JsonProperty("git_url")
    public String getGitUrl() {
        return git_url;
    }

    @JsonProperty("git_url")
    public void setGitUrl(String gitUrl) {
        this.git_url = gitUrl;
    }

    @JsonProperty("ssh_url")
    public String getSshUrl() {
        return ssh_url;
    }

    @JsonProperty("ssh_url")
    public void setSshUrl(String sshUrl) {
        this.ssh_url = sshUrl;
    }

    @JsonProperty("clone_url")
    public String getCloneUrl() {
        return clone_url;
    }

    @JsonProperty("clone_url")
    public void setCloneUrl(String cloneUrl) {
        this.clone_url = cloneUrl;
    }

    @JsonProperty("svn_url")
    public String getSvnUrl() {
        return svn_url;
    }

    @JsonProperty("svn_url")
    public void setSvnUrl(String svnUrl) {
        this.svn_url = svnUrl;
    }

    @JsonProperty("size")
    public Integer getSize() {
        return size;
    }

    @JsonProperty("size")
    public void setSize(Integer size) {
        this.size = size;
    }

    @JsonProperty("stargazers_count")
    public Integer getStargazersCount() {
        return stargazers_count;
    }

    @JsonProperty("stargazers_count")
    public void setStargazersCount(Integer stargazersCount) {
        this.stargazers_count = stargazersCount;
    }

    @JsonProperty("watchers_count")
    public Integer getWatchersCount() {
        return watchers_count;
    }

    @JsonProperty("watchers_count")
    public void setWatchersCount(Integer watchersCount) {
        this.watchers_count = watchersCount;
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
    public void setHasIssues(Boolean hasIssues) {
        this.has_issues = hasIssues;
    }

    @JsonProperty("has_projects")
    public Boolean getHasProjects() {
        return has_projects;
    }

    @JsonProperty("has_projects")
    public void setHasProjects(Boolean hasProjects) {
        this.has_projects = hasProjects;
    }

    @JsonProperty("has_downloads")
    public Boolean getHasDownloads() {
        return has_downloads;
    }

    @JsonProperty("has_downloads")
    public void setHasDownloads(Boolean hasDownloads) {
        this.has_downloads = hasDownloads;
    }

    @JsonProperty("has_wiki")
    public Boolean getHasWiki() {
        return has_wiki;
    }

    @JsonProperty("has_wiki")
    public void setHasWiki(Boolean hasWiki) {
        this.has_wiki = hasWiki;
    }

    @JsonProperty("has_pages")
    public Boolean getHasPages() {
        return has_pages;
    }

    @JsonProperty("has_pages")
    public void setHasPages(Boolean hasPages) {
        this.has_pages = hasPages;
    }

    @JsonProperty("forks_count")
    public Integer getForksCount() {
        return forks_count;
    }

    @JsonProperty("forks_count")
    public void setForksCount(Integer forksCount) {
        this.forks_count = forksCount;
    }

    @JsonProperty("open_issues_count")
    public Integer getOpenIssuesCount() {
        return open_issues_count;
    }

    @JsonProperty("open_issues_count")
    public void setOpenIssuesCount(Integer openIssuesCount) {
        this.open_issues_count = openIssuesCount;
    }

    @JsonProperty("license")
    public Object getLicense() {
        return license;
    }

    @JsonProperty("license")
    public void setLicense(Object license) {
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
    public void setOpenIssues(Integer openIssues) {
        this.open_issues = openIssues;
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
    public void setDefaultBranch(String defaultBranch) {
        this.default_branch = defaultBranch;
    }
}
