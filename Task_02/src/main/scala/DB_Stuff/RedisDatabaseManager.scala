package DB_Stuff

import JsonDefinitions.{Article, Author}
import redis.clients.jedis.args.FlushMode
import redis.clients.jedis.{Jedis, JedisPool}

object RedisDatabaseManager {
    val DB_HOST = "localhost";
    val DB_PORT = 32321;

    val jedisConnectionPool: JedisPool = new JedisPool(DB_HOST, DB_PORT);
    val jedisInstance: Jedis = jedisConnectionPool.getResource;

    jedisInstance.flushAll(FlushMode.SYNC);

    def closeConnection(): Unit = {
        jedisConnectionPool.close();
    }

}
