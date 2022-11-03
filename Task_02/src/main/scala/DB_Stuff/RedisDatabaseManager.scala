package DB_Stuff

import redis.clients.jedis.args.FlushMode
import redis.clients.jedis.{Jedis, JedisPool, Pipeline}

object RedisDatabaseManager {
    val DB_HOST = "localhost";
    val DB_PORT = 32321;
    val jedisPipeline: Pipeline = jedisInstance.pipelined();
    private val jedisConnectionPool: JedisPool = new JedisPool(DB_HOST, DB_PORT);

    jedisInstance.flushAll(FlushMode.SYNC);
    private val jedisInstance: Jedis = jedisConnectionPool.getResource;

    def syncPipelineAndCloseConnection(): Unit = {
        jedisPipeline.sync();
        jedisConnectionPool.close();
    }

}
