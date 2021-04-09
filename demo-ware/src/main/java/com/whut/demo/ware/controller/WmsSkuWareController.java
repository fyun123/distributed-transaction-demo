package com.whut.demo.ware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.whut.demo.common.exception.BizCodeEnume;
import com.whut.demo.common.exception.NoStockException;
import com.whut.demo.ware.entity.WmsSkuWareEntity;
import com.whut.demo.ware.service.WmsSkuWareService;
import com.whut.demo.ware.vo.WareSkuLockVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.whut.demo.common.utils.PageUtils;
import com.whut.demo.common.utils.R;



/**
 * 商品库存
 *
 * @author ä¸å¤§å²
 * @email 1184384017@qq.com
 * @date 2021-04-07 15:27:20
 */
@RestController
@RequestMapping("ware/wmsskuware")
public class WmsSkuWareController {

    @Autowired
    private WmsSkuWareService wmsSkuWareService;


    @PostMapping("/lock/order")
    public R orderLockStock(@RequestBody WareSkuLockVo vo){
        try{
            wmsSkuWareService.orderLockStock(vo);
            return R.ok();
        }catch (NoStockException e){
            return R.error(BizCodeEnume.NO_STOCK_EXCEPTION.getCode(),BizCodeEnume.NO_STOCK_EXCEPTION.getMsg()).put("skuIds",e.getSkuIds());
        }
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wmsSkuWareService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		WmsSkuWareEntity wmsSkuWare = wmsSkuWareService.getById(id);

        return R.ok().put("wmsSkuWare", wmsSkuWare);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody WmsSkuWareEntity wmsSkuWare){
		wmsSkuWareService.save(wmsSkuWare);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody WmsSkuWareEntity wmsSkuWare){
		wmsSkuWareService.updateById(wmsSkuWare);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		wmsSkuWareService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
