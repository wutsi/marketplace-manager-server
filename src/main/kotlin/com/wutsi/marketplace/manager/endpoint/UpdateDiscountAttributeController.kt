package com.wutsi.marketplace.manager.endpoint

import com.wutsi.marketplace.manager.`delegate`.UpdateDiscountAttributeDelegate
import com.wutsi.marketplace.manager.dto.UpdateDiscountAttributeRequest
import javax.validation.Valid
import kotlin.Long
import kotlin.Unit
import org.springframework.web.bind.`annotation`.PathVariable
import org.springframework.web.bind.`annotation`.PostMapping
import org.springframework.web.bind.`annotation`.RequestBody
import org.springframework.web.bind.`annotation`.RestController

@RestController
public class UpdateDiscountAttributeController(
  public val `delegate`: UpdateDiscountAttributeDelegate,
) {
  @PostMapping("/v1/discounts/{id}/attributes")
  public fun invoke(@PathVariable(name="id") id: Long, @Valid @RequestBody
      request: UpdateDiscountAttributeRequest): Unit {
    delegate.invoke(id, request)
  }
}
