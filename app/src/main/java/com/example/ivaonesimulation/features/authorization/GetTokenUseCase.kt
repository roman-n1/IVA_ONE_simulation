package com.example.ivaonesimulation.features.authorization

class GetTokenUseCase(
    private val haveToken: Boolean,
) {

    operator fun invoke(): String? {
        return if (haveToken) {
            "saved token"
        } else {
            null
        }
    }
}