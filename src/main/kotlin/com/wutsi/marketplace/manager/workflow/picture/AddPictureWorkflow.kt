package com.wutsi.marketplace.manager.workflow.picture

import com.wutsi.marketplace.access.dto.CreatePictureRequest
import com.wutsi.marketplace.access.dto.Product
import com.wutsi.marketplace.access.dto.Store
import com.wutsi.marketplace.manager.dto.AddPictureRequest
import com.wutsi.marketplace.manager.dto.AddPictureResponse
import com.wutsi.marketplace.manager.workflow.product.AbstractProductWorkflow
import com.wutsi.membership.access.dto.Account
import com.wutsi.platform.core.stream.EventStream
import com.wutsi.workflow.WorkflowContext
import com.wutsi.workflow.rule.account.ProductShouldNotHaveTooManyPicturesRule
import org.springframework.stereotype.Service

@Service
class AddPictureWorkflow(
    eventStream: EventStream
) : AbstractProductWorkflow<AddPictureRequest, AddPictureResponse>(eventStream) {
    override fun getProductId(context: WorkflowContext<AddPictureRequest, AddPictureResponse>) =
        context.request?.productId

    override fun getAdditionalRules(account: Account, store: Store?, product: Product?) = listOf(
        product?.let { ProductShouldNotHaveTooManyPicturesRule(it, regulationEngine) }
    )

    override fun doExecute(context: WorkflowContext<AddPictureRequest, AddPictureResponse>) {
        val request = context.request!!
        val response = marketplaceAccessApi.createPicture(
            request = CreatePictureRequest(
                productId = request.productId,
                url = request.url
            )
        )
        context.response = AddPictureResponse(
            pictureId = response.pictureId
        )
    }
}
