package com.wutsi.marketplace.manager.endpoint

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.marketplace.access.dto.CreatePictureResponse
import com.wutsi.marketplace.access.dto.GetProductResponse
import com.wutsi.marketplace.manager.Fixtures
import com.wutsi.marketplace.manager.dto.AddPictureRequest
import com.wutsi.marketplace.manager.dto.AddPictureResponse
import com.wutsi.platform.core.error.ErrorResponse
import com.wutsi.workflow.error.ErrorURN
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AddPictureControllerTest : AbstractProductControllerTest<AddPictureRequest>() {
    companion object {
        const val PICTURE_ID = 111L
    }

    override fun url() = "http://localhost:$port/v1/pictures"

    override fun createRequest() = AddPictureRequest(
        productId = PRODUCT_ID,
        url = "https://www.img.com/1.png"
    )

    @BeforeEach
    override fun setUp() {
        super.setUp()

        doReturn(CreatePictureResponse(PICTURE_ID)).whenever(marketplaceAccessApi).createPicture(any())
    }

    @Test
    fun add() {
        // WHEN
        val response = rest.postForEntity(url(), request, AddPictureResponse::class.java)

        // THEN
        assertEquals(HttpStatus.OK, response.statusCode)

        assertEquals(PICTURE_ID, response.body?.pictureId)

        verify(marketplaceAccessApi).createPicture(
            com.wutsi.marketplace.access.dto.CreatePictureRequest(
                productId = request!!.productId,
                url = request!!.url
            )
        )

        verify(eventStream, never()).publish(any(), any())
    }

    @Test
    fun tooManyPictures() {
        // GIVEN
        product = Fixtures.createProduct(
            id = PRODUCT_ID,
            storeId = STORE_ID,
            pictures = listOf(
                Fixtures.createPictureSummary(),
                Fixtures.createPictureSummary(),
                Fixtures.createPictureSummary(),
                Fixtures.createPictureSummary(),
                Fixtures.createPictureSummary()
            )
        )
        doReturn(GetProductResponse(product)).whenever(marketplaceAccessApi).getProduct(any())

        // WHEN
        val ex = assertThrows<HttpClientErrorException> {
            submit()
        }

        // THEN
        assertEquals(HttpStatus.CONFLICT, ex.statusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.PICTURE_LIMIT_REACHED.urn, response.error.code)

        verify(marketplaceAccessApi, never()).createStore(any())
        verify(eventStream, never()).publish(any(), any())
    }
}
