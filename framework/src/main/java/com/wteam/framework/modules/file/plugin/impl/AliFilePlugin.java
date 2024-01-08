package com.wteam.framework.modules.file.plugin.impl;

  import cn.hutool.json.JSONUtil;
  import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.ObjectMetadata;
import com.wteam.framework.common.enums.ResultCode;
import com.wteam.framework.common.exception.ServiceException;
import com.wteam.framework.modules.file.entity.enums.OssEnum;
  import com.wteam.framework.modules.file.plugin.FilePlugin;
  import com.wteam.framework.modules.system.entity.dos.Setting;
  import com.wteam.framework.modules.system.entity.dto.OssSetting;
  import com.wteam.framework.modules.system.entity.enums.SettingEnum;
  import com.wteam.framework.modules.system.service.SettingService;
  import lombok.extern.slf4j.Slf4j;
  import org.springframework.beans.factory.annotation.Autowired;

  import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * 阿里oss 文件操作
 *
 * @author Chopper
 */

@Slf4j
public class AliFilePlugin implements FilePlugin {

    private OssSetting ossSetting;

    private SettingService settingService;

    public AliFilePlugin(OssSetting ossSetting,SettingService settingService) {
        this.ossSetting = ossSetting;
        this.settingService=settingService;
    }




    /**
     * 获取配置前缀
     *
     * @return
     */
    private String getUrlPrefix() {

        Setting setting = settingService.get(SettingEnum.OSS_SETTING.name());
        OssSetting ossSetting = JSONUtil.toBean(setting.getSettingValue(), OssSetting.class);

        return "https://" + ossSetting.getBucketName() + "." + ossSetting.getEndPoint() + "/";
    }

    @Override
    public OssEnum pluginName() {
        return null;
    }

    @Override
    public String pathUpload(String filePath, String key) {

        Setting setting = settingService.get(SettingEnum.OSS_SETTING.name());
        OssSetting ossSetting = JSONUtil.toBean(setting.getSettingValue(), OssSetting.class);

        OSS ossClient = new OSSClientBuilder().build(
                ossSetting.getEndPoint(),
                ossSetting.getAccessKeyId(),
                ossSetting.getAccessKeySecret());
        try {
            ossClient.putObject(ossSetting.getBucketName(), key, new File(filePath));
        } catch (OSSException oe) {
            log.error("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            log.error("Error Message: " + oe.getErrorMessage());
            log.error("Error Code:       " + oe.getErrorCode());
            log.error("Request ID:      " + oe.getRequestId());
            log.error("Host ID:           " + oe.getHostId());
            throw new ServiceException(ResultCode.OSS_EXCEPTION_ERROR);
        } catch (ClientException ce) {
            log.error("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            log.error("Error Message: " + ce.getMessage());
            throw new ServiceException(ResultCode.OSS_EXCEPTION_ERROR);
        } finally {
            /*
             * Do not forget to shut down the client finally to release all allocated resources.
             */
            ossClient.shutdown();
        }
        ossClient.shutdown();
        return getUrlPrefix() + key;
    }

    @Override
    public String inputStreamUpload(InputStream inputStream, String key) {

        Setting setting = settingService.get(SettingEnum.OSS_SETTING.name());
        OssSetting ossSetting = JSONUtil.toBean(setting.getSettingValue(), OssSetting.class);

        OSS ossClient = new OSSClientBuilder().build(
                ossSetting.getEndPoint(),
                ossSetting.getAccessKeyId(),
                ossSetting.getAccessKeySecret());
        try {
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentType("image/jpg");
            ossClient.putObject(ossSetting.getBucketName(), key, inputStream, meta);
        } catch (OSSException oe) {
            log.error("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            log.error("Error Message: " + oe.getErrorMessage());
            log.error("Error Code:       " + oe.getErrorCode());
            log.error("Request ID:      " + oe.getRequestId());
            log.error("Host ID:           " + oe.getHostId());
            throw new ServiceException(ResultCode.OSS_EXCEPTION_ERROR);
        } catch (ClientException ce) {
            log.error("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            log.error("Error Message: " + ce.getMessage());
            throw new ServiceException(ResultCode.OSS_EXCEPTION_ERROR);
        } finally {
            /*
             * Do not forget to shut down the client finally to release all allocated resources.
             */
            ossClient.shutdown();
        }
        ossClient.shutdown();
        return getUrlPrefix() + key;
    }

    @Override
    public void deleteFile(List<String> key) {

        Setting setting = settingService.get(SettingEnum.OSS_SETTING.name());
        OssSetting ossSetting = JSONUtil.toBean(setting.getSettingValue(), OssSetting.class);

        OSS ossClient = new OSSClientBuilder().build(
                ossSetting.getEndPoint(),
                ossSetting.getAccessKeyId(),
                ossSetting.getAccessKeySecret());

        try {
            ossClient.deleteObjects(
                    new DeleteObjectsRequest(ossSetting.getBucketName()).withKeys(key));
        } catch (OSSException oe) {
            log.error("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            log.error("Error Message: " + oe.getErrorMessage());
            log.error("Error Code:       " + oe.getErrorCode());
            log.error("Request ID:      " + oe.getRequestId());
            log.error("Host ID:           " + oe.getHostId());
            throw new ServiceException(ResultCode.OSS_DELETE_ERROR);
        } catch (ClientException ce) {
            log.error("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            log.error("Error Message: " + ce.getMessage());
            throw new ServiceException(ResultCode.OSS_DELETE_ERROR);
        } finally {
            /*
             * Do not forget to shut down the client finally to release all allocated resources.
             */
            ossClient.shutdown();
        }
    }
}
