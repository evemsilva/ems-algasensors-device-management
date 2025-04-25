package com.algaworks.algasensors.device.management.api.model;

import io.hypersistence.tsid.TSID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorOutput {

    private TSID id;
    private String name;
    private String location;
    private String ip;
    private String protocol;
    private String model;
    private Boolean enabled;
}
