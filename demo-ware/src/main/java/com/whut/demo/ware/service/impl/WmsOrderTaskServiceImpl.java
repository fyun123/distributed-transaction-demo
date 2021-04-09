package com.whut.demo.ware.service.impl;

import com.whut.demo.ware.dao.WmsOrderTaskDao;
import com.whut.demo.ware.entity.WmsOrderTaskEntity;
import com.whut.demo.ware.service.WmsOrderTaskService;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whut.demo.common.utils.PageUtils;
import com.whut.demo.common.utils.Query;


@Service("wmsOrderTaskService")
public class WmsOrderTaskServiceImpl extends ServiceImpl<WmsOrderTaskDao, WmsOrderTaskEntity> implements WmsOrderTaskService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WmsOrderTaskEntity> page = this.page(
                new Query<WmsOrderTaskEntity>().getPage(params),
                new QueryWrapper<WmsOrderTaskEntity>()
        );

        return new PageUtils(page);
    }

}