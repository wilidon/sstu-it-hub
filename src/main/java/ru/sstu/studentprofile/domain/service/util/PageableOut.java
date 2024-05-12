package ru.sstu.studentprofile.domain.service.util;

import java.util.Collection;

public record PageableOut<T>(int page,
                             int size,
                             int totalPages,
                             Collection<T> content) {
}