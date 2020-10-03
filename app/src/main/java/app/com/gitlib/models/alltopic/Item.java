package app.com.gitlib.models.alltopic;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({
    "full_name",
    "html_url",
    "avatar_url",
    "description",
    "created_at",
    "updated_at",
    "pushed_at",
    "clone_url",
    "stargazers_count",
    "watchers_count",
    "language",
    "forks_count",
    "license",
    "forks",
    "open_issues",
    "watchers"
})
public class Item implements Parcelable {

    @JsonProperty("full_name")
    private String full_name;
    @JsonProperty("html_url")
    private String html_url;
    @JsonProperty("avatar_url")
    private String avatar_url;
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
    @JsonProperty("stargazers_count")
    private Integer stargazers_count;
    @JsonProperty("watchers_count")
    private Integer watchers_count;
    @JsonProperty("language")
    private String language;
    @JsonProperty("forks_count")
    private Integer forks_count;
    @JsonProperty("license")
    private License license;
    @JsonProperty("forks")
    private Integer forks;
    @JsonProperty("open_issues")
    private Integer open_issues;
    @JsonProperty("watchers")
    private Integer watchers;

    public Item(String full_name,String avatar_url, String html_url, String language, Integer stargazers_count, Integer watchers_count, Integer forks_count, Integer forks, Integer watchers) {
        this.full_name = full_name;
        this.avatar_url = avatar_url;
        this.html_url = html_url;
        this.language = language;
        this.stargazers_count = stargazers_count;
        this.watchers_count = watchers_count;
        this.forks_count = forks_count;
        this.forks = forks;
        this.watchers = watchers;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getPushed_at() {
        return pushed_at;
    }

    public void setPushed_at(String pushed_at) {
        this.pushed_at = pushed_at;
    }

    public String getClone_url() {
        return clone_url;
    }

    public void setClone_url(String clone_url) {
        this.clone_url = clone_url;
    }

    public Integer getStargazers_count() {
        return stargazers_count;
    }

    public void setStargazers_count(Integer stargazers_count) {
        this.stargazers_count = stargazers_count;
    }

    public Integer getWatchers_count() {
        return watchers_count;
    }

    public void setWatchers_count(Integer watchers_count) {
        this.watchers_count = watchers_count;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getForks_count() {
        return forks_count;
    }

    public void setForks_count(Integer forks_count) {
        this.forks_count = forks_count;
    }

    public License getLicense() {
        return license;
    }

    public void setLicense(License license) {
        this.license = license;
    }

    public Integer getForks() {
        return forks;
    }

    public void setForks(Integer forks) {
        this.forks = forks;
    }

    public Integer getOpen_issues() {
        return open_issues;
    }

    public void setOpen_issues(Integer open_issues) {
        this.open_issues = open_issues;
    }

    public Integer getWatchers() {
        return watchers;
    }

    public void setWatchers(Integer watchers) {
        this.watchers = watchers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected Item(Parcel in) {
        full_name = in.readString();
        avatar_url = in.readString();
        html_url = in.readString();
        language = in.readString();
        stargazers_count = in.readInt();
        watchers_count = in.readInt();
        forks_count = in.readInt();
        forks = in.readInt();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(this.full_name);
        parcel.writeString(this.avatar_url);
        parcel.writeString(this.html_url);
        parcel.writeString(this.language);
        parcel.writeInt(this.stargazers_count);
        parcel.writeInt(this.watchers_count);
        parcel.writeInt(this.forks_count);
        parcel.writeInt(this.forks);
    }
}
