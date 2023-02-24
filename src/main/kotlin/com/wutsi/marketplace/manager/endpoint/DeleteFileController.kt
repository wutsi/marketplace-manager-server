package com.wutsi.marketplace.manager.endpoint

import com.wutsi.marketplace.manager.`delegate`.DeleteFileDelegate
import org.springframework.web.bind.`annotation`.DeleteMapping
import org.springframework.web.bind.`annotation`.PathVariable
import org.springframework.web.bind.`annotation`.RestController
import kotlin.Long

@RestController
public class DeleteFileController(
    public val `delegate`: DeleteFileDelegate,
) {
    @DeleteMapping("/v1/files/{id}")
    public fun invoke(@PathVariable(name = "id") id: Long) {
        delegate.invoke(id)
    }
}
