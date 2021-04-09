package com.whut.demo.ware.service.impl;

import com.whut.demo.ware.dao.WmsOrderTaskDetailDao;
import com.whut.demo.ware.entity.WmsOrderTaskDetailEntity;
import com.whut.demo.ware.service.WmsOrderTaskDetailService;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whut.demo.common.utils.PageUtils;
import com.whut.demo.common.utils.Query;


@Service("wmsOrderTaskDetailService")
public class WmsOrderTaskDetailServiceImpl extends ServiceImpl<WmsOrderTaskDetailDao, WmsOrderTaskDetailEntity> implements WmsOrderTaskDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WmsOrderTaskDetailEntity> page = this.page(
                new Query<WmsOrderTaskDetailEntity>().getPage(params),
                new QueryWrapper<WmsOrderTaskDetailEntity>()
        );

        return new PageUtils(page);
    }

}