package xyz.destr.rpg.client;

public class TestClient {
	
	public static void main(String[] args) {
		final BasicClient client = new BasicClient(new TestEntityBehavior());
		client.setArguments(args);
		System.out.println("Start test client");
		client.run();
	}
	
}
