package main;

import io.vertx.core.Vertx;

public class ServerThread extends Thread{
	private RoomState room;
	public ServerThread(RoomState room) {
		this.room = room;
	}
	
	@Override
	public void run() {
		Vertx vertx = Vertx.vertx();
		DataService service = new DataService(8080,room);
		vertx.deployVerticle(service);
	}
}