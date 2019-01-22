package xyz.destr.survival.io.action;

import xyz.destr.survival.io.view.ActorView;

public class ActorActionWriter {
	
	private ActorView actorView;
	private ActorTypeAction actorTypeAction;
	private ActorAction actorAction;
	
	public void clear() {
		actorView = null;
		actorTypeAction = null;
		actorAction = null;
	}
	
	public void set(ActorView actorView, ActorTypeAction actorTypeAction) {
		this.actorView = actorView;
		this.actorTypeAction = actorTypeAction;
	}
	
	public ActorAction getActorAction() {
		if(actorAction == null) {
			actorAction = actorTypeAction.actorList.addInstance();
			actorAction.uid = actorView.uid;
		}
		return actorAction;
	}
	
	public void move(int x, int y) {
		ActorCommand command = getActorAction().commandList.addInstance();
		command.type = ActorCommand.Type.MOVE;
		command.properties.setInteger("x", x);
		command.properties.setInteger("y", y);
	}
	
	public void eatTileObject() {
		ActorCommand command = getActorAction().commandList.addInstance();
		command.type = ActorCommand.Type.EAT_TILE_OBJECT;
	}
	
}
