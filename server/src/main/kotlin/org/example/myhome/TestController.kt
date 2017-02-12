package org.example.myhome

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 */
@RestController
open class TestController {
  @GetMapping("/")
  open fun getIt() = "Test" to 2
}
