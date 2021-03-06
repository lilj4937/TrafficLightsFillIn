package com.fred.trafficlightsfillin.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fred.trafficlightsfillin.R;
import com.fred.trafficlightsfillin.base.BaseRecyclerAdapter;
import com.fred.trafficlightsfillin.base.BaseViewHolder;
import com.fred.trafficlightsfillin.intersection.bean.StageResponse;
import com.itheima.wheelpicker.WheelPicker;

import java.util.List;

public class DialogUtils {
    /**
     * 按钮点击回调接口
     */
    public interface OnButtonClickListener {
        /**
         * 确定按钮点击回调方法
         */
        void onPositiveButtonClick();

        /**
         * 取消按钮点击回调方法
         */
        void onNegativeButtonClick();

        /**
         * 选中的item
         */
        void onChoiceItem(String str,int pos);
    }

    /***
     * @param context
     */
    public static void showChoiceDialog(Activity context, List<String> data,OnButtonClickListener listener) {
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setCancelable(true);
        dialog.show();

        Window window = dialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setContentView(R.layout.layout_str_pictiker);
        WheelPicker picker = window.findViewById(R.id.picker);

        picker.setData(data);
        picker.setOnItemSelectedListener((wheelPicker, o, i) -> {
            if(listener!=null){
                listener.onChoiceItem(data.get(i),i);
            }
        });

        //给弹窗设置宽高
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        WindowManager manager = context.getWindowManager();
        Display display = manager.getDefaultDisplay();
        lp.width = display.getWidth()-100;
        dialog.getWindow().setAttributes(lp);

//        dialog.setOnDismissListener(dialog1 -> {
//            Log.e("fred","选择的数据2："+data.get(picker.getCurrentItemPosition()));
//            if(listener!=null){
//                listener.onChoiceItem(data.get(picker.getCurrentItemPosition()),picker.getCurrentItemPosition());
//            }
//        });
    }

    /**
     * 次序选择弹窗
     * @param context
     * @param data
     * @param listener
     */
    public static void showTimingChoiceDialog(Activity context, List<StageResponse.StageChanel> data, OnButtonClickListener listener) {
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setCancelable(true);
        dialog.show();

        Window window = dialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setContentView(R.layout.layout_timing_pictiker);

        RecyclerView content=window.findViewById(R.id.content);
        GridLayoutManager layoutManager=new GridLayoutManager(context,4);
        content.setLayoutManager(layoutManager);

        TimingAdapter timingAdapter=new TimingAdapter();
        timingAdapter.bindData(true,data);
        content.setAdapter(timingAdapter);

        timingAdapter.setOnItemClickListener((adapter, holder, itemView, index) -> {
            if (listener!=null){
                listener.onChoiceItem(data.get(index).no,index);
            }
            dialog.dismiss();
        });
        //给弹窗设置宽高
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        WindowManager manager = context.getWindowManager();
        Display display = manager.getDefaultDisplay();
        lp.width = display.getWidth()-100;
        dialog.getWindow().setAttributes(lp);
    }


    static class TimingAdapter extends BaseRecyclerAdapter<StageResponse.StageChanel> {

        @Override
        public int bindView(int viewType) {
            return R.layout.layout_timting_pictiker_item;
        }

        @Override
        public void onBindHolder(BaseViewHolder holder, @Nullable StageResponse.StageChanel stageChanel, int index) {
            TextView number = holder.obtainView(R.id.tv_no);
            ImageView picture = holder.obtainView(R.id.iv_picture);

            number.setText(stageChanel.no);
            String[] split = stageChanel.image.split(",");
            String base64 = split[1];
            byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            picture.setImageBitmap(decodedByte);
        }
    }
}
