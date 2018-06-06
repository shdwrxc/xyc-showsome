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
public class SampleK {

    private static Subscription subscription;

    /**
     * 异步
     * 根据request来平衡发送和接受
     */
    public static void useRequest() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                System.out.println("First requested = " + emitter.requested());
                boolean flag;
                for (int i = 0; ; i++) {
                    flag = false;
                    while (emitter.requested() == 0) {
                        if (!flag) {
                            System.out.println("Oh no! I can't emit value!");
                            flag = true;
                        }
                    }
                    emitter.onNext(i);
                    System.out.println("emit " + i + " , requested = " + emitter.requested());
                }
            }
        }, BackpressureStrategy.ERROR).subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread()).subscribe(new Subscriber<Integer>() {

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

        //然后我们调用request(96)，这会让下游去消费96个事件
        //当下游消费掉第96个事件之后，上游又开始发事件了，而且可以看到当前上游的requested的值是96(打印出来的95是已经发送了一个事件减一之后的值)，最终发出了第223个事件之后又进入了等待区，而223-127 正好等于 96。
        //这是不是说明当下游每消费96个事件便会自动触发内部的request()去设置上游的requested的值啊！没错，就是这样，而这个新的值就是96。
        //可以手动试试请求95个事件，上游是不会继续发送事件的
        subscription.request(96);

        try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        useRequest();
    }
}
