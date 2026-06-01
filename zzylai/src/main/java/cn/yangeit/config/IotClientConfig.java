package cn.yangeit.config;

import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.core.region.Region;
import com.huaweicloud.sdk.iotda.v5.IoTDAClient;
import com.huaweicloud.sdk.iotda.v5.region.IoTDARegion;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 创建IoTDA实例
 */
@Configuration
public class IotClientConfig {

    @Value("${huaweicloud.ak}")
    String ak;
    @Value("${huaweicloud.sk}")
    String sk;

    @Value("${huaweicloud.projectId}")
    String projectId;
    @Value("${huaweicloud.regionId}")
    String regionId;
    @Value("${huaweicloud.endpoint}")
    String endpoint;



    @Bean
    public IoTDAClient huaWeiIotInstance() {
        ICredential auth = new BasicCredentials()
                .withAk(ak)
                .withSk(sk)
                // 标准版/企业版需要使用衍生算法，基础版请删除配置"withDerivedPredicate"
               .withDerivedPredicate(BasicCredentials.DEFAULT_DERIVED_PREDICATE)
                .withProjectId(projectId);

        return IoTDAClient.newBuilder()
                .withCredential(auth)
                // 标准版/企业版：需自行创建Region对象，基础版：请使用IoTDARegion的region对象，如"withRegion(IoTDARegion.CN_NORTH_4)"
                .withRegion(new Region(regionId, endpoint))
                 //.withRegion(IoTDARegion.CN_NORTH_4)
                .build();
    }
}
