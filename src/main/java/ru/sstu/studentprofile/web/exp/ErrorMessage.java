package ru.sstu.studentprofile.web.exp;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ErrorMessage(@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss") LocalDateTime timestamp,
                           int status,
                           String error,
                           String message,
                           String path) {
    @Override
    public String toString() {
        return "{" +
                "timestamp=" + timestamp.toString() +
                ", status=" + status +
                ", error='" + error + '\'' +
                ", message='" + message + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
