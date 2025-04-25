package com.algaworks.algasensors.device.management.api.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class SensorInput {

    @NonNull
    private String name;
    @NonNull
    private String location;
    @NonNull
    private String ip;
    @NonNull
    private String protocol;
    @NonNull
    private String model;
}
