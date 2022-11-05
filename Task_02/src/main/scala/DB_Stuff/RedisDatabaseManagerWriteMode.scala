package DB_Stuff

import redis.clients.jedis.args.FlushMode
import redis.clients.jedis.{Jedis, JedisPool, JedisPoolConfig, Pipeline}

object RedisDatabaseManagerWriteMode {
    final val DB_HOST = "localhost";
    final val DB_PORT = 32321;
    final val JEDIS_TIMEOUT = 20 * 1000;

    final val PIPELINE_SYNC_LINE_FREQUENCY = 200_000;

    private val jedisPoolConfig: JedisPoolConfig = new JedisPoolConfig();
    private val jedisConnectionPool: JedisPool = new JedisPool(jedisPoolConfig, DB_HOST, DB_PORT, JEDIS_TIMEOUT);
    private val jedisInstance: Jedis = jedisConnectionPool.getResource;

    jedisInstance.flushAll(FlushMode.SYNC);

    val jedisPipeline: Pipeline = jedisInstance.pipelined();

    def syncPipelineAndCloseConnection(): Unit = {
        jedisPipeline.sync();
        jedisConnectionPool.close();
    }

}
