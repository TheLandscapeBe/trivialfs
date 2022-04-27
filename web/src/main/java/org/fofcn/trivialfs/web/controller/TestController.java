package org.fofcn.trivialfs.web.controller;

import com.alibaba.fastjson.JSON;
import org.fofcn.trivialfs.web.entity.TradeOrder;
import org.fofcn.trivialfs.web.service.TradeOrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Test controller
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/24 14:17
 */
@RestController
public class TestController {

    @Resource
    private TradeOrderService tradeOrderService;

    @PutMapping("/sharding")
    public String sharding() {
        for (int i = 1; i <= 10; i++) {
            TradeOrder tradeOrder = new TradeOrder();
            tradeOrderService.save(tradeOrder);
        }

        return "ok";
    }

    @GetMapping("/sharding")
    public String getSharding() {
        System.out.println(JSON.toJSONString(tradeOrderService.list()));
        System.out.println(tradeOrderService.count());
        return "ok";
    }
}
