package com.wzh.italker.frags.panel;


import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.wzh.common.app.BaseApplication;
import com.wzh.common.app.BaseFragment;
import com.wzh.common.tools.AudioRecordHelper;
import com.wzh.common.tools.UITool;
import com.wzh.common.widget.AudioRecordView;
import com.wzh.common.widget.GalleryView;
import com.wzh.common.widget.recycler.RecyclerAdapter;
import com.wzh.face.Face;
import com.wzh.italker.R;

import net.qiujuer.genius.ui.Ui;

import java.io.File;
import java.util.List;


/**
 * @author wang
 */
public class PanelFragment extends BaseFragment {
    private PanelCallback mCallback;
    private View mFacePanel, mGalleryPanel, mRecordPanel;

    public PanelFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_panel;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        initFace(root);
        initGallery(root);
        initRecord(root);
    }


    public void setup(PanelCallback callback) {
        mCallback = callback;
    }

    private void initFace(View root) {

        final View facePanel = mFacePanel = root.findViewById(R.id.lay_panel_face);

        View backspace = facePanel.findViewById(R.id.im_backspace);
        backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 删除逻辑

                PanelCallback callback = mCallback;
                if (callback == null) {
                    return;
                }

                // 模拟一个键盘删除点击
                KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL,
                        0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                callback.getInputEditText().dispatchKeyEvent(event);
            }
        });


        TabLayout tableLayout = (TabLayout) root.findViewById(R.id.tab);
        ViewPager viewPager = (ViewPager) root.findViewById(R.id.pager);
        tableLayout.setupWithViewPager(viewPager);

        // 每一表情显示48dp
        final int minFaceSize = (int) Ui.dipToPx(getResources(), 48);
        final int totalScreen = UITool.getScreenWidth(getActivity());
        final int spanCount = totalScreen / minFaceSize;

        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return Face.all(getContext()).size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.lay_face_content, container, false);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));

                List<Face.Bean> faces = Face.all(getContext()).get(position).faces;
                FaceAdapter adapter = new FaceAdapter(faces, new RecyclerAdapter.AdapterListenerImpl<Face.Bean>() {
                    @Override
                    public void onItemClick(RecyclerAdapter.ViewHolder holder, Face.Bean bean) {
                        if (mCallback == null) {
                            return;
                        }
                        EditText editText = mCallback.getInputEditText();
                        Face.inputFace(getContext(), editText.getText(), bean, (int)
                                (editText.getTextSize() + Ui.dipToPx(getResources(), 2)));
                    }

                });
                recyclerView.setAdapter(adapter);
                container.addView(recyclerView);

                return recyclerView;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                // 拿到表情盘的描述
                return Face.all(getContext()).get(position).name;
            }
        });
    }

    private void initGallery(View root) {

        View galleryPanel = mGalleryPanel = root.findViewById(R.id.lay_gallery_panel);
        final GalleryView galleryView = (GalleryView) mGalleryPanel.findViewById(R.id.view_gallery);
        final TextView selectedSize = (TextView) galleryPanel.findViewById(R.id.txt_gallery_select_count);
        galleryView.setUp(getLoaderManager(), new GalleryView.SelectedChangeListener() {
            @Override
            public void onSelectedCountChanged(int count) {
                String resStr = getText(R.string.label_gallery_selected_size).toString();
                selectedSize.setText(String.format(resStr, count));
            }
        });
        galleryPanel.findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGalleySendClick(galleryView, galleryView.getSelectedPath());
            }
        });


    }


    private void initRecord(View root) {
        View recordView = mRecordPanel = root.findViewById(R.id.lay_panel_record);
        final AudioRecordView audioRecordView = (AudioRecordView) recordView
                .findViewById(R.id.view_audio_record);
        //录音缓存文件
        File tmpFile = BaseApplication.getAudioTmpFile(true);
        final AudioRecordHelper helper=new AudioRecordHelper(tmpFile, new AudioRecordHelper.RecordCallback() {
            @Override
            public void onRecordStart() {

            }

            @Override
            public void onProgress(long time) {

            }

            @Override
            public void onRecordDone(File file, long time) {
                if (time < 1000) {
                    return;
                }
                File audioFile = BaseApplication.getAudioTmpFile(false);
                if (file.renameTo(audioFile)) {
                    // 通知到聊天界面
                    PanelCallback panelCallback = mCallback;
                    if (panelCallback != null) {
                        panelCallback.onRecordDone(audioFile, time);
                    }
                }
            }
        });

        audioRecordView.setup(new AudioRecordView.Callback() {
            @Override
            public void requestStartRecord() {
                helper.recordAsync();
            }

            @Override
            public void requestStopRecord(int type) {
                switch (type) {
                    case AudioRecordView.END_TYPE_CANCEL:
                    case AudioRecordView.END_TYPE_DELETE:
                        // 删除和取消都代表想要取消
                        helper.stop(true);
                        break;
                    case AudioRecordView.END_TYPE_NONE:
                    case AudioRecordView.END_TYPE_PLAY:
                        // 播放暂时当中就是想要发送
                        helper.stop(false);
                        break;
                    default:
                        break;
                }
            }
        });


    }


    private void onGalleySendClick(GalleryView galleryView, String[] selectedPath) {
        galleryView.clear();
        PanelCallback callback = mCallback;
        if (callback == null) {
            return;
        }
        callback.onSendGallery(selectedPath);
    }


    public void showFace() {
        mRecordPanel.setVisibility(View.GONE);
        mGalleryPanel.setVisibility(View.GONE);
        mFacePanel.setVisibility(View.VISIBLE);
    }

    public void showRecord() {
        mRecordPanel.setVisibility(View.VISIBLE);
        mGalleryPanel.setVisibility(View.GONE);
        mFacePanel.setVisibility(View.GONE);
    }

    public void showGallery() {
        mRecordPanel.setVisibility(View.GONE);
        mGalleryPanel.setVisibility(View.VISIBLE);
        mFacePanel.setVisibility(View.GONE);
    }

    public interface PanelCallback {
        EditText getInputEditText();

        void onSendGallery(String[] paths);

        void onRecordDone(File file, long time);
    }
}
