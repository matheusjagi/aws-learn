package br.com.jagi.aws_learn.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductEvent {

    private Long productId;
    private String code;
    private String username;
}
