package org.xyc.showsome.pecan.rxjava;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * created by wks on date: 2018/6/6
 */
public class SampleF {

    private static Subscription subscription;

    public static void useRequest() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                System.out.println("emit 1");
                emitter.onNext(1);
                System.out.println("emit 2");
                emitter.onNext(2);
                System.out.println("emit 3");
                emitter.onNext(3);
                System.out.println("emit complete");
                emitter.onComplete();
            }
        }, BackpressureStrategy.ERROR).subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread()).subscribe(new Subscriber<Integer>() {

            @Override
            public void onSubscribe(Subscription s) {
                System.out.println("onSubscribe");
                subscription = s;  //把Subscription保存起来
                //这个地方上游传递数据放在了中间的缓存里，下游需要就去拉
                //但是这个缓存是有大小的，默认128个，超过128个会报异常
                //上游一直发，下游需要一直接受，尽量不要满128个
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("onNext: " + integer);
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

        /**
         * 这个场景是3秒拉一条数据
         * 类似的场景还有页面点击下按钮就拉一次
         */
        while (true) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            subscription.request(1);
        }
    }

    public static void main(String[] args) {
        useRequest();
    }
}
