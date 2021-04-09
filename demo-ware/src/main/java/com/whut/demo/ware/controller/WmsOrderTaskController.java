package com.whut.demo.ware.controller;

import java.util.Arrays;
import java.util.Map;

import com.whut.demo.ware.entity.WmsOrderTaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.whut.demo.ware.service.WmsOrderTaskService;
import com.whut.demo.common.utils.PageUtils;
import com.whut.demo.common.utils.R;



/**
 * 订单任务
 *
 * @author ä¸å¤§å²
 * @email 1184384017@qq.com
 * @date 2021-04-07 15:27:20
 */
@RestController
@RequestMapping("ware/wmsordertask")
public class WmsOrderTaskController {
    @Autowired
    private WmsOrderTaskService wmsOrderTaskService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wmsOrderTaskService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		WmsOrderTaskEntity wmsOrderTask = wmsOrderTaskService.getById(id);

        return R.ok().put("wmsOrderTask", wmsOrderTask);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody WmsOrderTaskEntity wmsOrderTask){
		wmsOrderTaskService.save(wmsOrderTask);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody WmsOrderTaskEntity wmsOrderTask){
		wmsOrderTaskService.updateById(wmsOrderTask);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		wmsOrderTaskService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
