package DB_Stuff

import redis.clients.jedis.args.FlushMode
import redis.clients.jedis.{Jedis, JedisPool, Pipeline}

object RedisDatabaseManagerWriteMode {
    final val DB_HOST = "localhost";
    final val DB_PORT = 32321;

    private val jedisConnectionPool: JedisPool = new JedisPool(DB_HOST, DB_PORT);
    private val jedisInstance: Jedis = jedisConnectionPool.getResource;

    jedisInstance.flushAll(FlushMode.SYNC);

    val jedisPipeline: Pipeline = jedisInstance.pipelined();

    def syncPipelineAndCloseConnection(): Unit = {
        jedisPipeline.sync();
        jedisConnectionPool.close();
    }

}
