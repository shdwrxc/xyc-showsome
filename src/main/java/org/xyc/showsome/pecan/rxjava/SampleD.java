package org.xyc.showsome.pecan.rxjava;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * created by wks on date: 2018/6/6
 */
public class SampleD {

    public static void useFilter() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; ; i++) {
                    emitter.onNext(i);
                }
            }
        }).subscribeOn(Schedulers.io()).filter(new Predicate<Integer>() {
            @Override
            /**
             * 过滤数
             */ public boolean test(Integer integer) throws Exception {
                return integer % 10 == 0;
            }
        }).observeOn(Schedulers.newThread()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println(integer);
            }
        });
    }

    public static void useSample() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; ; i++) {
                    emitter.onNext(i);
                }
            }
        }).subscribeOn(Schedulers.io())
                .sample(2, TimeUnit.SECONDS)    //2秒取一次数据
                .observeOn(Schedulers.newThread()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println(integer);
            }
        });
    }

    public static void main(String[] args) {
        useFilter();
    }
}
