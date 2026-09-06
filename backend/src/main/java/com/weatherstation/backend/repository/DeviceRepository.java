package com.weatherstation.backend.repository;

import com.weatherstation.backend.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device,Long> {

    Device findByDeviceId(String deviceId);
}
