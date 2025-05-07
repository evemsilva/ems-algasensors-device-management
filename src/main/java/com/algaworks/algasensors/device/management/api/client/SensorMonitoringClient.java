package com.algaworks.algasensors.device.management.api.client;

import io.hypersistence.tsid.TSID;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PutExchange;

@HttpExchange("/api/sensors/{sensorId}/monitoring/enable")
public interface SensorMonitoringClient {

    @PutExchange
    void enableMonitoring(@PathVariable TSID sensorId);

    @DeleteExchange
    void disableMonitoring(@PathVariable TSID sensorId);

}
