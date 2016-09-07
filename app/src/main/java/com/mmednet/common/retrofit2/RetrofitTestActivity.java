package com.mmednet.common.retrofit2;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mmednet.common.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RetrofitTestActivity extends BaseActivity {


    String TAG = RetrofitTestActivity.class.getSimpleName();

    @Bind(R.id.srl_content)
    SwipeRefreshLayout mSrlContent;
    @Bind(R.id.rv_content)
    RecyclerView mRvContent;

    private List<Gank.Content> mDatas = new ArrayList<Gank.Content>();
    private GanAdapter mAdapter;
    //观察者
    Observer<Gank> observer = new Observer<Gank>() {
        @Override
        public void onCompleted() {
            Toast.makeText(RetrofitTestActivity.this, "加载完成", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }

        @Override
        public void onNext(Gank gen) {
            mDatas = gen.results;
            mAdapter.setDatas();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_test);
        ButterKnife.bind(this);
        mSrlContent.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.GRAY);
        mRvContent.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new GanAdapter(this);
        mAdapter.setDatas();
        mRvContent.setAdapter(mAdapter);
        subscription = NetWork.getGanApi().getArticles(10, 1).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


    class GanAdapter extends RecyclerView.Adapter<GanAdapter.GanViewHolder> {

        private Context context;

        private GanAdapter(Context context) {
            this.context = context;
        }


        @Override
        public GanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            GanViewHolder holder = new GanViewHolder(LayoutInflater.from(context).inflate(R.layout.item_char, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(GanViewHolder holder, int position) {
            holder.mTvTitle.setText(mDatas.get(position).title);
            holder.mTvTime.setText(mDatas.get(position).publishedAt);
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        public void setDatas() {
            notifyDataSetChanged();
        }

        class GanViewHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.tv_title)
            TextView mTvTitle;
            @Bind(R.id.tv_time)
            TextView mTvTime;

            public GanViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
