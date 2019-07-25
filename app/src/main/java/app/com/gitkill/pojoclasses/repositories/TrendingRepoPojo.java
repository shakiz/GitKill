
package app.com.gitkill.pojoclasses.repositories;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({
    "author",
    "name",
    "avatar",
    "url",
    "description",
    "language",
    "languageColor",
    "stars",
    "forks",
    "currentPeriodStars",
    "user"
})
public class TrendingRepoPojo {

    @JsonProperty("author")
    private String author;
    @JsonProperty("name")
    private String name;
    @JsonProperty("avatar")
    private String avatar;
    @JsonProperty("url")
    private String url;
    @JsonProperty("description")
    private String description;
    @JsonProperty("language")
    private String language;
    @JsonProperty("languageColor")
    private String languageColor;
    @JsonProperty("stars")
    private Integer stars;
    @JsonProperty("forks")
    private Integer forks;
    @JsonProperty("currentPeriodStars")
    private Integer currentPeriodStars;
    @JsonProperty("user")
    private List<User> user = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("author")
    public String getAuthor() {
        return author;
    }

    @JsonProperty("author")
    public void setAuthor(String author) {
        this.author = author;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("avatar")
    public String getAvatar() {
        return avatar;
    }

    @JsonProperty("avatar")
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("language")
    public String getLanguage() {
        return language;
    }

    @JsonProperty("language")
    public void setLanguage(String language) {
        this.language = language;
    }

    @JsonProperty("languageColor")
    public String getLanguageColor() {
        return languageColor;
    }

    @JsonProperty("languageColor")
    public void setLanguageColor(String languageColor) {
        this.languageColor = languageColor;
    }

    @JsonProperty("stars")
    public Integer getStars() {
        return stars;
    }

    @JsonProperty("stars")
    public void setStars(Integer stars) {
        this.stars = stars;
    }

    @JsonProperty("forks")
    public Integer getForks() {
        return forks;
    }

    @JsonProperty("forks")
    public void setForks(Integer forks) {
        this.forks = forks;
    }

    @JsonProperty("currentPeriodStars")
    public Integer getCurrentPeriodStars() {
        return currentPeriodStars;
    }

    @JsonProperty("currentPeriodStars")
    public void setCurrentPeriodStars(Integer currentPeriodStars) {
        this.currentPeriodStars = currentPeriodStars;
    }

    @JsonProperty("user")
    public List<User> getUser() {
        return user;
    }

    @JsonProperty("user")
    public void setUser(List<User> user) {
        this.user = user;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
