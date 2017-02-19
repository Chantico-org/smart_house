package org.example.myhome.exceptions

class UnsupportedFirmwareVersion(override val message: String?): RuntimeException()

class DeviceChannelClosed(override val message: String?) : RuntimeException()

class DeviceNotFound(override val message: String?) : RuntimeException()
