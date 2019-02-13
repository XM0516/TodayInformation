package com.news.today.todayinformation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.VideoView;

public class FullScreenVideoView extends VideoView {


    public FullScreenVideoView(Context context) {
        super(context);
    }
    //主要用于xml文件中，支持自定义属性
    public FullScreenVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    //也是主要用于xml文件中，支持自定义属性，同时支持style样式
    public FullScreenVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /* onMeasure方法就是决定VideoView在屏幕大小和占比的决定性方法。
        先可记住这种让视频全屏的方法，会应用，了解部分底层原理（见笔记）。
        1. 建立新类继承视频视图（VideoView）类；
        2. 重写onMeasure()测量方法
    */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //widthMeasureSpec 包含两个主要内容：1.测量模式。2.测量大小。
        int width =  getDefaultSize(0, widthMeasureSpec); //getDefaultSize函数中会把两个参数@param中的模式和大小取出，
        int height = getDefaultSize(0, heightMeasureSpec);//然后根据模式（这里是match_parent）来更改其大小数值以符合这个模式。
        //换言之，上两行的width和height即是“视频全屏”所需要的width和height。
        setMeasuredDimension(width, height); //所以通过设置width和height的方法来迫使视频match_parent，也即全屏。
       //super.onMeasure(widthMeasureSpec, heightMeasureSpec); 因重写该方法，所以不能用父类的方法，要注释掉。
    }
}
