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
public class SampleG {

    private static Subscription subscription;

    public static void useBuffer() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; ; i++) {
                    emitter.onNext(i);
                }
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread()).subscribe(new Subscriber<Integer>() {

            //BackpressureStrategy.DROP ，满了之后，新来的全部丢弃，下游处理后，接着存，到存满继续丢弃
            //BackpressureStrategy.LATEST ，和drop差不多，但是这个会始终保留最后一个事件

            @Override
            public void onSubscribe(Subscription s) {
                System.out.println("onSubscribe");
                subscription = s;
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("onNext: " + integer);
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("onError" + t);
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });

        try {
            Thread.sleep(1000 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        useBuffer();
    }
}
