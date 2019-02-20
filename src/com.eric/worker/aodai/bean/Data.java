package com.eric.worker.aodai.bean;

public class Data {

    private Product product;
    private Integer demand;
    private Boolean unlimit;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getDemand() {
        return demand;
    }

    public void setDemand(Integer demand) {
        this.demand = demand;
    }

    public Boolean getUnlimit() {
        return unlimit;
    }

    public void setUnlimit(Boolean unlimit) {
        this.unlimit = unlimit;
    }
}
