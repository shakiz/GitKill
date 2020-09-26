package app.com.gitlib.models.questionbank;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "question",
        "correct_answer"
})
public class Result {

    @JsonProperty("question")
    private String question;
    @JsonProperty("correct_answer")
    private String correct_answer;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Result() {
    }

    public Result(String question, String correctAnswer) {
        this.question = question;
        this.correct_answer = correctAnswer;
    }

    @JsonProperty("question")
    public String getQuestion() {
        return question;
    }

    @JsonProperty("question")
    public void setQuestion(String question) {
        this.question = question;
    }

    @JsonProperty("correct_answer")
    public String getCorrectAnswer() {
        return correct_answer;
    }

    @JsonProperty("correct_answer")
    public void setCorrectAnswer(String correctAnswer) {
        this.correct_answer = correctAnswer;
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
