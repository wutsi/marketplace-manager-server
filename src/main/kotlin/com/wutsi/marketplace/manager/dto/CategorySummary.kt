package com.wutsi.marketplace.manager.dto

import kotlin.Int
import kotlin.Long
import kotlin.String

public data class CategorySummary(
  public val id: Long = 0,
  public val title: String = "",
  public val longTitle: String = "",
  public val level: Int = 0,
  public val parentId: Long? = null,
)
