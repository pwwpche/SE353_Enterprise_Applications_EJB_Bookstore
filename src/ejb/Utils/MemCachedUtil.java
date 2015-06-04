package ejb.Utils;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;

import javax.ejb.Stateless;

/**
 * Created by pwwpche on 2015/5/3.
 * Run MemCached Service before deploying
 * Installing: D:/setup/MemCached/memcached.exe -d install
 * Start: memcached.exe -m 1024 -p 11211(default:11211) -d start
 * Testing:
 *   telnet 127.0.0.1 11211
 *   set a 0 0 1
 *   a
 *   If receive "STORED", then testing succeed.
 */
@Stateless(name = "MamCachedUtil")
public class MemCachedUtil {
    //MemCached utilities
    MemCachedClient client;
    SockIOPool pool;
    boolean initialized = false;

    private void getClient() {

        String[] addr = { "127.0.0.1:11211" ,"127.0.0.1:11212" };
        Integer[] weights = { 1, 1};
        pool = SockIOPool.getInstance("Cluster");
        pool.setServers(addr);
        pool.setWeights(weights);
        pool.setInitConn(5);
        pool.setMinConn(5);
        pool.setMaxConn(200);
        pool.setMaxIdle(1000 * 30 * 30);
        pool.setMaintSleep(30);
        pool.setNagle(false);
        pool.setSocketTO(30);
        pool.setSocketConnectTO(0);
        pool.initialize();
        initialized = true;
        client = new MemCachedClient("Cluster");
    }

    public Object getObject(String key){
        if(!initialized){
            getClient();
        }
        return client.get(key);
    }

    public void setObject(String key, Object object){
        if(!initialized){
            getClient();
        }
        client.set(key, object);
    }

    public void removeObject(String key){
        if(!initialized){
            getClient();
        }
        if(client.get(key) != null) {
            client.delete(key);
        }
    }
}
