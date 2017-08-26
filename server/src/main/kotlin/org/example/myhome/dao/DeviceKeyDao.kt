package org.example.myhome.dao

import org.example.myhome.entity.DeviceKeyEntity
import org.springframework.data.repository.CrudRepository

interface DeviceKeyDao : CrudRepository<DeviceKeyEntity, String>
