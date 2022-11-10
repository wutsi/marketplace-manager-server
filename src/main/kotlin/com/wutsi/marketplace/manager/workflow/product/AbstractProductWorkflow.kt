package com.wutsi.marketplace.manager.workflow.product

import com.wutsi.marketplace.access.dto.Product
import com.wutsi.marketplace.access.dto.Store
import com.wutsi.marketplace.manager.event.ProductEventPayload
import com.wutsi.marketplace.manager.workflow.AbstractMarketplaceWorkflow
import com.wutsi.membership.access.dto.Account
import com.wutsi.platform.core.stream.EventStream
import com.wutsi.workflow.Rule
import com.wutsi.workflow.RuleSet
import com.wutsi.workflow.WorkflowContext
import com.wutsi.workflow.rule.account.AccountShouldBeActiveRule
import com.wutsi.workflow.rule.account.AccountShouldBeBusinessRule
import com.wutsi.workflow.rule.account.AccountShouldBeOwnerOfProductRule
import com.wutsi.workflow.rule.account.AccountShouldBeOwnerOfStoreRule
import com.wutsi.workflow.rule.account.AccountShouldHaveStoreRule
import com.wutsi.workflow.rule.account.StoreShouldBeActiveRule

abstract class AbstractProductWorkflow<Req, Resp>(eventStream: EventStream) :
    AbstractMarketplaceWorkflow<Req, Resp, ProductEventPayload>(eventStream) {
    override fun getEventType(): String? = null
    override fun toEventPayload(context: WorkflowContext<Req, Resp>): ProductEventPayload? = null
    protected abstract fun getProductId(context: WorkflowContext<Req, Resp>): Long?

    override fun getValidationRules(context: WorkflowContext<Req, Resp>): RuleSet {
        val account = getCurrentAccount(context)
        val store = account.storeId?.let {
            getCurrentStore(account)
        }
        val product = getProduct(context)

        val rules = mutableListOf(
            AccountShouldBeBusinessRule(account),
            AccountShouldBeActiveRule(account),
            AccountShouldHaveStoreRule(account),
            store?.let { AccountShouldBeOwnerOfStoreRule(account, it) },
            store?.let { StoreShouldBeActiveRule(it) },
            product?.let { AccountShouldBeOwnerOfProductRule(account, it) }
        )
        rules.addAll(getAdditionalRules(account, store, product))
        return RuleSet(
            rules.filterNotNull()
        )
    }

    protected open fun getAdditionalRules(account: Account, store: Store?, product: Product?): List<Rule?> = emptyList()

    private fun getProduct(context: WorkflowContext<Req, Resp>): Product? =
        getProductId(context)?.let {
            marketplaceAccessApi.getProduct(it).product
        }
}
