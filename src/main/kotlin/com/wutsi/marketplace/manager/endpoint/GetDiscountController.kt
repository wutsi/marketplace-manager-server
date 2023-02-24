package com.wutsi.marketplace.manager.endpoint

import com.wutsi.marketplace.manager.`delegate`.GetDiscountDelegate
import com.wutsi.marketplace.manager.dto.GetDiscountResponse
import kotlin.Long
import org.springframework.web.bind.`annotation`.GetMapping
import org.springframework.web.bind.`annotation`.PathVariable
import org.springframework.web.bind.`annotation`.RestController

@RestController
public class GetDiscountController(
  public val `delegate`: GetDiscountDelegate,
) {
  @GetMapping("/v1/discounts/{id}")
  public fun invoke(@PathVariable(name="id") id: Long): GetDiscountResponse = delegate.invoke(id)
}
