package dispatcher.demo;
 
import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
 
import com.zehin.sns.protocol.http.xml.codec.HttpXmlRequest;
import com.zehin.sns.protocol.http.xml.codec.HttpXmlResponse;
import com.zehin.sns.protocol.http.xml.pojo.HttpRequestMessage;
import com.zehin.sns.protocol.http.xml.pojo.HttpResponseMessage;
 
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
 
@Sharable
public final class HttpXmlServerHandler extends SimpleChannelInboundHandler<HttpXmlRequest> {
 
    @Override
    public void messageReceived(final ChannelHandlerContext ctx, HttpXmlRequest xmlRequest) throws Exception {
        HttpRequest request = xmlRequest.getRequest();
        HttpRequestMessage reqMessage = (HttpRequestMessage) xmlRequest.getBody();
        System.out.println("Http server receive request : " + reqMessage);
        HttpResponseMessage resMessage = dobusiness(reqMessage);
        ChannelFuture future = ctx.writeAndFlush(new HttpXmlResponse(null, resMessage));
        if (!isKeepAlive(request)) {
            future.addListener(new GenericFutureListener<Future<? super Void>>() {
                public void operationComplete(Future future) throws Exception {
                    ctx.close();
                }
            });
        }
    }
 
    private HttpResponseMessage dobusiness(HttpRequestMessage req) {
        HttpResponseMessage resMessage = new HttpResponseMessage();
        if (req.getCmd() == 0) {
            resMessage.setResult(true);
        } else {
            // other verify code here...
        }
        return resMessage;
    }
 
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if (ctx.channel().isActive()) {
            sendError(ctx, INTERNAL_SERVER_ERROR);
        }
    }
 
    private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status,
                Unpooled.copiedBuffer("失败: " + status.toString() + "\r\n", CharsetUtil.UTF_8));
        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}