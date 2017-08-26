package org.example.myhome.exceptions

class DeviceNotFound(deviceId: String) : RuntimeException("Device with $deviceId Not found")
