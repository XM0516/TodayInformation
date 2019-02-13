package com.news.today.todayinformation;
/*包名：com.news.today.todayinformation(不属于todayinformation，只为方便故放在这)
* 文件名：testHandlerpostActivity.java
* 创建者：Alan
* 时间：201902021605
* 用途：将此Activity作为主线程，用以测试子线程中Hanler.post方法会不会连带Runnable的run方法一起执行
* */
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class testHandlerpostActivity extends AppCompatActivity {

    private TextView testHandlerPostView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_handler_post_activity);
        testHandlerPostView = findViewById(R.id.testHandlerPost);
        //Invoker
        TextRenovator textRenovator = new TextRenovator("notChangedString", new TextRenovator.RenovateHandler(){
            @Override
            public void onRenovate(String text) {
                testHandlerPostView.setText(text);
            }
        });
        textRenovator.start();

    }
}
