package xyz.destr.rpg.client;

public class TestWaxworkClient {
	public static void main(String[] args) {
		final WaxworkClient client = new WaxworkClient(new TestEntityBehavior());
		System.out.println("Start test !WAX! client");
		client.run();
	}
}
