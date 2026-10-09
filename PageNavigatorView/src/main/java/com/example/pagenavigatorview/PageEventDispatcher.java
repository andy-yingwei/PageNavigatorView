package com.example.pagenavigatorview;

/**
 * 分页事件分发器
 * 区分「翻页（上一页/下一页）」和「跳转」两种来源
 */
public interface PageEventDispatcher {

    /**
     * 上一页 / 下一页 点击触发
     * @param page 新当前页（已校验）
     */
    void onPageSelected(int page);

    /**
     * 跳转页码确认触发
     * @param page 目标页码（已校验）
     */
    void onJumpRequested(int page);

    /**
     * 跳转时的校验失败提示
     * @param message 提示信息
     */
    void onJumpError(String message);
}
