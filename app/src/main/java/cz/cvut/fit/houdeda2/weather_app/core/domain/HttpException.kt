package cz.cvut.fit.houdeda2.weather_app.core.domain

class HttpException(statusCode: Int, bodyAsText: String) : Exception() {
    override val message = "statusCode=$statusCode, bodyAsText=$bodyAsText"
}