package com.joon.ibox_back_end.common;

/**
 * 通用响应工具类
 */
public class R<T> {

    private int code; // 状态码
    private String message; // 返回信息
    private T data; // 返回数据

    // 私有构造方法，禁止直接创建对象
    private R(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功响应（无数据）
     */
    public static <T> R<T> success() {
        return new R<>(200, "操作成功", null);
    }

    /**
     * 成功响应（带数据）
     */
    public static <T> R<T> success(T data) {
        return new R<>(200, "操作成功", data);
    }

    /**
     * 成功响应（自定义消息）
     */
    public static <T> R<T> success(String message, T data) {
        return new R<>(200, message, data);
    }

    /**
     * 失败响应（默认状态码和消息）
     */
    public static <T> R<T> error() {
        return new R<>(500, "操作失败", null);
    }

    /**
     * 失败响应（自定义消息）
     */
    public static <T> R<T> error(String message) {
        return new R<>(500, message, null);
    }

    /**
     * 失败响应（自定义状态码和消息）
     */
    public static <T> R<T> error(int code, String message) {
        return new R<>(code, message, null);
    }

    // Getter 和 Setter 方法
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "R{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}