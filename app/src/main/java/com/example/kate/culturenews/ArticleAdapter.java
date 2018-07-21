package com.example.kate.culturenews;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ArticleAdapter extends ArrayAdapter<Article> {

    private Activity mContext;

    public ArticleAdapter(Activity context, ArrayList<Article> articles) {
        super(context, 0, articles);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.article, parent, false);
        }

        Article currentArticle = getItem(position);

        TextView title = listItemView.findViewById(R.id.title);
        title.setText(currentArticle.getTitle());

        TextView section = listItemView.findViewById(R.id.section);
        section.setText(currentArticle.getSection());

        TextView additionalInformation = listItemView.findViewById(R.id.additional_info);

        String authorToDisplay = currentArticle.getAuthor();

        String dateToDisplay = null;
        if (currentArticle.getDate() != null) {
            Date dateObject = currentArticle.getDate();
            SimpleDateFormat dateFormatter = new SimpleDateFormat("LLL dd, yyyy h:mm a");
            dateToDisplay = dateFormatter.format(dateObject);
        }

        String additionalInformationToDisplay = null;
        if (authorToDisplay != null && dateToDisplay != null) {
            additionalInformationToDisplay = mContext.getString(R.string.additional_info, authorToDisplay, dateToDisplay);
        } else if (authorToDisplay != null) {
            additionalInformationToDisplay = authorToDisplay;
        } else if (dateToDisplay != null) {
            additionalInformationToDisplay = dateToDisplay;
        }

        if (additionalInformationToDisplay != null) {
            additionalInformation.setText(additionalInformationToDisplay);
        } else {
            additionalInformation.setVisibility(View.GONE);
        }

        return listItemView;
    }
}
