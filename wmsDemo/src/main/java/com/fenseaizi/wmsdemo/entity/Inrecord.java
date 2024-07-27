package com.fenseaizi.wmsdemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author fenseaizi
 * @since 2024-02-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Inrecord对象", description="")
public class Inrecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "货品")
    private Integer goods;

    @ApiModelProperty(value = "入库用户")
    private Integer user;

    @ApiModelProperty(value = "操作管理员")
    private Integer admin;

    @ApiModelProperty(value = "入库数量")
    private Integer count;

    @ApiModelProperty(value = "申请时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "状态，0为待审核，1为通过，2为拒绝")
    private Integer status;

    @ApiModelProperty(value = "操作时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "备注")
    private String remark;
    @TableField(exist = false)
    private Goods good;

}
