package DB_Stuff

import redis.clients.jedis.{Jedis, JedisPool, JedisPoolConfig}

object RedisDatabaseManagerReadMode {
    final val DB_HOST = "localhost";
    final val DB_PORT = 6379;
    final val JEDIS_TIMEOUT = 60 * 1000;

    private val jedisPoolConfig: JedisPoolConfig = new JedisPoolConfig();
    private val jedisConnectionPool: JedisPool = new JedisPool(jedisPoolConfig, DB_HOST, DB_PORT, JEDIS_TIMEOUT);
    val jedisInstance: Jedis = jedisConnectionPool.getResource;

    def closeConnection(): Unit = {
        jedisConnectionPool.close();
    }

}
