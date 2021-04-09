package com.whut.demo.order.web;

import com.whut.demo.common.exception.NoStockException;
import com.whut.demo.order.entity.OmsOrderEntity;
import com.whut.demo.order.service.OmsOrderService;
import com.whut.demo.order.vo.OrderConfirmVo;
import com.whut.demo.order.vo.OrderSubmitVo;
import com.whut.demo.order.vo.SubmitOrderResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class OrderWebController {

    @Autowired
    OmsOrderService orderService;

    @GetMapping("/confirmOrder")
    public String confirmOrder(Model model){
        OrderConfirmVo orderConfirmVo = orderService.confirmOrder();
        model.addAttribute("orderConfirmData", orderConfirmVo);
        return "order";
    }

    @PostMapping("/submitOrder")
    public String submitOrder(Model model, RedirectAttributes redirectAttributes, OrderSubmitVo submitVo){
        try {
            SubmitOrderResponseVo responseVo = orderService.submitOrder(submitVo);
            if (responseVo.getCode() == 0) {
                model.addAttribute("submitOrderResp", responseVo);
                return "pay";
            } else {
                String msg = "下单失败：";
                if (responseVo.getCode() == 1) {
                    msg += "订单信息过期，请刷新重试";
                }
                redirectAttributes.addFlashAttribute("msg", msg);
                return "redirect:http://order.demo.com:6100/confirmOrder";
            }
        }catch (NoStockException e){
            redirectAttributes.addFlashAttribute("msg", "商品库存不足，请重新下单");
            redirectAttributes.addFlashAttribute("failSkuIds", e.getSkuIds());
            return "redirect:http://order.demo.com:6100/confirmOrder";
        }
    }

    @GetMapping("/listOrder")
    public String listOrder(Model model){
        List<OmsOrderEntity> orders = orderService.listOrder();
        model.addAttribute("orders",orders);
        return "listOrder";
    }

}
