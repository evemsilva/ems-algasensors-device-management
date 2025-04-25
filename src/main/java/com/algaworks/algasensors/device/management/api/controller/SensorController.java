package com.algaworks.algasensors.device.management.api.controller;

import com.algaworks.algasensors.device.management.api.model.SensorInput;
import com.algaworks.algasensors.device.management.api.model.SensorOutput;
import com.algaworks.algasensors.device.management.common.IdGenerator;
import com.algaworks.algasensors.device.management.domain.model.Sensor;
import com.algaworks.algasensors.device.management.domain.model.SensorId;
import com.algaworks.algasensors.device.management.domain.repository.SensorRepository;
import io.hypersistence.tsid.TSID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@AllArgsConstructor
@RequestMapping("/api/sensors")
@RestController
public class SensorController {

    private final SensorRepository sensorRepository;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<SensorOutput> search(@PageableDefault Pageable page) {
        Page<Sensor> sensors = sensorRepository.findAll(page);
        return sensors.map(this::convertToSensorOutput);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{sensorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SensorOutput getOne(@PathVariable TSID sensorId) {
        Sensor sensor = findSensorById(sensorId);
        return convertToSensorOutput(sensor);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SensorOutput create(@RequestBody SensorInput input) {
        Sensor sensor = Sensor.builder()
                                .id(new SensorId(IdGenerator.generateTSID()))
                                .name(input.getName())
                                .location(input.getLocation())
                                .ip(input.getIp())
                                .protocol(input.getProtocol())
                                .model(input.getModel())
                                .enabled(false)
                                .build();

        sensorRepository.saveAndFlush(sensor);
        return convertToSensorOutput(sensor);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/{sensorId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SensorOutput update(@PathVariable TSID sensorId, @RequestBody SensorInput input) {
        Sensor sensorToUpdate = findSensorById(sensorId);
        sensorToUpdate.setName(input.getName());
        sensorToUpdate.setLocation(input.getLocation());
        sensorToUpdate.setIp(input.getIp());
        sensorToUpdate.setProtocol(input.getProtocol());
        sensorToUpdate.setModel(input.getModel());
        sensorRepository.saveAndFlush(sensorToUpdate);
        return convertToSensorOutput(sensorToUpdate);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{sensorId}")
    public void remove(@PathVariable TSID sensorId) {
        Sensor sensorToDelete = findSensorById(sensorId);
        sensorRepository.delete(sensorToDelete);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{sensorId}/enable")
    public void enable(@PathVariable TSID sensorId) {
        Sensor sensorToEnable = findSensorById(sensorId);
        sensorToEnable.setEnabled(true);
        sensorRepository.saveAndFlush(sensorToEnable);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{sensorId}/enable")
    public void disable(@PathVariable TSID sensorId) {
        Sensor sensorToDisable = findSensorById(sensorId);
        sensorToDisable.setEnabled(false);
        sensorRepository.saveAndFlush(sensorToDisable);
    }

    private Sensor findSensorById(TSID sensorId) {
        return sensorRepository.findById(new SensorId(sensorId)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private SensorOutput convertToSensorOutput(Sensor sensor) {
        return SensorOutput.builder()
                            .id(sensor.getId().getValue())
                            .name(sensor.getName())
                            .location(sensor.getLocation())
                            .ip(sensor.getIp())
                            .protocol(sensor.getProtocol())
                            .model(sensor.getModel())
                            .enabled(sensor.getEnabled())
                            .build();
    }
}
