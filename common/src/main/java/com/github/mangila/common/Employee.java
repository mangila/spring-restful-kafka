package com.github.mangila.common;

import lombok.*;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Employee implements Serializable {
    private String id;
    private String name;
    private Instant updated;
}
