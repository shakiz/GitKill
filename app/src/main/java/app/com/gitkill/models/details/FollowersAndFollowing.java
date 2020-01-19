package app.com.gitkill.models.details;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FollowersAndFollowing {
    @JsonProperty("login")
    private String login;
    @JsonProperty("html_url")
    private String html_url;

    public FollowersAndFollowing(String login, String html_url) {
        this.login = login;
        this.html_url = html_url;
    }

    @JsonProperty("login")
    public String getLogin() {
        return login;
    }

    @JsonProperty("login")
    public void setLogin(String login) {
        this.login = login;
    }

    @JsonProperty("html_url")
    public String getHtmlUrl() {
        return html_url;
    }

    @JsonProperty("html_url")
    public void setHtmlUrl(String html_url) {
        this.html_url = html_url;
    }

}
