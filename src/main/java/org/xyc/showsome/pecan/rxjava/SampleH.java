package org.xyc.showsome.pecan.rxjava;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * created by wks on date: 2018/6/6
 */
public class SampleH {

    /**
     * 这里不是Flowable.create,所以不能设定策略
     *
     * 但是提供了如下的方法
     * onBackpressureBuffer()
     onBackpressureDrop()
     onBackpressureLatest()
     */
    public static void useInterval() {
        Flowable.interval(1, TimeUnit.MICROSECONDS)
                .onBackpressureDrop()   //非Flowable.create时使用
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        System.out.println("onSubscribe");
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        System.out.println("onNext: " + aLong);
                        try {
                            Thread.sleep(1000);  //延时1秒
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.out.println("onError: " + t);
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                });

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        useInterval();
    }
}
