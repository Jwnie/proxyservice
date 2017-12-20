package com.meow.proxy.deduplicate;

import org.apache.commons.collections.CollectionUtils;

import java.util.BitSet;
import java.util.List;

/**
 * @author Alex
 *         date:2017/12/18
 *         email:jwnie@foxmail.com
 */
public class SimpleBloomFilter {
    /**
     * 设置每个字符串在布隆过滤器中所占的位的大小（24位）
     */
    private static final int DEFAULT_SIZE = 2 << 24;
    /**
     * 产生随机数的种子，可产生6个不同的随机数产生器
     */
    private static final int[] seeds = new int[]{7, 11, 13, 31, 37, 61};
    /**
     * Java中的按位存储的思想，其算法的具体实现（布隆过滤器）
     */
    private BitSet bits = new BitSet(DEFAULT_SIZE);
    /**
     * 根据随机数的种子，创建6个哈希函数
     */
    private SimpleHash[] func = new SimpleHash[seeds.length];
    private static SimpleBloomFilter ourInstance = new SimpleBloomFilter();

    public static SimpleBloomFilter getInstance() {
        return ourInstance;
    }

    /**
     * 设置布隆过滤器所对应k（6）个哈希函数
     */
    private SimpleBloomFilter() {
        for (int i = 0; i < seeds.length; i++) {
            func[i] = new SimpleHash(DEFAULT_SIZE, seeds[i]);
        }
    }

    /**
     * 往过滤器中加去重数据
     *
     * @param value
     */
    public void add(String value) {
        for (SimpleHash f : func) {
            bits.set(f.hash(value), true);
        }
    }

    /**
     *
     * @param valueList
     */
    public void addValueList(List<String> valueList) {
        if (CollectionUtils.isEmpty(valueList)) {
            return;
        }
        for (String s : valueList) {
            add(s);
        }
    }


    /**
     * 是否已经包含该URL
     *
     * @param value
     * @return
     */
    public boolean contains(String value) {
        if (value == null) {
            return false;
        }
        boolean ret = true;
        //根据此URL得到在布隆过滤器中的对应位，并判断其标志位（6个不同的哈希函数产生6种不同的映射）
        for (SimpleHash f : func) {
            ret = ret && bits.get(f.hash(value));
        }
        return ret;
    }


    public static class SimpleHash {
        private int cap;
        private int seed;

        /**
         * 默认构造器，哈希表长默认为DEFAULT_SIZE大小，此哈希函数的种子为seed
         *
         * @param cap
         * @param seed
         */
        public SimpleHash(int cap, int seed) {
            this.cap = cap;
            this.seed = seed;
        }

        /**
         * @param value
         * @return
         */
        public int hash(String value) {
            int result = 0;
            int len = value.length();
            for (int i = 0; i < len; i++) {
                //将此URL用哈希函数产生一个值（使用到了集合中的每一个元素）
                result = seed * result + value.charAt(i);
            }
            //产生单个信息指纹
            return (cap - 1) & result;
        }
    }
}
