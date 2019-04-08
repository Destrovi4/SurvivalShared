package xyz.destr.rpg.entity.skill;

import java.util.UUID;

public class StaticSkill {
	public static final UUID MOVE = UUID.fromString("94d253a8-ab6c-4642-8db7-6ef0ff587b0b");
	public static final UUID SET_BLOCK = UUID.fromString("202181db-a058-4f01-b4ea-8e54d8317806");
	
	public static void main(String[] args) {
		System.out.println(UUID.randomUUID());
	}
}
