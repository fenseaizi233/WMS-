package com.fenseaizi.wmsdemo.common;

import lombok.Data;

@Data
public class Result {

    private int code;
    private String msg;
    private Long total;
    private Object data;

    /**
     * 创建一个表示失败的Result对象
     *
     * @return Result对象
     */
    public static Result fail() {
        return result(400, "失败", 0L, null);
    }

    /**
     * 创建一个表示失败的Result对象，并指定消息
     *
     * @param msg 失败消息
     * @return Result对象
     */
    public static Result fail(String msg) {
        return result(400, msg, 0L, null);
    }

    /**
     * 创建一个表示成功的Result对象
     *
     * @return Result对象
     */
    public static Result suc() {
        return result(200, "成功", 0L, null);
    }

    /**
     * 创建一个表示成功的Result对象，并指定数据
     *
     * @param data 成功返回的数据
     * @return Result对象
     */
    public static Result suc(Object data) {
        return result(200, "成功", 0L, data);
    }

    /**
     * 创建一个表示成功的Result对象，并指定数据和总记录数
     *
     * @param data  成功返回的数据
     * @param total 总记录数
     * @return Result对象
     */
    public static Result suc(Object data, Long total) {
        return result(200, "成功", total, data);
    }
    public static Result suc(Object data, String msg) {
        return result(200, msg, 0L, data);
    }

    /**
     * 根据指定参数创建Result对象
     *
     * @param code  编码
     * @param msg   消息
     * @param total 总记录数
     * @param data  数据
     * @return Result对象
     */
    private static Result result(int code, String msg, Long total, Object data) {
        Result res = new Result();
        res.setData(data);
        res.setMsg(msg);
        res.setCode(code);
        res.setTotal(total);
        return res;
    }

}