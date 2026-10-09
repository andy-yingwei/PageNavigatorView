package com.example.mypagenavigatorview;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.example.pagenavigatorview.*;

/**
 * PageNavigatorView 测试用 Activity
 *
 * 测试覆盖：
 * 1. 上一页 / 下一页翻页 → onPageSelected
 * 2. 弹窗跳转 → onJumpRequested
 * 3. 边界状态（第一页 / 最后一页按钮自动 disable）
 * 4. 动态设置 maxPage
 * 5. 外部调用 setCurrentPage
 */
public class TestPagerActivity extends AppCompatActivity {

    private PageNavigatorView navigator;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_pager);

        // ===== 绑定 View =====
        navigator = findViewById(R.id.pageNavigator);
        tvResult = findViewById(R.id.tvResult);

        // ===== 设置回调 =====
        navigator.enableLog("Test");
        navigator.setDispatcher(new PageEventDispatcher() {

            @Override
            public void onPageSelected(int page) {
                // 上一页 / 下一页触发
                String msg = "onPageSelected → 第 " + page + " 页";
                tvResult.setText(msg);
                Toast.makeText(TestPagerActivity.this, msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onJumpRequested(int page) {
                // 弹窗跳转触发
                String msg = "onJumpRequested → 跳转到第 " + page + " 页";
                tvResult.setText(msg);
                Toast.makeText(TestPagerActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
             @Override
            public void onJumpError(String message){
                 Log.d("Test", "onJumpError: " + message);
             }
        });

        // ===== 初始设置：总页数 30 =====
        navigator.setMinPage(1);
        navigator.setMaxPage(3);
        navigator.setCurrentPage(1);

    }
}
