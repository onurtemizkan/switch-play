package org.openqa.selenium.remote.http;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.base.Strings.nullToEmpty;
import static com.google.common.net.HttpHeaders.CACHE_CONTROL;
import static com.google.common.net.HttpHeaders.CONTENT_LENGTH;
import static com.google.common.net.HttpHeaders.CONTENT_TYPE;
import static com.google.common.net.HttpHeaders.EXPIRES;
import static com.google.common.net.MediaType.JSON_UTF_8;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_OK;

import org.openqa.selenium.remote.BeanToJsonConverter;
import org.openqa.selenium.remote.ErrorCodes;
import org.openqa.selenium.remote.JsonException;
import org.openqa.selenium.remote.JsonToBeanConverter;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.remote.ResponseCodec;

/**
 * A response codec that adheres to the Selenium project's JSON/HTTP wire protocol.
 *
 * @see <a href="https://code.google.com/p/selenium/wiki/JsonWireProtocol">
 *   JSON wire protocol</a>
 */
public class JsonHttpResponseCodec implements ResponseCodec<HttpResponse> {

  private final ErrorCodes errorCodes = new ErrorCodes();
  private final BeanToJsonConverter beanToJsonConverter = new BeanToJsonConverter();
  private final JsonToBeanConverter jsonToBeanConverter = new JsonToBeanConverter();

  /**
   * Encodes the given response as a HTTP response message. This method is guaranteed not to throw.
   *
   * @param response The response to encode.
   * @return The encoded response.
   */
  @Override
  public HttpResponse encode(Response response) {
    int status = response.getStatus() == ErrorCodes.SUCCESS
                 ? HTTP_OK
                 : HTTP_INTERNAL_ERROR;

    byte[] data = beanToJsonConverter.convert(response).getBytes(UTF_8);

    HttpResponse httpResponse = new HttpResponse();
    httpResponse.setStatus(status);
    httpResponse.setHeader(CACHE_CONTROL, "no-cache");
    httpResponse.setHeader(EXPIRES, "Thu, 01 Jan 1970 00:00:00 GMT");
    httpResponse.setHeader(CONTENT_LENGTH, String.valueOf(data.length));
    httpResponse.setHeader(CONTENT_TYPE, JSON_UTF_8.toString());
    httpResponse.setContent(data);

    return httpResponse;
  }

  @Override
  public Response decode(HttpResponse encodedResponse) {
    String contentType = nullToEmpty(encodedResponse.getHeader(CONTENT_TYPE));
    String content = encodedResponse.getContentString().trim();
    try {
      return jsonToBeanConverter.convert(Response.class, content);
    } catch (JsonException e) {
      if (contentType.startsWith("application/json")) {
        throw new IllegalArgumentException(
            "Cannot decode response content: " + content, e);
      }
    } catch (ClassCastException e) {
      if (contentType.startsWith("application/json")) {
        if (content.isEmpty()) {
          // The remote server has died, but has already set some headers.
          // Normally this occurs when the final window of the firefox driver
          // is closed on OS X. Return null, as the return value _should_ be
          // being ignored. This is not an elegant solution.
          return new Response();
        }
        throw new IllegalArgumentException(
            "Cannot decode response content: " + content, e);
      }
    }

    Response response = new Response();
    int statusCode = encodedResponse.getStatus();
    if (statusCode < 200 || statusCode > 299) {
      // 4xx represents an unknown command or a bad request.
      if (statusCode > 399 && statusCode < 500) {
        response.setStatus(ErrorCodes.UNKNOWN_COMMAND);
      } else {
        response.setStatus(ErrorCodes.UNHANDLED_ERROR);
      }
    }

    if (encodedResponse.getContent().length > 0) {
      response.setValue(content);
    }

    if (response.getValue() instanceof String) {
      // We normalise to \n because Java will translate this to \r\n
      // if this is suitable on our platform, and if we have \r\n, java will
      // turn this into \r\r\n, which would be Bad!
      response.setValue(((String) response.getValue()).replace("\r\n", "\n"));
    }
    response.setState(errorCodes.toState(response.getStatus()));
    return response;
  }
}
