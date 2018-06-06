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
public class SampleE {

    public static void doSome() {
        Flowable<Integer> upstream = Flowable.create(new FlowableOnSubscribe<Integer>() {
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
        }, BackpressureStrategy.ERROR); //增加了一个参数
        //BackpressureStrategy.ERROR, 这种方式会在出现上下游流速不均衡的时候直接抛出一个异常,这个异常就是著名的MissingBackpressureException

        Subscriber<Integer> downstream = new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                System.out.println("onSubscribe");
                s.request(Long.MAX_VALUE);  //限制输入的数量，说明处理的能力
                s.cancel(); //和Disposable.dispose效果一样，中断输入
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
        };
        upstream.subscribe(downstream);
    }

    public static void main(String[] args) {
        doSome();
    }
}
