package jp.co.plus.core.dto;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@Scope(value= "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String secret;

}
