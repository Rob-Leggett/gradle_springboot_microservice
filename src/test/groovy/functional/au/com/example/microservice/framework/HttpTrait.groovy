package au.com.example.microservice.framework

import groovyx.net.http.HTTPBuilder
import groovyx.net.http.HttpResponseException
import groovyx.net.http.Method
import groovyx.net.http.RESTClient
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.entity.mime.content.ByteArrayBody

trait HttpTrait {
    def uri
    def data
    def status
    def header

    def setUri(endpoint) {
        uri = endpoint;
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

    def post(path, headers, body, contentType) {
        try {
            def response = new RESTClient(uri).post(
                    path: path,
                    headers: headers,
                    body: body,
                    requestContentType: contentType
            )
            status = response.status
            data = response.data
            header = response.headers
        } catch (HttpResponseException e) {
            status = e.getStatusCode()
            data = e.getResponse().getData()
        }
    }

    def multipart(path, contentType, file) {

        def http = new HTTPBuilder(uri + path)

        http.handler.success = { resp, body ->
            status = resp.status
            data = body
            header = resp.headers
        }

        http.handler.failure = { resp, body ->
            status = resp.status
            data = body
        }

        http.request(Method.POST) { request ->

            def multipartEntityBuilder = MultipartEntityBuilder.create()

            multipartEntityBuilder.addPart("file", new ByteArrayBody(file.getBytes(), file.getContentType(), file.getName()))

            request.setEntity(multipartEntityBuilder.build())
        }
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
        def contentType = 'multipart/form-data'
        multipart(path, contentType, file)
    }
}
