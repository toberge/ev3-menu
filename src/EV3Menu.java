/**
 *	EV3Menu.java
 *	Håndterer menyklassene og setter i gang det brukeren vil ha gjort.
 *	Går nå opp og ned i et klient-spesifisert tre av menyer.
 *	
 *	@author Tore Bergebakken
 */

public class EV3Menu {
	private MenuList[] branchTree = new MenuList[5]; // 5 at the moment
	private byte currentMenu = 0; // there are no menus beyond this index
	private static final byte SELECTED_AT = 3; // y-pos of marked entry

	private final EV3GUI gui = new EV3GUI();
	
	/* All them opcodes */
	public static final byte SPEED_MENU = 11;
	public static final byte CLIMB_DYNAMIC = 22;
	public static final byte CLIMB_KALVSKINNET = 23;
	public static final byte START_CLIMBING = 44;
	public static final byte GO_UNDERWATER = 32;
	public static final byte STAY_DRY = 33;
	public static final byte FLY_AWAY = 34;
	public static final byte BULLSHIT = 110;

	private int theSpeed = 375; // temp

	/* Constructors */
	public EV3Menu(MenuList topMenu) {
		branchTree[currentMenu] = topMenu; // no deep copy - clone constructor?
		gui.setLight(EV3GUI.GREEN);
	}

	/*
	 * The menu now branches, going further down an array of MenuList objects.
	 */
	private void setMenu(MenuList nextMenu) {
		if (currentMenu + 1 < branchTree.length) {
			currentMenu++;
		} else { // since we don't need to extend our array in this usage
			throw new IllegalArgumentException("Can't branch further, mate. (placeholder exception)");
		}
		branchTree[currentMenu] = nextMenu; // again, no deep copy. saving resources.
	}

	/*
	 * implemented in buttonResponse()
	 * private boolean goBack() { if (currentMenu >= 0) {currentMenu--;} else {//exit program} } 
	 */

	/* Displaying the array formatted in MenuList */
	public void showMenu() {
		gui.displayList(branchTree[currentMenu].getEntryList(EV3GUI.Y_LIMIT, SELECTED_AT));
	}

	/*
	 * Show a message on the EV3 screen, accepting newline markers
	 * You'll have to use "\n \n" to get an empty line.
	 */
	private void showMessage(String msg) {
		String[] msgSplit = msg.split("\n");
		gui.displayList(msgSplit);
		gui.waitForKeyPress();
	}

	/* Private helper method used in askForValue() */
	private void valuePrompt(String[] msgSplit, int value) {
		int index = gui.displayList(msgSplit);
		gui.write("Current value: " + value, index);
	}

	/* Ask user to set a value */
	private int askForValue(String msg, int value, int interval, int min, int max) {
		String[] msgSplit = msg.split("\n");

		valuePrompt(msgSplit, value);
		int opt = gui.waitForKeyPress();
		while (!(gui.isEscape(opt) || gui.isEnter(opt))) {
			if (gui.isUp(opt)) {
				if (value + interval <= max) {
					value += interval;
				} else {
					value = min;
				}
			} else if (gui.isDown(opt)) {
				if (value - interval >= min) {
					value -= interval;
				} else {
					value = max;
				}
			}
			valuePrompt(msgSplit, value);
			opt = gui.waitForKeyPress();
		}
		showMessage(" \n \nValue set to:\n" + value);
		return value;
	}

	/* Simple thing for gooding the bye */
	public void sayGoodbye() {
		gui.setLight(EV3GUI.FLICKER_GREEN);
		for (int i = 0; i < 5; i++) {
			gui.clear();
			String disp = "Goodbye";
			for (int j = 0; j < i + 1; j++) {
				disp += ".";
			}
			gui.write(disp, 3);
			gui.wait(350);
		}
	}

	/* Check buttons and do things, return false if escape or 0 somehow */
	public boolean buttonResponse() {
		int opt = gui.waitForKeyPress();
		if (opt > 0) {
			if (gui.isUp(opt)) {
				if (!branchTree[currentMenu].browseUp()) {
					gui.setLight(EV3GUI.BLINK_RED, 500);
				}
			} else if (gui.isDown(opt)) {
				if (!branchTree[currentMenu].browseDown()) {
					gui.setLight(EV3GUI.BLINK_RED, 500);
				}
			} else if (gui.isEnter(opt)) {
				activate();
			} else if (gui.isEscape(opt)) {
				if (currentMenu <= 0) { // at top level (avoiding errors)
					gui.clear();
					return false;
				} else { // at lower level, go up one level
					currentMenu--;
				}
			}
			return true;
		}
		return false;
	}


	/* Do what the MenuEntry holds */
	private void activate() {
		if (branchTree[currentMenu].doesBranch()) {
			setMenu(branchTree[currentMenu].getBranch());
		} else {
			doAction(branchTree[currentMenu].getAction());
		}
	}

	/* The actual action happens here */
	private void doAction(byte opcode) {
		switch (opcode) {
			case SPEED_MENU:
				theSpeed = askForValue("Welcome to our\n--SPEED MENU--\nWould you kindly\nSET MY SPEEED",
								 theSpeed, 5, 100, 900);
				break;
			case CLIMB_KALVSKINNET:
				gui.setLight(EV3GUI.ORANGE);
				showMessage("\n \nClimb mode: \nKalvskinnet");
				gui.setLight(EV3GUI.GREEN);
				break;
			case CLIMB_DYNAMIC:
				gui.setLight(EV3GUI.ORANGE);
				showMessage("\n \nClimb mode: \nDynamic");
				gui.setLight(EV3GUI.GREEN);
				break;
			case START_CLIMBING:
				gui.setLight(EV3GUI.FLICKER_ORANGE);
				showMessage("\n \nI won't climb for ya\n \n   heh");
				gui.setLight(EV3GUI.GREEN);
				break;
			case GO_UNDERWATER:
				gui.setLight(EV3GUI.RED);
				showMessage("\n \nbLUb BluB");
				gui.setLight(EV3GUI.GREEN);
				break;
			case STAY_DRY:
				gui.setLight(EV3GUI.BLINK_ORANGE);
				showMessage("\n \nIsn't this a little\ntoo dry for ya?");
				gui.setLight(EV3GUI.GREEN);
				break;
			case FLY_AWAY:
				gui.setLight(EV3GUI.FLICKER_RED);
				showMessage("\n \n   *flutter*\n     *flutter*\nflop");
				gui.setLight(EV3GUI.GREEN);
				break;
			case BULLSHIT:
				showMessage(" \n \nComplete, utter\n \nB U L L S H I T\nTESTING DISPLAY\n cpabullityces\nDOES THIS SHOW?");
				break;
			default:
				showMessage(" \n \nInvalid opcode\nMajor flaw");
				break;
		}
	}
}