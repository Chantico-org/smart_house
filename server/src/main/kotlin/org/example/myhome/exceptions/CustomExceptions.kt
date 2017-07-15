package org.example.myhome.exceptions

class UnsupportedFirmwareVersion(override val message: String = ""): RuntimeException()

class DeviceChannelClosed(override val message: String = "") : RuntimeException()

class DeviceNotFound(deviceId: String) : RuntimeException("Device with $deviceId Not found")
