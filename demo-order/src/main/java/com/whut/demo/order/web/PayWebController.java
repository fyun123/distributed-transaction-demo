package com.whut.demo.order.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.whut.demo.common.constant.OrderConstant;
import com.whut.demo.common.constant.PayConstant;
import com.whut.demo.common.exception.NoStockException;
import com.whut.demo.order.entity.OmsOrderEntity;
import com.whut.demo.order.service.OmsOrderService;
import com.whut.demo.order.vo.*;
import org.bouncycastle.math.raw.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class PayWebController {

    @Autowired
    OmsOrderService orderService;

    @GetMapping("/toPay")
    public String toPay(String bizOrderId,Model model){
        OmsOrderEntity order = orderService.getOne(new QueryWrapper<OmsOrderEntity>().eq("biz_order_id", bizOrderId));
        model.addAttribute("order",order);
        return "pay";
    }

    @GetMapping("/toCancel")
    public String toCancel(String bizOrderId){
        orderService.closeOrder(bizOrderId);
        return "redirect:http://order.demo.com:6100/listOrder";
    }

    @GetMapping("/pay")
    public String pay(PayVo payVo, RedirectAttributes redirectAttributes){
        PayRespVo vo = orderService.pay(payVo);
        String msg;
        switch (vo.getCode()){
            case 0:
                msg = "支付成功";
                break;
            case 1:
                msg = "支付失败";
                break;
            case 2:
                msg = "订单【"+payVo.getBizOrderId()+"】已经支付，请勿重新支付";
                break;
            case 3:
                msg = "订单【"+payVo.getBizOrderId()+"】不存在";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + vo.getCode());
        }
        redirectAttributes.addFlashAttribute("msg",msg);
        if (vo.getCode() == 1){
            return "redirect:http://order.demo.com:6100/toPay?bizOrderId="+vo.getBizOrderId();
        }
        return "redirect:http://order.demo.com:6100/listOrder";
    }
}
