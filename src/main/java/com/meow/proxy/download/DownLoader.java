package com.meow.proxy.download;

import com.meow.proxy.entity.Task;

import java.util.List;

/**
 * 下载接口
 * @author Alex
 *         date:2017/12/15
 *         email:jwnie@foxmail.com
 */
public interface DownLoader {

    /**
     * 包括翻页下载，返回List<String>
     * @return
     */
    public List<String> downLoad(Task task);

}
