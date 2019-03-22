package xyz.destr.rpg.world;

public enum WorldDirection {
		
	EAST(1, 0, 0){
		
		@Override
		public WorldDirection reverse() {
			return WEST;
		}
		
		@Override
		public WorldDirection right90() {
			return SOUTH;
		}
		
		@Override
		public WorldDirection left90() {
			return NORTH;
		}
		
	},
	SOUTH(0, 1, 0){

		@Override
		public WorldDirection reverse() {
			return NORTH;
		}
		
		@Override
		public WorldDirection right90() {
			return WEST;
		}
		
		@Override
		public WorldDirection left90() {
			return EAST;
		}
		
	},
	WEST(-1, 0, 0){

		@Override
		public WorldDirection reverse() {
			return EAST;
		}
		
		@Override
		public WorldDirection right90() {
			return NORTH;
		}
		
		@Override
		public WorldDirection left90() {
			return SOUTH;
		}
		
	},
	NORTH(0, -1, 0){

		@Override
		public WorldDirection reverse() {
			return SOUTH;
		}
		
		@Override
		public WorldDirection right90() {
			return EAST;
		}
		
		@Override
		public WorldDirection left90() {
			return WEST;
		}
		
	},
	TOP(0, 0, 1){

		@Override
		public WorldDirection reverse() {
			return BOTTOM;
		}
		
		@Override
		public WorldDirection right90() {
			return TOP;
		}
		
		@Override
		public WorldDirection left90() {
			return TOP;
		}
		
	},
	BOTTOM(0, 0, -1){

		@Override
		public WorldDirection reverse() {
			return TOP;
		}
		
		@Override
		public WorldDirection right90() {
			return BOTTOM;
		}
		
		@Override
		public WorldDirection left90() {
			return BOTTOM;
		}
		
	};
	
	public static final WorldDirection[] VALUES = values();
	
	public final int x;
	public final int y;
	public final int z;
	
	WorldDirection(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	abstract public WorldDirection reverse();
	abstract public WorldDirection right90();
	abstract public WorldDirection left90();
	
}
