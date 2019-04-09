package xyz.destr.rpg.entity.skill;

import xyz.destr.rpg.world.MaterialType;

public class SkillArgumentMaterialType implements SkillArgument {

	private static final long serialVersionUID = 543872555880738701L;
	public MaterialType materialType;
	
	public SkillArgumentMaterialType() {
		materialType = MaterialType.EMPTY;
	}
	
	public SkillArgumentMaterialType(MaterialType material) {
		this.materialType = material;
	}
	
	@Override
	public SkillArgumentType getType() {
		return SkillArgumentType.MATERIAL_TYPE;
	}	
	
	@Override
	public SkillArgumentMaterialType clone() {
		return new SkillArgumentMaterialType(materialType);
	}
	
}
