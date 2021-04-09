package com.whut.demo.ware.controller;

import java.util.Arrays;
import java.util.Map;

import com.whut.demo.ware.entity.WmsOrderTaskDetailEntity;
import com.whut.demo.ware.service.WmsOrderTaskDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.whut.demo.common.utils.PageUtils;
import com.whut.demo.common.utils.R;



/**
 * 订单任务细节
 *
 * @author ä¸å¤§å²
 * @email 1184384017@qq.com
 * @date 2021-04-07 15:27:20
 */
@RestController
@RequestMapping("ware/wmsordertaskdetail")
public class WmsOrderTaskDetailController {
    @Autowired
    private WmsOrderTaskDetailService wmsOrderTaskDetailService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wmsOrderTaskDetailService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		WmsOrderTaskDetailEntity wmsOrderTaskDetail = wmsOrderTaskDetailService.getById(id);

        return R.ok().put("wmsOrderTaskDetail", wmsOrderTaskDetail);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody WmsOrderTaskDetailEntity wmsOrderTaskDetail){
		wmsOrderTaskDetailService.save(wmsOrderTaskDetail);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody WmsOrderTaskDetailEntity wmsOrderTaskDetail){
		wmsOrderTaskDetailService.updateById(wmsOrderTaskDetail);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		wmsOrderTaskDetailService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
