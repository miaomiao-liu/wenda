package com.nowcoder.util;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

import java.util.List;

/**
 * @Author: miaomiao
 * @Date: 2018/10/18
 * @Description:
 **/
@Service
public class JedisAdapter implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);

    private JedisPool pool;
    /*
    private static void print(int i, Object object) {
        System.out.println(String.format("%d,%s", i, object.toString()));
    }


    public static void main(String[] args) {
        Jedis jedis = new Jedis("redis://127.0.0.1:6379");

//        set get
        jedis.set("hello", "world");
        print(1, jedis.get("hello"));
        jedis.rename("hello", "hi");
        print(2, jedis.get("hi"));
        jedis.setex("hei", 15, "miaomiao");
        print(3, jedis.get("hei"));

//        数值增减
        jedis.set("pv", "100");
        jedis.incr("pv");
        print(4, jedis.get("pv"));
        jedis.incrBy("pv", 100);
        print(5, jedis.get("pv"));
        jedis.decr("pv");
        print(6, jedis.get("pv"));
        jedis.decrBy("pv", 100);
        print(7, jedis.get("pv"));

        print(8, jedis.keys("*"));

//        list
        String listName = "list";
        jedis.del(listName);
        for (int i = 0; i < 10; i++) {
            jedis.lpush(listName, "a" + String.valueOf(i));
        }
        print(9, jedis.lrange(listName, 0, 10));
        print(10, jedis.lpop(listName));
        print(11, jedis.llen(listName));
        print(12, jedis.lrange(listName, 2, 3));
        print(13, jedis.lindex(listName, 1));
        print(14, jedis.linsert(listName, BinaryClient.LIST_POSITION.BEFORE, "a2", "haha"));
        print(15, jedis.linsert(listName, BinaryClient.LIST_POSITION.AFTER, "a2", "hehe"));
        print(16, jedis.lrange(listName, 0, 12));

//        hash
        String userKey = "user";
        jedis.hset(userKey, "name", "miaomiao");
        jedis.hset(userKey, "age", "21");
        jedis.hset(userKey, "phone", "12318181818");
        print(17, jedis.hget(userKey, "name"));
        print(18, jedis.hgetAll(userKey));
        print(19, jedis.hdel(userKey, "phone"));
        print(20, jedis.hgetAll(userKey));
        print(21, jedis.hexists(userKey, "phone"));
        print(22, jedis.hkeys(userKey));
        print(23, jedis.hvals(userKey));
        print(24, jedis.hsetnx(userKey, "email", "2355"));
        print(25, jedis.hsetnx(userKey, "name", "miao"));
        print(26, jedis.hgetAll(userKey));

//        set
        String likeKey1 = "commentLike1";
        String likeKey2 = "commentLike2";
        for (int i = 0; i < 10; i++) {
            jedis.sadd(likeKey1, String.valueOf(i));
            jedis.sadd(likeKey2, String.valueOf(i * i));
        }
        print(27, jedis.smembers(likeKey1));
        print(28, jedis.smembers(likeKey2));
        print(29, jedis.sunion(likeKey1, likeKey2));
        print(30, jedis.sdiff(likeKey1, likeKey2));
        print(31, jedis.sinter(likeKey1, likeKey2));
        print(32, jedis.sismember(likeKey1, "12"));
        print(33, jedis.sismember(likeKey1, "2"));
        jedis.srem(likeKey1, "5");
        print(34, jedis.smembers(likeKey1));
        jedis.smove(likeKey2, likeKey1, "25");
        print(35, jedis.smembers(likeKey1));
        print(36, jedis.scard(likeKey1));

//        sorted set 默认从小到大排序
        String rankKey = "rankKey";
        jedis.zadd(rankKey, 36, "Jim");
        jedis.zadd(rankKey, 66, "Coco");
        jedis.zadd(rankKey, 96, "Jack");
        jedis.zadd(rankKey, 47, "Marry");
        jedis.zadd(rankKey, 83, "Ben");
        print(37, jedis.zcard(rankKey));
        print(38,jedis.zcount(rankKey,61,100));
        print(39,jedis.zscore(rankKey,"Jim"));
        jedis.zincrby(rankKey,2,"Jim");
        print(40,jedis.zscore(rankKey,"Jim"));
        print(41,jedis.zrange(rankKey,0,10));
        print(42,jedis.zrange(rankKey,0,2));
//        从大到小排序
        print(43,jedis.zrevrange(rankKey,0,2));
        for (Tuple tuple : jedis.zrangeByScoreWithScores(rankKey,60,100)){
            print(44,tuple.getElement() + " : " + String.valueOf(tuple.getScore()));
        }
//        查排名，根据分值
        print(45,jedis.zrank(rankKey,"Ben"));
        print(46,jedis.zrevrank(rankKey,"Ben"));

        String setKey = "zset";
        jedis.zadd(setKey,1,"a");
        jedis.zadd(setKey,1,"b");
        jedis.zadd(setKey,1,"c");
        jedis.zadd(setKey,1,"d");
        jedis.zadd(setKey,1,"e");
        jedis.zadd(setKey,1,"f");

        print(47,jedis.zlexcount(setKey,"-","+"));
        print(47,jedis.zlexcount(setKey,"(b","[d"));

        JedisPool pool = new JedisPool();
        for (int i = 0; i  < 100; i++){
            Jedis j = pool.getResource();
//            j.set("hi","hello");
            String value = j.get("hi");
            print(48,value);
//            需要关闭，否则会每次都开一个线程，默认有八个线程
            j.close();
        }

    }
    */


    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("redis://127.0.0.1:6379/10");
    }

    public long sadd (String key,String value){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sadd(key,value);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return 0;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    public long srem (String key,String value){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srem(key,value);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return 0;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }


    public boolean  sismember(String key,String value){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sismember(key,value);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return false;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    public long scard(String key){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return 0;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    //保存一个对象 将对象转化为JSON 串
    public void setObject(String key,Object obj){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.set(key, JSON.toJSONString(obj));
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    public <T> T getObject(String key, Class<T> clazz){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            String value = jedis.get(key);
            if(value != null){
                return JSON.parseObject(value,clazz);
            }

        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return null;
    }

    //消息队列
    public void lpush(String key,String value){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.lpush(key, value);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    //取出队列
    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }
}
