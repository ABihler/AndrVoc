package de.albert.bihler.andrvoc.adapter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import de.albert.bihler.andrvoc.model.Lesson;
import de.albert.bihler.andrvoc.orangeiron.R;

public class LessonListViewAdapter extends BaseAdapter {

    private final List<Lesson> lessons;
    private final LayoutInflater inflater;

    Typeface roboto_light;
    Typeface roboto_regular;

    public LessonListViewAdapter(Context ctx, List<Lesson> lessons) {
        Collections.sort(lessons);
        this.lessons = lessons;
        this.inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.roboto_light = Typeface.createFromAsset(ctx.getAssets(), "fonts/Roboto/Roboto-Light.ttf");
        this.roboto_regular = Typeface.createFromAsset(ctx.getAssets(), "fonts/Roboto/Roboto-Regular.ttf");
    }

    @Override
    public int getCount() {
        return this.lessons.size();
    }

    @Override
    public Lesson getItem(int index) {
        return this.lessons.get(index);
    }

    @Override
    public long getItemId(int index) {
        return getItem(index).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup rootView) {
        if (view == null) {
            view = inflater.inflate(R.layout.item_lesson_list, null);
        }

        TextView lessonName = (TextView) view.findViewById(R.id.lessonName);
        TextView lessonWordCount = (TextView) view.findViewById(R.id.lessonWordCount);
        lessonName.setText(getItem(position).getName());
        lessonWordCount.setText(getItem(position).getVocabulary().size() + "");
        lessonName.setTypeface(this.roboto_light);
        lessonWordCount.setTypeface(this.roboto_regular);

        return view;
    }

}
