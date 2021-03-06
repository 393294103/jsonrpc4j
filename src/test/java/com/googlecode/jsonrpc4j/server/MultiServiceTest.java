package com.googlecode.jsonrpc4j.server;

import static com.googlecode.jsonrpc4j.JsonRpcBasicServer.RESULT;
import static com.googlecode.jsonrpc4j.util.Util.decodeAnswer;
import static com.googlecode.jsonrpc4j.util.Util.messageWithMapParamsStream;
import static com.googlecode.jsonrpc4j.util.Util.param1;
import static com.googlecode.jsonrpc4j.util.Util.param2;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.MockType;

import com.googlecode.jsonrpc4j.JsonRpcMultiServer;
import com.googlecode.jsonrpc4j.JsonRpcParam;

import java.io.ByteArrayOutputStream;

@RunWith(EasyMockRunner.class)
public class MultiServiceTest {

	private static final String serviceName = "Test";
	@Mock(type = MockType.NICE)
	private ServiceInterfaceWithParamNameAnnotation mockService;
	private JsonRpcMultiServer multiServer;
	private ByteArrayOutputStream byteArrayOutputStream;

	@Before
	public void setup() {
		multiServer = new JsonRpcMultiServer();
		multiServer.addService(serviceName, mockService, ServiceInterfaceWithParamNameAnnotation.class);
		byteArrayOutputStream = new ByteArrayOutputStream();
	}

	@Test
	public void callMethodExactNumberOfParametersNamed() throws Exception {
		EasyMock.expect(mockService.testMethod(param2)).andReturn("success");
		EasyMock.replay(mockService);

		multiServer.handleRequest(messageWithMapParamsStream(serviceName + JsonRpcMultiServer.DEFAULT_SEPARATOR + "testMethod", param1, param2), byteArrayOutputStream);

		assertEquals("success", decodeAnswer(byteArrayOutputStream).get(RESULT).textValue());
	}

	public interface ServiceInterfaceWithParamNameAnnotation {
		String testMethod(@JsonRpcParam("param1") String param1);
	}

}
