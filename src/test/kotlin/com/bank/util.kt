package com.bank

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.matching.UrlPattern

fun mockPost(urlPattern: UrlPattern, status: Int, body: String) {
    WireMock.stubFor(
        WireMock.post(urlPattern)
            .willReturn(
                WireMock.aResponse()
                    .withStatus(status)
                    .withHeader("Content-Type", "application/json")
                    .withBody(body),
            ),
    )
}
