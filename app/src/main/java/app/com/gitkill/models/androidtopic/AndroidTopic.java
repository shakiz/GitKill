package app.com.gitkill.models.androidtopic;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({
        "total_count",
        "incomplete_results",
        "items"
})
public class AndroidTopic {

    @JsonProperty("total_count")
    private Integer total_count;
    @JsonProperty("incomplete_results")
    private Boolean incomplete_results;
    @JsonProperty("items")
    private List<Item> items;

    public AndroidTopic(List<Item> items) {
        this.items = items;
    }

    @JsonProperty("total_count")
    public Integer getTotal_count() {
        return total_count;
    }

    @JsonProperty("total_count")
    public void setTotalCount(Integer total_count) {
        this.total_count = total_count;
    }

    @JsonProperty("incomplete_results")
    public Boolean getIncompleteResults() {
        return incomplete_results;
    }

    @JsonProperty("incomplete_results")
    public void setIncompleteResults(Boolean incomplete_results) {
        this.incomplete_results = incomplete_results;
    }

    @JsonProperty("items")
    public List<Item> getItems() {
        return items;
    }

    @JsonProperty("items")
    public void setItems(List<Item> items) {
        this.items = items;
    }



}
