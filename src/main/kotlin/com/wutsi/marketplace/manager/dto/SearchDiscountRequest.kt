package com.wutsi.marketplace.manager.dto

import java.time.LocalDate
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import org.springframework.format.`annotation`.DateTimeFormat

public data class SearchDiscountRequest(
  public val storeId: Long? = null,
  public val productIds: List<Long> = emptyList(),
  public val discountIds: List<Long> = emptyList(),
  public val type: String? = null,
  @get:DateTimeFormat(pattern="yyyy-MM-dd")
  public val date: LocalDate? = null,
  public val limit: Int = 100,
  public val offset: Int = 0,
)
