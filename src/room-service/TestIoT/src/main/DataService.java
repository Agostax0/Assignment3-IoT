package main;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;

/*
 * Data Service as a vertx event-loop 
 */
public class DataService extends AbstractVerticle {

	private int port;
	private RoomState room;
	
	public DataService(int port,RoomState room) {		
		this.port = port;
		this.room = room;
	}

	@Override
	public void start() {		
		Router router = Router.router(vertx);
		router.route().handler(BodyHandler.create());
		CorsHandler corsHandler = CorsHandler.create()
				.allowedMethod(HttpMethod.GET)
				.allowedMethod(HttpMethod.POST)
				.allowedHeader("Content-type");
		router.route().handler(corsHandler);
		
		
		router.post("/api/data").handler(this::handleAddNewData);
		router.get("/api/data").handler(this::handleGetData);		
		vertx
			.createHttpServer()
			.requestHandler(router)
			.listen(port);

		log("Service ready on port: " + port);
	}
	
	private void handleAddNewData(RoutingContext routingContext) {
		HttpServerResponse response = routingContext.response();
		// log("new msg "+routingContext.getBodyAsString());
		JsonObject res = routingContext.getBodyAsJson();
		if (res == null) {
			sendError(400, response);
		} else {
			System.out.println("recvd: " + res.toString());
			String command = res.getString("command");
			//long time = System.currentTimeMillis();
			
			room.addToHistory(new DataPoint(command));
			
			//log("New command: " + command + " on " + new Date(time));
			response.setStatusCode(200).end();
		}
	}
	
	private void handleGetData(RoutingContext routingContext) {
		
		System.out.println("request for data");
		
		JsonArray arr = new JsonArray();
		
		JsonArray dev = new JsonArray();
		for (Device d: this.room.getDeviceStates()) {
			JsonObject data = new JsonObject();
			data.put("name", d.getName());
			data.put("value", d.getValue());
			dev.add(data);
		}
		JsonObject device = new JsonObject();
		device.put("devices", dev);
		arr.add(device);
		
		JsonArray hs = new JsonArray();
		
		var history = this.room.getHistory();
		
		var time_span_key = history.keySet().parallelStream().sorted().collect(Collectors.toList()); 
		
		for (Long time : time_span_key) {
			
			var state = history.get(time);
			
			JsonObject data = new JsonObject();
			data.put("time", formatTime(time));
			data.put("LED", state.get(1).getValue()>0 ? 120 : 0);
			data.put("SERVO", state.get(2).getValue());
			data.put("PIR", state.get(3).getValue());
			data.put("LL", state.get(4).getValue() / 4);
			//data.put("value", d.getDeviceID()==1 ? d.getValue()>0 ? 111 : 000 : d.getValue());
			hs.add(data);
		}
		arr.add(hs);
		
		routingContext.response()
			.putHeader("Access-Control-Allow-Origin", "*")
			.putHeader("content-type", "application/json")
			.end(arr.encodePrettily());
	}
	
	private void sendError(int statusCode, HttpServerResponse response) {
		response.setStatusCode(statusCode).end();
	}

	private void log(String msg) {
		System.out.println("[DATA SERVICE] "+msg);
	}
	
	private String formatTime(long time) {
		return new Date(time).getHours() +":"+new Date(time).getMinutes() +":"+ new Date(time).getSeconds();
	}

}