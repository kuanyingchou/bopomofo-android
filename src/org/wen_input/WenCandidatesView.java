package org.wen_input;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.inputmethodservice.InputMethodService;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputConnection;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class WenCandidatesView extends LinearLayout {
    
    private WenInputService inputService;
    private List<? extends Object> list = new ArrayList<Object>();
    private LinearLayout listView;
    final private HorizontalScrollView scroller;
    final private int elmtSize = 24;
    
    public WenCandidatesView(WenInputService context) {
        super(context);
        
        setBackgroundColor(Color.WHITE);

        listView = new LinearLayout(context);
        
        scroller = new HorizontalScrollView(context);

        final LayoutParams lp = new LayoutParams(
                LayoutParams.WRAP_CONTENT, 
                LayoutParams.WRAP_CONTENT);
        lp.weight = 1;

        scroller.setLayoutParams(lp);

        scroller.addView(listView);

        final View prev = createNextPrevButton(FOCUS_LEFT);
        final View next = createNextPrevButton(FOCUS_RIGHT);

        addView(prev);
        addView(scroller);
        addView(next);

        setService(context);
      
    }
    
    private void setService(WenInputService service) {
        inputService = service;
    }
    
    public void setCandidates(List<? extends Object> c) {
        list = (c == null)? new ArrayList<Object>(): c;
        listView.removeAllViews();
        for(final Object s: list) {
            listView.addView(createElement(s.toString()));
        }
        scroller.fullScroll(FOCUS_LEFT);
    }

    public boolean hasCandidates() {
        return ! list.isEmpty();
    }
    public Object getFirstCandidate() {
        if(list.size() > 0) { return list.get(0); }
        else { return null; }
    }
    
    private View createElement(final String s) {
        final Button elmt = new Button(getContext());
        /*
        final ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(50, 50);
        elmt.setLayoutParams(lp);
        */
        elmt.setTextSize(elmtSize);
        elmt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                System.out.println("choose "+s);
                inputService.sendString(s);
            }
        });
        elmt.setText(s);
        return elmt;
    }

    private View createNextPrevButton(final int direction) {
        final Button btn = new Button(getContext());
        btn.setText((direction == FOCUS_RIGHT)? ">": "<");
        btn.setTextSize(elmtSize);
        btn.setLayoutParams(new LayoutParams(
                LayoutParams.WRAP_CONTENT, 
                LayoutParams.WRAP_CONTENT));
        btn.setOnClickListener(new OnClickListener() {
            public void onClick(View arg) {
                scroller.pageScroll(direction);
            }
        });
        return btn;
    }

    public void clear() {
        setCandidates(new ArrayList<Object>());
    }
}
