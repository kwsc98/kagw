package pers.kagw.core.dto;


import lombok.Data;

import java.io.Serializable;

/**
 * @author kwsc98
 */
@Data
public class RouteNodeDTO implements Serializable {

    private String addres;

    private double weight = 1;

    public RouteNodeDTO setAddres(String addres) {
        this.addres = addres;
        return this;
    }

    public RouteNodeDTO setWeight(double weight) {
        this.weight = weight;
        return this;
    }
}
