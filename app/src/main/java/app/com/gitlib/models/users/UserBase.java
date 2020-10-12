package app.com.gitlib.models.users;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "total_count",
        "incomplete_results",
        "items"
})
public class UserBase {

    @JsonProperty("total_count")
    private Integer total_count;
    @JsonProperty("incomplete_results")
    private Boolean incomplete_results;
    @JsonProperty("items")
    private List<TrendingDevelopers> items = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("total_count")
    public Integer getTotalCount() {
        return total_count;
    }

    @JsonProperty("total_count")
    public void setTotalCount(Integer totalCount) {
        this.total_count = totalCount;
    }

    @JsonProperty("incomplete_results")
    public Boolean getIncompleteResults() {
        return incomplete_results;
    }

    @JsonProperty("incomplete_results")
    public void setIncompleteResults(Boolean incompleteResults) {
        this.incomplete_results = incompleteResults;
    }

    @JsonProperty("items")
    public List<TrendingDevelopers> getItems() {
        return items;
    }

    @JsonProperty("items")
    public void setItems(List<TrendingDevelopers> items) {
        this.items = items;
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