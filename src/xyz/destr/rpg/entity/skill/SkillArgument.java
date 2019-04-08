package xyz.destr.rpg.entity.skill;

import java.util.ArrayList;

public interface SkillArgument {
	
	public SkillArgumentType getType();

	public SkillArgument clone();
	
	public static void copy(ArrayList<SkillArgument> to, ArrayList<SkillArgument> from) {
		to.clear();
		for(SkillArgument skillArgument: from) {
			to.add(skillArgument.clone());
		}
	}
}
