package com.ramesh.recyclerviewwithleftandrightarrows;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.ramesh.recyclerviewwithleftandrightarrows.data.DummyDataHelper;
import com.ramesh.recyclerviewwithleftandrightarrows.databinding.ActivityMainBinding;
import com.ramesh.recyclerviewwithleftandrightarrows.model.App;
import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.HORIZONTAL;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

public class MainActivityOne extends AppCompatActivity {

    private List<App> appsList = new ArrayList<>();
    RecyclerView recycler_view;

    //To track the position of the current visible item .for navigation with arrows
    int currentVisibleItem = 0;
    //To check whether user scrolled the recycler view or used arrows to navigate.
    private boolean programaticallyScrolled;
    ProgressBar progressBar;
    //LinearLayoutManagerWithSmoothScroller linearLayoutManagerWithSmoothScroller;
    private LinearLayoutManager linearLayoutManager;

    ImageView tv_left_arrow,tv_right_arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_one);
        init();
    }
    public void init(){
        tv_left_arrow = (ImageView)findViewById(R.id.tv_left_arrow);
        tv_right_arrow = (ImageView)findViewById(R.id.tv_right_arrow);
        recycler_view = (RecyclerView)findViewById(R.id.recycler_view);
        DummyDataHelper dummyDataHelper = new DummyDataHelper();
        appsList = dummyDataHelper.getAppList();
        AppListAdapter appListAdapter=new AppListAdapter(getApplicationContext(),appsList);

        linearLayoutManager=new LinearLayoutManager(MainActivityOne.this);
        linearLayoutManager.setOrientation(HORIZONTAL);
        recycler_view.setLayoutManager(linearLayoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(MainActivityOne.this, R.dimen.recyclerview_item_offset);
        recycler_view.addItemDecoration(itemDecoration);
        recycler_view.setAdapter(appListAdapter);

        SnapHelper snapHelper = new StartSnapHelper();
        snapHelper.attachToRecyclerView(recycler_view);

        recycler_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case SCROLL_STATE_DRAGGING:
                        //Indicated that user scrolled.
                        programaticallyScrolled = false;
                        break;
                    case SCROLL_STATE_IDLE:
                        if (!programaticallyScrolled) {
                            currentVisibleItem = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                            handleWritingViewNavigationArrows(false);
                        }
                        break;
                    default:
                        break;
                }
            }
        });

        tv_left_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                programaticallyScrolled = true;
                //Decrement current visible item position to navigate back to previous item
                currentVisibleItem--;
                handleWritingViewNavigationArrows(true);
            }
        });
        tv_right_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                programaticallyScrolled = true;
                //Increment current visible item position to navigate next item
                currentVisibleItem++;
                handleWritingViewNavigationArrows(true);
            }
        });
    }

    private void handleWritingViewNavigationArrows(boolean scroll) {
        if (currentVisibleItem == (recycler_view.getAdapter().getItemCount() - 1)) {
            tv_left_arrow.setVisibility(View.GONE);
            tv_right_arrow.setVisibility(View.VISIBLE);
        } else if (currentVisibleItem != 0) {
            tv_right_arrow.setVisibility(View.VISIBLE);
            tv_left_arrow.setVisibility(View.VISIBLE);
        } else if (currentVisibleItem == 0) {
            tv_left_arrow.setVisibility(View.GONE);
            tv_right_arrow.setVisibility(View.VISIBLE);
        }
        if (scroll) {
            recycler_view.smoothScrollToPosition(currentVisibleItem);
        }
    }
    /*public void setAdapter(RecyclerView.Adapter adapter) {
        recycler_view.setAdapter(adapter);
    }*/
}
