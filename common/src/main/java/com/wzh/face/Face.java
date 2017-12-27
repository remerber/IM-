package com.wzh.face;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.ArrayMap;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.wzh.common.R;
import com.wzh.utils.StreamUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by HP on 2017/12/24.
 * 表情工具类
 *
 * @author by wangWei
 */

public class Face {

    private static final ArrayMap<String, Bean> FACE_MAP = new ArrayMap<>();
    private static List<FaceTab> FACE_TABS = null;

    /**
     * 获得所有的表情
     *
     * @param context
     * @return
     */
    public static List<FaceTab> all(@NonNull Context context) {
        init(context);
        return FACE_TABS;

    }

    /**
     * 从spannable解析表情并替换显示
     *
     * @param target
     * @param spannable
     * @param size
     * @return
     */
    public static Spannable decode(@NonNull View target, Spannable spannable, int size) {
        if (spannable == null) {
            return null;
        }
        String str = spannable.toString();
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        Context context = target.getContext();
        // 进行正在匹配[][][]
        Pattern pattern = Pattern.compile("(\\[[^\\[\\]:\\s\\n]+\\])");
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            String key = matcher.group();
            if (TextUtils.isEmpty(key)) {
                continue;
            }

            Bean bean = get(context, key.replace("[", "").replace("]", ""));
            if (bean == null) {
                continue;
            }

            final int start = matcher.start();
            final int end = matcher.end();

            // 得到一个复写后的标示
            ImageSpan span = new FaceSpan(context, target, bean.preview, size);

            // 设置标示
            spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        }

        return spannable;
    }

    public static class FaceSpan extends ImageSpan {

        private Drawable mDrawable;
        private View mView;
        private int mSize;

        public FaceSpan(Context context, View view, Object source, final int size) {
            super(context, R.drawable.default_face, ALIGN_BOTTOM);
            this.mView = view;
            this.mSize = size;

            Glide.with(context)
                    .load(source)
                    .fitCenter()
                    .into(new SimpleTarget<GlideDrawable>(size, size) {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            mDrawable = resource.getCurrent();
                            int width = resource.getIntrinsicWidth();
                            int height = resource.getIntrinsicHeight();
                            mDrawable.setBounds(0, 0, width > 0 ? width : size,
                                    height > 0 ? height : size);

                            mView.invalidate();
                        }
                    });

        }

        @Override
        public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {

            Rect rect = mDrawable != null ? mDrawable.getBounds() :
                    new Rect(0, 0, mSize, mSize);

            if (fm != null) {
                fm.ascent = -rect.bottom;
                fm.descent = 0;

                fm.top = fm.ascent;
                fm.bottom = 0;
            }

            return rect.right;
        }

        @Override
        public Drawable getDrawable() {
            return mDrawable;
        }

        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
            if (mDrawable != null) {
                super.draw(canvas, text, start, end, x, top, y, bottom, paint);
            }

        }
    }

    public static Bean get(Context context, String key) {
        init(context);
        if (FACE_MAP.containsKey(key)) {
            return FACE_MAP.get(key);
        }
        return null;
    }

    /**
     * 输入表情
     *
     * @param context
     * @param editable
     * @param bean
     * @param size
     */
    public static void inputFace(@NonNull final Context context, final Editable editable,
                                 final Face.Bean bean, final int size) {
        Glide.with(context)
                .load(bean.preview)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>(size, size) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Spannable spannable = new SpannableString(String.format("[%s]", bean.key));
                        ImageSpan span = new ImageSpan(context, resource, ImageSpan.ALIGN_BASELINE);

                        spannable.setSpan(span, 0, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        editable.append(spannable);

                    }
                });
    }

    /**
     * 初始化
     *
     * @param context
     */
    private static void init(Context context) {
        if (FACE_TABS == null) {
            synchronized (Face.class) {
                if (FACE_TABS == null) {
                    ArrayList<FaceTab> faceTabs = new ArrayList<>();
                    FaceTab tab = initAssetsFace(context);
                    if (tab != null) {
                        faceTabs.add(tab);
                    }

                    tab = initResourceFace(context);
                    if (tab != null) {
                        faceTabs.add(tab);
                    }

                    for (FaceTab faceTab : faceTabs) {
                        faceTab.copyToMap(FACE_MAP);
                    }

                    // init list 不可变的集合
                    FACE_TABS = Collections.unmodifiableList(faceTabs);
                }
            }

        }

    }


    /**
     * 从drawable资源中加载数据并映射到对应的key
     *
     * @param context
     * @return
     */
    private static FaceTab initResourceFace(Context context) {
        final ArrayList<Bean> faces = new ArrayList<>();
        final Resources resources = context.getResources();
        String packageName = context.getApplicationInfo().packageName;
        for (int i = 1; i <= 142; i++) {


            String key = String.format(Locale.ENGLISH, "fb%03d", i);
            String resStr = String.format(Locale.ENGLISH, "face_base_%03d", i);

            int resId = resources.getIdentifier(resStr, "drawable", packageName);
            if (resId == 0) {
                continue;
            }

            faces.add(new Bean(key, resId));

        }

        if (faces.size() == 0) {
            return null;
        }

        return new FaceTab("NAME", faces.get(0).preview, faces);
    }

    /**
     * 从face-t.zip包解析我们的表情
     *
     * @param context
     * @return
     */
    private static FaceTab initAssetsFace(Context context) {
        String faceAsset = "face-t.zip";
        // data/data/包名/files/face/ft/*
        String faceCacheDir = String.format("%s/face/tf", context.getFilesDir());
        File faceFolder = new File(faceCacheDir);
        if (!faceFolder.exists()) {
            // 不存在进行初始化
            if (faceFolder.mkdirs()) {
                try {
                    InputStream inputStream = context.getAssets().open(faceAsset);
                    // 存储文件
                    File faceSource = new File(faceFolder, "source.zip");
                    // copy
                    StreamUtil.copy(inputStream, faceSource);

                    // 解压
                    unZipFile(faceSource, faceFolder);

                    // 清理文件
                    StreamUtil.delete(faceSource.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        // info.json
        File infoFile = new File(faceCacheDir, "info.json");
        // Gson

        Gson gson = new Gson();
        JsonReader reader;
        try {
            reader = gson.newJsonReader(new FileReader(infoFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        // 解析
        FaceTab tab = gson.fromJson(reader, FaceTab.class);

        // 相对路径到绝对路径
        for (Bean face : tab.faces) {
            face.preview = String.format("%s/%s", faceCacheDir, face.preview);
            face.source = String.format("%s/%s", faceCacheDir, face.source);
        }

        return tab;
    }

    /**
     * 把zipFile解压到desDir目录
     *
     * @param zipFile
     * @param desDir
     * @throws IOException
     */
    private static void unZipFile(File zipFile, File desDir) throws IOException {
        final String folderPath = desDir.getAbsolutePath();

        ZipFile zf = new ZipFile(zipFile);
        // 判断节点进行循环
        for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            // 过滤缓存的文件
            String name = entry.getName();
            if (name.startsWith(".")) {
                continue;
            }

            // 输入流
            InputStream in = zf.getInputStream(entry);
            String str = folderPath + File.separator + name;

            // 防止名字错乱
            str = new String(str.getBytes("8859_1"), "GB2312");

            File desFile = new File(str);
            // 输出文件
            StreamUtil.copy(in, desFile);
        }
    }


    /**
     * 每一个表情盘，含有多个表情
     */
    public static class FaceTab {

        public List<Bean> faces = new ArrayList<>();
        public String name;
        public Object preview;

        FaceTab(String name, Object preview, List<Bean> faces) {
            this.faces = faces;
            this.name = name;
            this.preview = preview;
        }


        public void copyToMap(ArrayMap<String, Bean> faceMap) {
            for (Bean face : faces) {
                faceMap.put(face.key, face);
            }
        }
    }


    /**
     * 每一个表情
     */
    public static class Bean {

        Bean(String key, int preview) {
            this.key = key;
            this.source = preview;
            this.preview = preview;
        }

        public String key;
        public String desc;
        public Object source;
        public Object preview;
    }
}
