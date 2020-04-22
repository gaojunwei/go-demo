package com.gjw.common.algorithm; /**
 * Created by 李勇志 on 2016/12/6.
 * 2014301500370
 * <p>
 * 本工程包含两个数据文件
 * fulldata为老师给的原始数据文件，由于数据量过大程序跑不出来结果，没有选用进行测试
 * top1000data是从fulldata中摘取的前1000条数据，本程序运行的结果是基于这前1000条数据进行的频繁项集挖掘和关联度分析
 */

import com.alibaba.fastjson.JSON;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Apriori算法实现 最大模式挖掘，涉及到支持度，但没有置信度计算
 * <p>
 * AssociationRulesMining()函数实现置信度计算和关联规则挖掘
 */
public class AprioriMyself {
    /**
     * 迭代次数
     */
    public static int times = 0;
    /**
     * 最小支持率-用于过滤数据
     */
    private static double MIN_SUPPROT = 0.02;
    /**
     * 最小置信度-用于过滤数据
     */
    private static double MIN_CONFIDENCE = 0.6;
    /**
     * 循环状态，迭代标识
     */
    private static boolean endTag = false;
    /**
     * 原始事务数据集合
     */
    static List<List<String>> record = new ArrayList<>();
    /**
     * 存储所有的频繁项集
     */
    static List<List<String>> frequentItemset = new ArrayList<>();
    /***
     * 存放频繁项集和对应的支持度计数
     */
    static List<Mymap> map = new ArrayList();

    public static void main(String args[]) {
        System.out.println("请输入最小支持度（如0.05）和最小置信度（如0.6）");

        /**读取待处理事务数据*/
        record = getRecord("D:\\cd\\top1000data");
        System.out.println("读取数据集record成功，" + record.size());

        /**调用Apriori算法获得频繁项集*/
        Apriori();
        System.out.println("频繁模式挖掘完毕。");
        System.out.println();
        System.out.println("进行关联度挖掘，最小支持度百分比为：" + MIN_SUPPROT + "  最小置信度为：" + MIN_CONFIDENCE);

        /**挖掘关联规则*/
        AssociationRulesMining();
    }

    /**
     * 从文件中读取待分析的事务数据
     */
    public static List<List<String>> getRecord(String url) {
        List<List<String>> record = new ArrayList<>();
        InputStreamReader read = null;
        try {
            /**字符编码(可解决中文乱码问题 )*/
            String encoding = "UTF-8";
            File file = new File(url);
            if (file.isFile() && file.exists()) {
                read = new InputStreamReader(new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                /**
                 * 读取每一个事务
                 * 事务：库中的每一条记录被称为一笔事务。在上表的购物篮事务中，每一笔事务都表示一次购物行为。
                 */
                String lineTXT;
                while ((lineTXT = bufferedReader.readLine()) != null) {
                    String[] lineString = lineTXT.split(",");
                    record.add(Arrays.asList(lineString).stream().sorted().collect(Collectors.toList()));
                }

            } else {
                System.out.println("找不到指定的文件！");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容操作出错");
            e.printStackTrace();
        } finally {
            if (read != null) {
                try {
                    read.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return record;
    }


    /**
     * 实现apriori算法
     */
    public static void Apriori() {
        //************获取候选1项集**************
        System.out.println("第一次扫描后的1级 备选集CandidateItemset");
        List<List<String>> CandidateItemset = findFirstCandidate(record);
        System.out.println("备选集CandidateItemset集合长度：" + CandidateItemset.size());

        //************获取频繁1项集***************
        System.out.println("第一次扫描后的1级 频繁集FrequentItemset");
        List<List<String>> FrequentItemset = getSupprotedItemset(CandidateItemset);
        System.out.println(String.format("第一次扫描后的1级 最大前项数数量：%s，%s", FrequentItemset.size(), JSON.toJSONString(FrequentItemset)));
        /**添加到所有的频繁项集中*/
        AddToFrequenceItem(FrequentItemset);

        //*****************************迭代过程**********************************
        times = 2;
        while (endTag != true) {

            System.out.println("*******************************第" + times + "次扫描后备选集");
            //**********连接操作****获取候选times项集**************
            List<List<String>> nextCandidateItemset = getNextCandidate(FrequentItemset);
            System.out.println("*获取候选times项集："+JSON.toJSONString(nextCandidateItemset));

            /**************计数操作***由候选k项集选择出频繁k项集****************/
            System.out.println("*******************************第" + times + "次扫描后频繁集");
            List<List<String>> nextFrequentItemset = getSupprotedItemset(nextCandidateItemset);
            AddToFrequenceItem(nextFrequentItemset);//添加到所有的频繁项集中
            //输出所有的频繁项集
            ShowData(nextFrequentItemset);


            //*********如果循环结束，输出最大模式**************
            if (endTag == true) {
                System.out.println("\n\n\nApriori算法--->最大频繁集==================================");
                ShowData(FrequentItemset);
            }
            //****************下一次循环初值********************
            FrequentItemset = nextFrequentItemset;
            times++;//迭代次数加一
        }
    }

    public static int getCount(List<String> in)//根据频繁项集得到 其支持度计数
    {
        int rt = 0;
        for (int i = 0; i < map.size(); i++) {
            Mymap tem = map.get(i);
            if (tem.isListEqual(in)) {
                rt = tem.getcount();
                return rt;
            }
        }
        return rt;

    }


    /**
     * 添加到所有的频繁项集中
     */
    public static boolean AddToFrequenceItem(List<List<String>> fre) {
        for (int i = 0; i < fre.size(); i++) {
            frequentItemset.add(fre.get(i));
        }
        return true;
    }

    /**
     * 显示出candidateitem中的所有的项集
     */
    public static void ShowData(List<List<String>> CandidateItemset) {
        for (int i = 0; i < CandidateItemset.size(); i++) {
            List<String> list = new ArrayList<>(CandidateItemset.get(i));
            for (int j = 0; j < list.size(); j++) {
                System.out.print(list.get(j) + " ");
            }
            System.out.println();
        }
    }


    /**
     * 有当前频繁项集自连接求下一次候选集
     */
    private static List<List<String>> getNextCandidate(List<List<String>> FrequentItemset) {
        List<List<String>> nextCandidateItemset = new ArrayList<>();

        for (int i = 0; i < FrequentItemset.size(); i++) {
            HashSet<String> hsSet = new HashSet<>();
            HashSet<String> hsSettemp;
            for (int k = 0; k < FrequentItemset.get(i).size(); k++)//获得频繁集第i行
                hsSet.add(FrequentItemset.get(i).get(k));
            int hsLength_before = hsSet.size();//添加前长度
            hsSettemp = (HashSet<String>) hsSet.clone();
            for (int h = i + 1; h < FrequentItemset.size(); h++) {//频繁集第i行与第j行(j>i)连接   每次添加且添加一个元素组成    新的频繁项集的某一行，
                hsSet = (HashSet<String>) hsSettemp.clone();//！！！做连接的hasSet保持不变
                for (int j = 0; j < FrequentItemset.get(h).size(); j++)
                    hsSet.add(FrequentItemset.get(h).get(j));
                int hsLength_after = hsSet.size();
                if (hsLength_before + 1 == hsLength_after && isnotHave(hsSet, nextCandidateItemset)) {
                    //如果不相等，表示添加了1个新的元素       同时判断其不是候选集中已经存在的一项
                    Iterator<String> itr = hsSet.iterator();
                    List<String> tempList = new ArrayList<String>();
                    while (itr.hasNext()) {
                        String Item = (String) itr.next();
                        tempList.add(Item);
                    }
                    nextCandidateItemset.add(tempList);
                }
            }
        }
        return nextCandidateItemset;
    }


    /**
     * 判断新添加元素形成的候选集是否在新的候选集中
     */
    private static boolean isnotHave(HashSet<String> hsSet, List<List<String>> nextCandidateItemset) {//判断hsset是不是candidateitemset中的一项
        List<String> tempList = new ArrayList<String>();
        Iterator<String> itr = hsSet.iterator();
        while (itr.hasNext()) {//将hsset转换为List<String>
            String Item = (String) itr.next();
            tempList.add(Item);
        }
        for (int i = 0; i < nextCandidateItemset.size(); i++)//遍历candidateitemset，看其中是否有和templist相同的一项
            if (tempList.equals(nextCandidateItemset.get(i)))
                return false;
        return true;
    }


    /**
     * 对所有的商品进行支持度计数
     * 由k项候选集剪枝得到k项频繁集
     */
    private static List<List<String>> getSupprotedItemset(List<List<String>> candidateItemset) {
        boolean end = true;
        List<List<String>> supportedItemset = new ArrayList<>();

        for (int i = 0; i < candidateItemset.size(); i++) {
            /**统计记录数*/
            int count = countFrequent1(candidateItemset.get(i));
            /**最低支持度阈值过滤*/
            if (count >= MIN_SUPPROT * (record.size())) {
                supportedItemset.add(candidateItemset.get(i));
                map.add(new Mymap(candidateItemset.get(i), count));//存储当前频繁项集以及它的支持度计数
                end = false;
            }
        }
        endTag = end;//存在频繁项集则不会结束
        if (endTag == true)
            System.out.println("*****************无满足支持度的" + times + "项集,结束连接");
        return supportedItemset;
    }


    /**
     * 统计record中出现list集合的个数
     */
    private static int countFrequent1(List<String> list) {//遍历所有数据集record，对单个候选集进行支持度计数

        int count = 0;
        for (int i = 0; i < record.size(); i++)//从record的第一个开始遍历
        {
            boolean flag = true;
            for (int j = 0; j < list.size(); j++)//如果record中的第一个数据集包含list中的所有元素
            {
                String t = list.get(j);
                if (!record.get(i).contains(t)) {
                    flag = false;
                    break;
                }
            }
            if (flag)
                count++;//支持度加一
        }
        return count;//返回支持度计数
    }

    /**
     * 获得一项候选集
     */
    private static List<List<String>> findFirstCandidate(List<List<String>> recordData) {
        List<List<String>> tableList = new ArrayList<>();
        /**数据去重*/
        List<String> allList = new ArrayList<>();
        recordData.stream().forEach(itemList -> allList.addAll(itemList.stream().distinct().collect(Collectors.toList())));
        List<String> distinctList = allList.stream().distinct().collect(Collectors.toList());
        /**封装结果数据*/
        distinctList.stream().forEach(item -> tableList.add(Arrays.asList(item)));
        return tableList;
    }


    /**
     * 关联规则挖掘
     */
    public static void AssociationRulesMining() {
        for (int i = 0; i < frequentItemset.size(); i++) {
            List<String> tem = frequentItemset.get(i);
            if (tem.size() > 1) {
                List<String> temclone = new ArrayList<>(tem);
                List<List<String>> AllSubset = getSubSet(temclone);//得到频繁项集tem的所有子集
                for (int j = 0; j < AllSubset.size(); j++) {
                    List<String> s1 = AllSubset.get(j);
                    List<String> s2 = gets2set(tem, s1);
                    double conf = isAssociationRules(s1, s2, tem);
                    if (conf > 0)
                        System.out.println("置信度为：" + conf);
                }
            }

        }
    }

    public static List<List<String>> getSubSet(List<String> set) {
        List<List<String>> result = new ArrayList<>();    //用来存放子集的集合，如{{},{1},{2},{1,2}}
        int length = set.size();
        int num = length == 0 ? 0 : 1 << (length);    //2的n次方，若集合set为空，num为0；若集合set有4个元素，那么num为16.

        //从0到2^n-1（[00...00]到[11...11]）
        for (int i = 1; i < num - 1; i++) {
            List<String> subSet = new ArrayList<>();

            int index = i;
            for (int j = 0; j < length; j++) {
                if ((index & 1) == 1) {//每次判断index最低位是否为1，为1则把集合set的第j个元素放到子集中
                    subSet.add(set.get(j));
                }
                index >>= 1;        //右移一位
            }

            result.add(subSet);        //把子集存储起来
        }
        return result;
    }

    public static List<String> gets2set(List<String> tem, List<String> s1)//计算tem减去s1后的集合即为s2
    {
        List<String> result = new ArrayList<>();

        for (int i = 0; i < tem.size(); i++)//去掉s1中的所有元素
        {
            String t = tem.get(i);
            if (!s1.contains(t))
                result.add(t);
        }
        return result;
    }

    public static double isAssociationRules(List<String> s1, List<String> s2, List<String> tem)//判断是否为关联规则
    {
        double confidence = 0;
        int counts1;
        int countTem;
        if (s1.size() != 0 && s1 != null && tem.size() != 0 && tem != null) {
            counts1 = getCount(s1);
            countTem = getCount(tem);
            confidence = countTem * 1.0 / counts1;

            if (confidence >= MIN_CONFIDENCE) {
                System.out.print("关联规则：" + s1.toString() + "=>>" + s2.toString() + "   ");
                return confidence;
            } else
                return 0;

        } else
            return 0;

    }
}

/***
 * 自定义的map类，一个对象存放一个频繁项集以及其支持度计数
 */
class Mymap {
    public List<String> li = new LinkedList<>();
    public int count;

    public Mymap(List<String> l, int c)//构造函数  新建一个对象
    {
        li = l;
        count = c;
    }

    public int getcount()//返回得到当前频繁项集的支持度计数
    {
        return count;
    }

    public boolean isListEqual(List<String> in)//判断传入的频繁项集是否和本频繁项集相同
    {
        if (in.size() != li.size())//先判断大小是否相同
            return false;
        else {
            for (int i = 0; i < in.size(); i++)//遍历输入的频繁项集，判断是否所有元素都包含在本频繁项集中
            {
                if (!li.contains(in.get(i)))
                    return false;
            }
        }
        return true;//如果两个频繁项集大小相同，同时本频繁项集包含传入的频繁项集的所有元素，则表示两个频繁项集是相等的，返回为真
    }
}