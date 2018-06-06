package org.xyc.showsome.pecan.rxjava;

import java.util.List;

import com.google.common.collect.Lists;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * created by wks on date: 2018/5/17
 */
public class SampleA {

    private static void subscribeObserver() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                //onCompleter方法就是告诉你已经完成了，就算我在再发消息，那是我发着玩的，别接受
                emitter.onComplete();
            }
        }).subscribe(new Observer<Integer>() {
            //这个Disopsable的dispose方法可以阻断事件流，不再接受数据
            private Disposable d;

            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("onSubscribe");
                this.d = d;
            }

            @Override
            public void onNext(Integer value) {
                System.out.println("onNext + " + value);
                if (value == 2) {
                    d.dispose();
                }
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError + " + e);
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });
    }

    private static void subscribeNext() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                //onCompleter方法就是告诉你已经完成了，就算我在再发消息，那是我发着玩的，别接受
                emitter.onComplete();
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println("accept + " + integer);
            }
        });
    }

    /**
     * subscribeOn() 指定的是上游发送事件的线程,用哪个线程发送出去,如果有重复，第一次后效
     * observeOn() 指定的是下游接收事件的线程.,用哪个线程来接受，如果有重复，可以覆盖

     多次指定上游的线程只有第一次指定的有效, 也就是说多次调用subscribeOn() 只有第一次的有效, 其余的会被忽略.

     多次指定下游的线程是可以的, 也就是说每调用一次observeOn() , 下游的线程就会切换一次.
     */
    private static void subscribeNextOnDifferentThread() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                System.out.println("producer " + Thread.currentThread().getName());
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                //onCompleter方法就是告诉你已经完成了，就算我在再发消息，那是我发着玩的，别接受
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(Schedulers.newThread()).subscribe(new Consumer<Integer>() {
                @Override
                public void accept(Integer integer) throws Exception {
                    System.out.println("consumer " + Thread.currentThread().getName());
                    System.out.println("accept + " + integer);
                }
        });

        //要加上点延迟才能打印，否则异步走完直接关闭程序了
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        subscribeObserver();
//        subscribeNext();
        subscribeNextOnDifferentThread();
    }
}
