package ru.cbrrate.model;

public enum RateType {
    CBR("cbr");

    final String serviceName;

    RateType(String serviceName) {this.serviceName = serviceName;}

    public String getServiceName() {
        return serviceName;
    }
}
