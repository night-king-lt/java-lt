package redis;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author liu.teng
 * @Date 2020/9/11 16:10
 * @Version 1.0
 */
public class RedisTools {

    public static JedisCluster getJedisCluster(String adress, int maxConnect){
        // Jedis连接池配置
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 最大空闲连接数, 默认8个
        jedisPoolConfig.setMaxIdle(10);
        // 最大连接数, 默认8个
        jedisPoolConfig.setMaxTotal(maxConnect);
        // 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
        jedisPoolConfig.setMaxWaitMillis(2000); // 设置2秒
        //对拿到的connection进行validateObject校验
        jedisPoolConfig.setTestOnBorrow(true);

        return getJedisCluster(adress, jedisPoolConfig);
    }

    public static JedisCluster getJedisCluster(String adress, JedisPoolConfig jedisPoolConfig){
        // 添加集群的服务节点Set集合
        Set<HostAndPort> hostAndPortsSet = new HashSet<>();
        for (String next: adress.split(",")){
            String url = next.split(":")[0];
            int port = Integer.parseInt(next.split(":")[1]);
            hostAndPortsSet.add(new HostAndPort(url, port));
        }

        return getJedisCluster(hostAndPortsSet, jedisPoolConfig);
    }

    public static JedisCluster getJedisCluster(Set<HostAndPort> hostAndPortsSet, JedisPoolConfig jedisPoolConfig){
        return new JedisCluster(hostAndPortsSet, jedisPoolConfig);
    }

    public static void main(String[] args) {
        String adress = "host1:1111,host2:8888";
        for (String next: adress.split(",")){
            String url = next.split(":")[0];
            int port = Integer.parseInt(next.split(":")[1]);
            System.out.println(url + " : " + port);
        }
    }
}
