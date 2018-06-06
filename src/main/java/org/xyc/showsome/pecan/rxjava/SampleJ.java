package org.xyc.showsome.pecan.rxjava;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * created by wks on date: 2018/6/6
 */
public class SampleJ {

    public static void useRequest() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(final FlowableEmitter<Integer> emitter) throws Exception {
                //这里是线程同步的情况，发送和接受在一个线程上，SampleK是异步的例子
                //这里的emitter.requested()是和下游的request是相通的，以此可以控制输入和输出
                System.out.println("before emit, requested = " + emitter.requested());
                System.out.println("emit 1");
                emitter.onNext(1);
                System.out.println("after emit 1, requested = " + emitter.requested());
                System.out.println("emit 2");
                emitter.onNext(2);
                System.out.println("after emit 2, requested = " + emitter.requested());
                System.out.println("emit 3");
                emitter.onNext(3);
                System.out.println("after emit 3, requested = " + emitter.requested());
                System.out.println("emit complete");
                emitter.onComplete();
                System.out.println("after emit complete, requested = " + emitter.requested());
            }
        }, BackpressureStrategy.ERROR).subscribe(new Subscriber<Integer>() {

            @Override
            public void onSubscribe(Subscription s) {
                System.out.println("onSubscribe");
                s.request(10);  //request 10
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
    }

    public static void main(String[] args) {
        useRequest();
    }
}
