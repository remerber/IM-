package com.wzh.italker.frags.main;


import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wzh.common.app.PresenterFragment;
import com.wzh.common.widget.EmptyView;
import com.wzh.common.widget.PortraitView;
import com.wzh.common.widget.recycler.RecyclerAdapter;
import com.wzh.factory.model.db.Group;
import com.wzh.factory.presenter.group.GroupsContract;
import com.wzh.factory.presenter.group.GroupsPresenter;
import com.wzh.italker.R;
import com.wzh.italker.activities.MessageActivity;

import butterknife.BindView;


/**
 * 我的群界面
 *
 * @author wang
 */
public class GroupFragment extends PresenterFragment<GroupsContract.Presenter>
        implements GroupsContract.View {


    @BindView(R.id.empty)
    EmptyView mEmptyView;


    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private RecyclerAdapter<Group> mAdapter;


    public GroupFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_group;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<Group>() {
            @Override
            protected int getItemViewType(int position, Group data) {
                return R.layout.cell_group_list;
            }

            @Override
            protected ViewHolder<Group> onCreateViewHolder(View root, int viewType) {
                return new GroupFragment.ViewHolder(root);
            }
        });
        // 点击事件监听
        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<Group>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, Group group) {

                MessageActivity.show(getContext(), group);

            }
        });


        mEmptyView.bind(mRecycler);
        setPlaceHolderView(mEmptyView);

    }

    @Override
    protected void onFirstInit() {
        super.onFirstInit();
        mPresenter.start();
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected GroupsContract.Presenter initPresenter() {
        return new GroupsPresenter(this);
    }

    @Override
    public RecyclerAdapter<Group> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<Group> {

        @BindView(R.id.im_portrait)
        PortraitView mPortraitView;

        @BindView(R.id.txt_name)
        TextView mName;

        @BindView(R.id.txt_desc)
        TextView mDesc;

        @BindView(R.id.txt_member)
        TextView mMember;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Group group) {
            mPortraitView.setup(Glide.with(GroupFragment.this), group.getPicture());
            mName.setText(group.getName());
            mDesc.setText(group.getDesc());
            if (group.holder != null && group.holder instanceof String) {
                mMember.setText((String) group.holder);
            } else {
                mMember.setText("");
            }

        }

    }
}
