package com.wutsi.marketplace.manager.endpoint

import com.wutsi.marketplace.manager.`delegate`.UpdateProductEventDelegate
import com.wutsi.marketplace.manager.dto.UpdateProductEventRequest
import javax.validation.Valid
import kotlin.Unit
import org.springframework.web.bind.`annotation`.PostMapping
import org.springframework.web.bind.`annotation`.RequestBody
import org.springframework.web.bind.`annotation`.RestController

@RestController
public class UpdateProductEventController(
  public val `delegate`: UpdateProductEventDelegate,
) {
  @PostMapping("/v1/products/event")
  public fun invoke(@Valid @RequestBody request: UpdateProductEventRequest): Unit {
    delegate.invoke(request)
  }
}
