package org.example.myhome.dao

import org.springframework.data.repository.CrudRepository
import org.example.myhome.models.DeviceKeyEntity

interface DataKeyDao: CrudRepository<DeviceKeyEntity, String>
