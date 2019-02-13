package com.news.today.todayinformation;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class CustomCountDownTimer implements  Runnable{
    //Final修饰过的变量其值不可改，且必须显式地初始化：1.定义时初始化 2.在构造方法中初始化 3.在非静态块中为final实例变量设置值（第三点还不懂）
    //Final修饰的方法不能重写
    //***Final修饰的类不能被继承***，***且类中所有成员方法隐式定义为final***
    private int time;
    private int countDownTime;
    private final ICountDownHandler countDownHandler;
    private final android.os.Handler handler; //注意要选择这个handler，Java原生还有一个handler，不是同一个类
    private boolean isRun;

    //1.实时地回调“倒计时到几秒”这个事件 (观察者设计模式)
    //2.支持动态传入总秒数
    //3.每过一秒，总秒数减一
    //4.总时间倒计时为0时，要回调完成的状态

    public CustomCountDownTimer(int time, ICountDownHandler icountDownHandler){
        //将Activity中调用本类构造器的参数映射到对象中(三行this的赋值)
        this.time = time;
        this.countDownTime = time;  //实时的秒数，是需要通过onTicker方法更新到UI上的
        //***注意***，下一行执行后，countDownHandler实际上就是Activity中的那个匿名内部类了，其onText和onFinish方法都会直接对
        //UI造成更新（因为重写方法中进行了更新操作）
        this.countDownHandler = icountDownHandler;
        handler = new android.os.Handler();
    }//Constructor

    //倒计时的逻辑。run()方法中的postDelayed本质上也是post方法的调用，功能一样的。所以写在run里形成一个循环判断
    @Override
    public void run() {
        if (isRun){ //判断run()函数到底是start()还是cancel()调用的
            if (countDownHandler != null){  //UI线程中调用的匿名类（被观察者）非空(即需要在重写方法中更新UI信息)
                countDownHandler.onTicker(countDownTime);   //由UI线程中重写过的onTicker方法将其更新到UI上
            }//if over
            if (countDownTime == 0){  //实时秒数等于0说明倒计时工作已经完成
                cancel();
                if (countDownHandler != null){  //不要漏掉这个判断
                    countDownHandler.onFinish();    //UI线程更新出“跳过”在界面上
                }
            }else{  //若实时秒数不等于0
               countDownTime = time--;  //更新秒数
               handler.postDelayed(this, 1000); //延迟1秒post
            }
        }
    }

    //开启倒计时
    public void start(){
        isRun = true;   //Activity中直接使用timer.start()方法，所以将flag置true，表明已经开始了倒计时
        //https://www.cnblogs.com/cheneasternsun/p/5467115.html 详细说明了Handler原理
        //post方法中会引用getPostMessage将Runnable类型封装成Message类。
        handler.post(this); //参数可以是Runnable类型的接口，也可以是实现了Runnable的类。
                                             //在这里使用this，就是这个方法所在的类，也就是实现了Runnable的CustomCountdownTimer
                                            //handler的发送消息方法都会把message进到MessageQueue中
    }

    //跳出循环，终止
    public void cancel(){
        isRun = false;
        handler.removeCallbacks(this);
    }

    //观察者回调借口（IOC数据回调）
    public interface ICountDownHandler{
        //倒计时回调
        void onTicker(int time);
         //完成时回调
        void onFinish();

    }
}
