package DB_Stuff

import redis.clients.jedis.{Jedis, JedisPool}

object RedisDatabaseManagerReadMode {
    final val DB_HOST = "localhost";
    final val DB_PORT = 32321;

    private val jedisConnectionPool: JedisPool = new JedisPool(DB_HOST, DB_PORT);
    val jedisInstance: Jedis = jedisConnectionPool.getResource;

    def closeConnection(): Unit = {
        jedisConnectionPool.close();
    }

}
