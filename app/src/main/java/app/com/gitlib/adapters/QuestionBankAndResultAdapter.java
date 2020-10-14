package app.com.gitlib.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import app.com.gitlib.R;
import app.com.gitlib.models.questionbank.Result;

public class QuestionBankAndResultAdapter extends RecyclerView.Adapter<QuestionBankAndResultAdapter.ViewHolder> {
    private ArrayList<Result> results;
    private Context context;

    public QuestionBankAndResultAdapter(ArrayList<Result> results, Context context) {
        this.results = results;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_layout_it_question_answer_list,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final ViewHolder holder = viewHolder;
        final int pos = position;
        final Result result = results.get(position);
        holder.Question.setText((position+1)+".Question : "+result.getQuestion());
        holder.Answer.setText(context.getString(R.string.tap_to_view_the_answer));

        viewHolder.Answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.Answer.getText().equals(context.getString(R.string.tap_to_view_the_answer))){
                    holder.Answer.setText((pos+1)+".Answer : "+result.getCorrectAnswer());
                }
                else{
                    holder.Answer.setText(context.getString(R.string.tap_to_view_the_answer));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView Question , Answer;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.item_card_view);
            Question = itemView.findViewById(R.id.Question);
            Answer = itemView.findViewById(R.id.Answer);
        }
    }
}
