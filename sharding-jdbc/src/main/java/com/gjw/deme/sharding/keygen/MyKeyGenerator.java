package com.gjw.deme.sharding.keygen;

import org.apache.shardingsphere.spi.keygen.ShardingKeyGenerator;

import java.util.Properties;

/**
 * 自定义生成ID
 */
public class MyKeyGenerator implements ShardingKeyGenerator {
    @Override
    public Comparable<?> generateKey() {
        long id = System.currentTimeMillis();
        System.out.println("自定义生成ID=" + id);
        return id;
    }

    @Override
    public String getType() {
        return "my_keygen";
    }

    @Override
    public Properties getProperties() {
        return null;
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
