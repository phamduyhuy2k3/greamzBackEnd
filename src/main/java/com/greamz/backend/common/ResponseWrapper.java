package com.greamz.backend.common;

import java.time.LocalDateTime;

public class ResponseWrapper<T> {
    private String status;
    private LocalDateTime timestamp;
    private T data;

    public ResponseWrapper(String status, LocalDateTime timestamp, T data) {
        this.status = status;
        this.timestamp = timestamp;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
