package br.com.jagi.aws_learn.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UrlResponse {

    private String url;
    private Long expirationTime;
}
