package com.ecust.test;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.google.gson.Gson;
import com.jdjr.crawler.tcpj.utils.ThreadSleepUtils;
import org.junit.Test;

import java.io.*;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 类描述
 *
 * @Author gaojunwei
 * @Date 2020/8/4 17:20
 **/
public class MainTest {

    @Test
    public void refr() {
        ReferenceQueue<Object> referenceQueue = new ReferenceQueue<>();


        Object obj1 = new Object();
        WeakReference<Object> weakObj1 = new WeakReference<Object>(obj1);
        WeakReference<Object> weakObj2 = new WeakReference<Object>(new Object(), referenceQueue);
        System.out.println(referenceQueue.poll());
//主动回收
        System.gc();
        System.out.println(weakObj1.get());//非null
        System.out.println(weakObj2.get()); // null
        System.out.println(referenceQueue.poll().get());

        //System.out.println(referenceQueue.poll().get());
    }

    @Test
    public void bloomTest() {
        //预期数据量
        int insertions = 1000000;
        //期望的误判率
        double fpp = 0.02;

        //初始化一个存储string数据的布隆过滤器,默认误判率是0.02
        BloomFilter<String> bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8), insertions, fpp);

        //用于存放所有实际存在的key，用于是否存在
        Set<String> sets = new HashSet<>(insertions);

        //用于存放所有实际存在的key，用于取出
        List<String> lists = new ArrayList<>(insertions);

        //插入随机字符串
        for (int i = 0; i < insertions; i++) {
            String uuid = UUID.randomUUID().toString();
            bloomFilter.put(uuid);
            sets.add(uuid);
            lists.add(uuid);
        }

        int rightNum = 0;
        int wrongNum = 0;
        for (int i = 0; i < 10000; i++) {
            // 0-10000之间，可以被100整除的数有100个（100的倍数）
            String data = i % 100 == 0 ? lists.get(i / 100) : UUID.randomUUID().toString();
            //这里用了might,看上去不是很自信，所以如果布隆过滤器判断存在了,我们还要去sets中实锤
            if (bloomFilter.mightContain(data)) {
                if (sets.contains(data)) {
                    rightNum++;
                    continue;
                }
                wrongNum++;
            }
        }

        BigDecimal percent = new BigDecimal(wrongNum).divide(new BigDecimal(9900), 2, RoundingMode.HALF_UP);
        BigDecimal bingo = new BigDecimal(9900 - wrongNum).divide(new BigDecimal(9900), 2, RoundingMode.HALF_UP);
        System.out.println("在100W个元素中，判断100个实际存在的元素，布隆过滤器认为存在的：" + rightNum);
        System.out.println("在100W个元素中，判断9900个实际不存在的元素，误认为存在的：" + wrongNum + "，命中率：" + bingo + "，误判率：" + percent);
    }


    @Test
    public void test001() throws IOException, ClassNotFoundException {
        //测试是对象
        UserInfo userInfo = new UserInfo(10, "gjw", "game");
        //序列化
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File("a.txt")));
        out.writeObject(userInfo);
        System.out.println("对象序列化完成 " + userInfo + " " + UserInfo.count);
        out.close();
        //反序列化
        ObjectInputStream in = new ObjectInputStream(new FileInputStream("a.txt"));
        UserInfo user = (UserInfo) in.readObject();
        System.out.println("反序列化完成 " + user + " " + UserInfo.count);
        in.close();
    }


    static class User {
        private int age;
        private String name;

        public User(int age, String name) {
            this.age = age;
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private static Gson gson = new Gson();

    public static void main(String[] args) {


        User userA = new User(1, "A");
        AtomicStampedReference<User> userAtomicStampedReference = new AtomicStampedReference<>(userA, 1);

        new Thread(() -> {
            User userB = new User(2, "A");
            ThreadSleepUtils.log("A 修改:" + userAtomicStampedReference.compareAndSet(userA, userB, userAtomicStampedReference.getStamp(), userAtomicStampedReference.getStamp() + 1)
                    + gson.toJson(userA) + " " + userA + " 运行结束");
        }).start();

        new Thread(() -> {
            User userB = new User(2, "A");
            ThreadSleepUtils.log("A 修改:" + userAtomicStampedReference.compareAndSet(userA, userB, userAtomicStampedReference.getStamp(), userAtomicStampedReference.getStamp() + 2)
                    + gson.toJson(userA) + " " + userA + " 运行结束");
        }).start();

        ThreadSleepUtils.sleep(500);
    }


}