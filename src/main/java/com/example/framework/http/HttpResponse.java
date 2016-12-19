package com.example.framework.http;

import org.apache.http.Header;
import org.apache.http.ProtocolVersion;

import java.util.Arrays;
import java.util.Objects;

/**
 * Created by ZhaiJiaYi on 2016/12/19.
 */
public class HttpResponse {
    private int status;
    private String content;
    private Header[] headers;
    private ProtocolVersion protocolVersion;

    public HttpResponse() {
    }

    public static HttpResponse empty() {
        return new HttpResponse();
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Header[] getHeaders() {
        return this.headers;
    }

    public void setHeaders(Header[] headers) {
        this.headers = headers;
    }

    public ProtocolVersion getProtocolVersion() {
        return this.protocolVersion;
    }

    public void setProtocolVersion(ProtocolVersion protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public boolean equals(Object o) {
        if(this == o) {
            return true;
        } else if(!(o instanceof HttpResponse)) {
            return false;
        } else {
            HttpResponse that = (HttpResponse)o;
            return this.getStatus() == that.getStatus() && Objects.equals(this.getContent(), that.getContent()) && Arrays.equals(this.getHeaders(), that.getHeaders()) && Objects.equals(this.getProtocolVersion(), that.getProtocolVersion());
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(this.getStatus()), this.getContent(), this.getHeaders(), this.getProtocolVersion()});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("HttpResponse{");
        sb.append("status=").append(this.status);
        sb.append(", content=\'").append(this.content).append('\'');
        sb.append(", headers=").append(Arrays.toString(this.headers));
        sb.append(", protocolVersion=").append(this.protocolVersion);
        sb.append('}');
        return sb.toString();
    }
}

