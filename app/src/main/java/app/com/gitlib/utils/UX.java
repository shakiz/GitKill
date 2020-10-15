package app.com.gitlib.utils;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import app.com.gitlib.R;
import app.com.gitlib.adapters.AllTopicAdapter;
import app.com.gitlib.models.alltopic.Item;

public class UX {
    private Context context;
    public Dialog loadingDialog;
    private ArrayAdapter arrayAdapter;

    public UX(Context context) {
        this.context = context;
        loadingDialog = new Dialog(context);
    }

    public void getLoadingView(){
        loadingDialog.setContentView(R.layout.loading_layout);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();
    }

    public void removeLoadingView(){
        if (loadingDialog.isShowing()) loadingDialog.cancel();
    }

    public void setSpinnerAdapter(Spinner spinner, ArrayList<String> spinnerItemList) {
        arrayAdapter = new ArrayAdapter<>(context,R.layout.spinner_drop,spinnerItemList);
        arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }

    public void setSpinnerAdapter(Spinner spinner, String[] spinnerItemList) {
        arrayAdapter = new ArrayAdapter<>(context,R.layout.spinner_drop,spinnerItemList);
        arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }

    public AllTopicAdapter loadListView(ArrayList<Item> itemList, RecyclerView recyclerView , int layoutResId){
        AllTopicAdapter allTopicAdapter = new AllTopicAdapter(itemList, context, layoutResId);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(allTopicAdapter);
        allTopicAdapter.notifyDataSetChanged();
        return allTopicAdapter;
    }

    //region launch play store for rating
    public static void launchMarket(Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(myAppLinkToMarket);
            Toast.makeText(context, "Redirecting to play store...", Toast.LENGTH_LONG).show();
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "Unable to find market app", Toast.LENGTH_LONG).show();
        }
    }
    //endregion

    //region remove underline from autoLink web textView
    public static void stripUnderlines(TextView textView) {
        Spannable s = (Spannable) textView.getText();
        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
        for (URLSpan span: spans) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            s.setSpan(span, start, end, 0);
        }
        textView.setText(s);
    }

    private static class URLSpanNoUnderline extends URLSpan {
        public URLSpanNoUnderline(String url) {
            super(url);
        }
        @Override public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    }
    //endregion
}
