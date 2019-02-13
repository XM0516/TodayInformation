package com.news.today.todayinformation;
/*包名：com.news.today.todayinformation(不属于todayinformation，只为方便故放在这)
 * 文件名：TextRenovator.java
 * 创建者：Alan
 * 时间：201902021605
 * 用途：被观察者，子线程，尝试Handler.post方法能否带动run()方法运行? --> 测试答案：Affirmative.
 * */
import android.os.Handler;

public class TextRenovator implements  Runnable{

    private String textToRenovate;
    private final RenovateHandler renovateHandler;
    private Handler handler;

    public TextRenovator(String textFromInvoker, RenovateHandler renovateHandlerFromInvoker){
        this.textToRenovate = textFromInvoker;
        this.renovateHandler = renovateHandlerFromInvoker;
        handler = new Handler();
    }

    @Override
    public void run() {
        this.textToRenovate = "textChanged!!!";
        renovateHandler.onRenovate(textToRenovate);
    }

    public void start(){
        handler.post(this);
    }

    public interface RenovateHandler{
         void onRenovate(String text);
    }

}
