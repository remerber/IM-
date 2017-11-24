package com.wzh.italker.frags.search;


import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wzh.common.app.PresenterFragment;
import com.wzh.common.widget.EmptyView;
import com.wzh.common.widget.PortraitView;
import com.wzh.common.widget.recycler.RecyclerAdapter;
import com.wzh.factory.model.card.UserCard;
import com.wzh.factory.presenter.search.SearchContract;
import com.wzh.factory.presenter.search.SearchUserPresenter;
import com.wzh.italker.R;
import com.wzh.italker.activities.SearchActivity;

import java.util.List;

import butterknife.BindView;


/**
 * A simple {@link Fragment} subclass.
 *
 * @author wang
 */
public class SearchUserFragment extends PresenterFragment<SearchContract.Presenter>
        implements SearchActivity.SearchListener,
        SearchContract.UserView {

    @BindView(R.id.empty)
    EmptyView emptyView;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private RecyclerAdapter<UserCard> mAdapter;

    public SearchUserFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_user;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<UserCard>() {
            @Override
            protected int getItemViewType(int position, UserCard data) {
                return R.layout.cell_search_list;
            }

            @Override
            protected ViewHolder<UserCard> onCreateViewHolder(View root, int viewType) {
                return new SearchUserFragment.ViewHolder(root);
            }
        });
        //初始化占位布局
        emptyView.bind(mRecycler);
        setPlaceHolderView(emptyView);
    }

    @Override
    protected void initData() {
        super.initData();
        search("");
    }

    @Override
    public void search(String content) {
        mPresenter.search(content);
    }

    @Override
    public void onSearchDone(List<UserCard> userCards) {
        mAdapter.replace(userCards);
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    @Override
    protected SearchContract.Presenter initPresenter() {
        return new SearchUserPresenter(this);
    }


    class ViewHolder extends RecyclerAdapter.ViewHolder<UserCard> {

        @BindView(R.id.im_portrait)
        PortraitView mPortraitView;

        @BindView(R.id.txt_name)
        TextView mName;

        @BindView(R.id.im_follow)
        ImageView mFollow;


        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(UserCard data) {
            Glide.with(getActivity()).load(data.getPortrait()).into(mPortraitView);
            mName.setText(data.getName());
            mFollow.setEnabled(!data.isFollow());

        }
    }
}
