package org.fofcn.trivialfs.web.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * trade order entity
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/24 14:28
 */
@Data
@TableName("trade_order")
public class TradeOrder {

    @TableId("id")
    private Long id;
}
