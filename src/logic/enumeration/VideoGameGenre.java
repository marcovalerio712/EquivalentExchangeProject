package logic.enumeration;

public enum VideoGameGenre {
	ACTION, ADVENTURE, HORROR, MOBA, OTHER, PLATFORM, PUZZLE, ROLEPLAYING_GAME, SANDBOX, SIMULATION, SPORT, STRATEGY, SURVIVAL;
	
	public static VideoGameGenre getGenre(String genre) {
		return ADVENTURE;
	}
}
