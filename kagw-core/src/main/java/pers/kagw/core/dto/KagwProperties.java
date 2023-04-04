package pers.kagw.core.dto;

import lombok.Data;

@Data
public class KagwProperties {

    private String registeredPath;

    private int port = 8080;

}