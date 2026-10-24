package au.com.example.microservice.framework

import groovy.json.JsonSlurper

import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

trait HttpTrait {
    def uri
    def data
    def status
    def header

    def setUri(endpoint) {
        uri = endpoint
    }

    def getStatus() {
        status
    }

    def getData() {
        data
    }

    def getHeader() {
        header
    }

    def send(HttpRequest request) {
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString())
        status = response.statusCode()
        header = response.headers().map()
        def responseContentType = response.headers().firstValue('Content-Type').orElse('')
        data = responseContentType.contains('json') && response.body() ? new JsonSlurper().parseText(response.body()) : response.body()
    }

    def get(path) {
        send(HttpRequest.newBuilder(URI.create("${uri}${path}"))
                .header('Accept', 'application/json')
                .GET()
                .build())
    }

    def post(path, headers, body, contentType) {
        def builder = HttpRequest.newBuilder(URI.create("${uri}${path}"))
                .header('Content-Type', contentType as String)
                .header('Accept', 'application/json')
                .POST(HttpRequest.BodyPublishers.ofString(body as String))
        headers?.each { k, v -> builder.header(k as String, v as String) }
        send(builder.build())
    }

    def multipart(path, file) {
        def boundary = "----spock-${UUID.randomUUID()}"
        def bytes = new ByteArrayOutputStream()
        bytes << "--${boundary}\r\n"
        bytes << "Content-Disposition: form-data; name=\"file\"; filename=\"${file.getOriginalFilename()}\"\r\n"
        bytes << "Content-Type: ${file.getContentType()}\r\n\r\n"
        bytes << file.getBytes()
        bytes << "\r\n--${boundary}--\r\n"

        send(HttpRequest.newBuilder(URI.create("${uri}${path}"))
                .header('Content-Type', "multipart/form-data; boundary=${boundary}")
                .POST(HttpRequest.BodyPublishers.ofByteArray(bytes.toByteArray()))
                .build())
    }

    def payslipSummary(body) {
        def path = 'payslip/monthly/summary'
        def contentType = 'application/json'
        post(path, null, body, contentType)
    }

    def payslipSummaries(body) {
        def path = 'payslip/monthly/summaries'
        def contentType = 'application/json'
        post(path, null, body, contentType)
    }

    def payslipSummariesBatch(file) {
        def path = 'payslip/monthly/summaries/batch'
        multipart(path, file)
    }
}
