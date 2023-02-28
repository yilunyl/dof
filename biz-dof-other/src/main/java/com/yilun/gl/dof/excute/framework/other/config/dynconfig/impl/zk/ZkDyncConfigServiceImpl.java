package com.yilun.gl.dof.excute.framework.other.config.dynconfig.impl.zk;

import com.yilun.gl.dof.excute.framework.other.config.dynconfig.core.BasicDyncConfigService;
import com.yilun.gl.dof.excute.framework.other.config.dynconfig.core.entity.DyncConfigTypeEnum;
import com.yilun.gl.dof.excute.framework.other.config.dynconfig.impl.zk.entity.DyncZookeeperProperties;
import com.yilun.gl.dof.excute.framework.other.config.dynconfig.util.PropertyUtil;
import com.yilun.gl.dof.excute.framework.other.config.dynconfig.core.entity.RefreshConfgiEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.springframework.stereotype.Component;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: gl
 * @Date: 2020/10/22 17:59
 * @Description:
 */
@Component
@Slf4j
public class ZkDyncConfigServiceImpl extends BasicDyncConfigService {

    private CuratorFramework client;

    private ConcurrentHashMap<String, TreeCache> treeCacheMap = new ConcurrentHashMap<>(20);

    /**
     * 获取连接zk的客户端
     */
    @Override
    protected void createConfigClient() {
        if (Objects.isNull(client)) {
            DyncZookeeperProperties zookeeperProperties = PropertyUtil.instanceClassFromEnv(environment, DyncConfigTypeEnum.ZOOKEEPER_00.configAddressPrefix(), DyncZookeeperProperties.class);
            client = CuratorFrameworkFactory.builder()
                    .connectString(zookeeperProperties.getAddress())
                    .retryPolicy(new ExponentialBackoffRetry(1000, 10))
                    .build();
            client.start();
        }

    }

    /**
     * 初始化信息
     *
     * @param path 路径
     * @return 初始化信息
     */
    @Override
    protected byte[] initFirstData(String path) {
        try {
            return client.getData().forPath(path);
        } catch (Exception e) {
            log.error("节点{}的初始化数据发生异常:", path, e);
        }
        return null;
    }

    /**
     * 注册监听器信息
     *
     * @param path path
     */
    @Override
    protected void registerListener(String path) {
        try {
            if (!treeCacheMap.containsKey(path)) {
                TreeCache treeCache = TreeCache.newBuilder(client, path).setCacheData(false).build();
                treeCache.getListenable().addListener(new CuratorWatcherImpl());
                treeCache.start();
                treeCacheMap.put(path, treeCache);
            }
        } catch (Exception e) {
            log.error("节点{}注册监听器发生异常:", path, e);
        }
    }

    /**
     * 信息销毁
     */
    @Override
    public void doDestroy() {
        log.info("treeCaches:{} and zkClient:{} was destroying", treeCacheMap, client);
        if (CollectionUtils.isNotEmptyMap(treeCacheMap)) {
            treeCacheMap.forEach((key, val) -> {
                val.close();
            });
        }
        if (Objects.nonNull(client)) {
            this.client.close();
        }
    }

    /**
     * 获取配置中心类型
     *
     * @return
     */
    @Override
    public DyncConfigTypeEnum getDyncConfigType() {
        return DyncConfigTypeEnum.ZOOKEEPER_00;
    }

    /**
     * 监听器,该监听器，只需要注册一次，会一直监听
     */
    class CuratorWatcherImpl implements TreeCacheListener {

        @Override
        public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
            if (event.getType().equals(TreeCacheEvent.Type.NODE_UPDATED)) {
                RefreshConfgiEntity data = RefreshConfgiEntity.builder()
                        .data(event.getData().getData())
                        .wholePath(event.getData().getPath())
                        .build();
                refreshProperties(data);
                log.info("TreeCacheListenerUpdate path:{}, data:{}", event.getData().getPath(), new String(event.getData().getData()));
            }
        }
    }
}
