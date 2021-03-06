package de.albert.bihler.andrvoc.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import de.albert.bihler.andrvoc.model.Vokabel;
import de.albert.bihler.andrvoc.orangeiron.R;

public class QuestionListViewAdapter extends BaseAdapter {

    private List<String> translations;

    private final LayoutInflater inflater;

    Typeface roboto_light;

    public QuestionListViewAdapter(Context ctx) {
        this.inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.roboto_light = Typeface.createFromAsset(ctx.getAssets(), "fonts/Roboto/Roboto-Light.ttf");
    }

    public QuestionListViewAdapter(Context ctx, Vokabel word) {
        setWord(word);
        this.inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.roboto_light = Typeface.createFromAsset(ctx.getAssets(), "fonts/Roboto/Roboto-Light.ttf");
    }

    @Override
    public int getCount() {
        if (this.translations != null) {
            return this.translations.size();
        }
        return 0;
    }

    @Override
    public String getItem(int index) {
        return this.translations.get(index);
    }

    @Override
    public long getItemId(int index) {
        return -1;
    }

    public void setWord(Vokabel word) {
        this.translations = new ArrayList<String>(word.getAlternativeTranslations());
        this.translations.add(word.getCorrectTranslation());
        Collections.shuffle(translations);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View view, ViewGroup rootView) {
        if (view == null) {
            view = inflater.inflate(R.layout.item_question_list, null);
        }

        TextView textView = (TextView) view.findViewById(R.id.translation);
        textView.setText(getItem(position));
        textView.setTypeface(this.roboto_light);

        return view;
    }

}