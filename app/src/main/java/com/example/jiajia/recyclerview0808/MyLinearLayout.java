package com.example.jiajia.recyclerview0808;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

/**
 * Created by jiajia on 2016/8/8.
 */
public class MyLinearLayout extends AppCompatActivity {
    private RecyclerView rv_linear;
    private MyLinearAdapter myLinearAdapter;
    private LinearLayoutManager linearLayoutManager;
    private CoordinatorLayout cl_linear;
    private SwipeRefreshLayout sr_linear;
    private List<Beauty> beauties;
    private int page = 1;
    private int lastVisibleItem;
    private ItemTouchHelper itemTouchHelper;
    private int screenWidth;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.act_linear);
        Toolbar tb = (Toolbar) findViewById(R.id.tb_linear);
        setSupportActionBar(tb);//取代之前的导航栏
        loadDate();
        initView();
        initEvent();
        new GetData().execute("http://gank.io/api/data/福利/10/1");
    }

    public void setListener() {

    }

    public void loadDate() {
        //beauties内容加载

    }

    public void initView() {
        cl_linear = (CoordinatorLayout) findViewById(R.id.cl_linear);
        rv_linear = (RecyclerView) findViewById(R.id.rv_linear);
        sr_linear = (SwipeRefreshLayout) findViewById(R.id.sr_linear);
        sr_linear.setColorSchemeResources(R.color.sr_color_first, R.color.sr_color_second, R.color.sr_color_third);
        sr_linear.setProgressViewOffset(true, 100, 200);

        /*获取屏幕宽度*/
        WindowManager windowManager = (WindowManager) MyLinearLayout.this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;

    }

    public void initEvent() {
        sr_linear.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                new GetData().execute("http://gank.io/api/data/福利/10/1");
            }
        });

        myLinearAdapter = new MyLinearAdapter(beauties);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_linear.setLayoutManager(linearLayoutManager);
        rv_linear.setAdapter(myLinearAdapter);

        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            /**
             * 设置拖放和移动的方向；使用helperItemTouchHelper.makeMovementFlags(dragFlags, swipeFlags)来构造返回的flag
             *上下为拖动（drag），左右为滑动（swipe）
             * */
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlags,swipeFlags;
                dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                //设置侧滑方向为从左到右和从右到左都可以
                return makeMovementFlags(dragFlags,swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();
                Collections.swap(beauties,from,to);//交换指定位置的两个元素
                myLinearAdapter.notifyDataSetChanged();//适配器重新加载数据
                return true;
            }
            /**
             * 某一项的左右滑动
             * 移除该项
             *
             * */
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                myLinearAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }

            @Override
            public int convertToAbsoluteDirection(int flags, int layoutDirection) {
                return super.convertToAbsoluteDirection(flags, layoutDirection);
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                super.onSelectedChanged(viewHolder, actionState);
                if(actionState == ItemTouchHelper.ACTION_STATE_DRAG){
                    viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
                }
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.sr_color_third));

            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                viewHolder.itemView.setAlpha(1- Math.abs(dX)/screenWidth);
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);            }
        });

        rv_linear.addOnScrollListener(new RecyclerView.OnScrollListener() {
            /**
             * 滚动状态发生改变时发生回调
             * */
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                /**
                 * newState:
                 * SCROLL_STATE_IDLE = 0:当前屏幕停止滚动；
                 * SCROLL_STATE_DRAGGING = 1：屏幕在滚动，且用户扔在触屏屏幕；
                 * SCROLL_STATE_SETTLING = 2：随用户的操作，屏幕产生惯性滑动
                 * 滑动状态停止时并且只剩余两个item时自动加载
                 *
                 * */
                if(newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem +2 >= linearLayoutManager.getItemCount()){
                    new GetData().execute("http://gank.io/api/data/福利/10/" + (++page));
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //获取加载的最后一个可见视图在适配器的位置
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });

    }

    private class GetData extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return MyOkHttp.getData(strings[0]);
        }

        /*执行之前刷新列表*/
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            sr_linear.setRefreshing(true);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!TextUtils.isEmpty(s)) {
                Gson gson = new Gson();
                String jsonData = null;

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    jsonData = jsonObject.getString("results");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(beauties == null || beauties.size() == 0 ){
                    beauties =  gson.fromJson(jsonData,new TypeToken<List<Beauty>>(){}.getType());
                    Beauty pages = new Beauty();
                    pages.setPage(page);
                    beauties.add(pages);

                }else{
                    List<Beauty> newBeauties = gson.fromJson(jsonData,new TypeToken<List<Beauty>>(){}.getType());
                    beauties.addAll(newBeauties);
                    Beauty pages = new Beauty();
                    pages.setPage(page);
                    beauties.add(pages);
                }
                if(myLinearAdapter == null){
                    rv_linear.setAdapter( new MyLinearAdapter(beauties));
                    itemTouchHelper .attachToRecyclerView(rv_linear);//recycleView实现增删改
                }else {
                    myLinearAdapter.notifyDataSetChanged();//适配器重新加载数据
                }
                sr_linear.setRefreshing(false);//数据加载完毕停止刷新
            }

        }
    }
}
