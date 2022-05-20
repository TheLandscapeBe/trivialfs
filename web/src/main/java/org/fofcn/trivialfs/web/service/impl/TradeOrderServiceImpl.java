package org.fofcn.trivialfs.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.fofcn.trivialfs.web.entity.TradeOrder;
import org.fofcn.trivialfs.web.mapper.TradeOrderMapper;
import org.fofcn.trivialfs.web.service.TradeOrderService;
import org.springframework.stereotype.Service;

/**
 * trade order service implementation
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/24 14:31
 */
@Service
public class TradeOrderServiceImpl extends ServiceImpl<TradeOrderMapper, TradeOrder> implements TradeOrderService {
}
