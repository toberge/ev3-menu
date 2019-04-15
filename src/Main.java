/**
 * Main.java
 * Is main, very yes
 * Gjør ting
 * Det er her safta ligger
 *
 * @author Joakim Moe Adolfsen, Tore Bergebakken, Jon Åby Bergquist, Magnus Brevik, Aleksander Skogan
 *
 */

public class Main {

    //Lage poengløse undermenyer
    private static final MenuEntry bullshit = new MenuEntry("Take some bullshit and go", EV3Menu.BULLSHIT);
    private static final MenuEntry stay = new MenuEntry("Don't stay", -45);
    private static final MenuEntry walk = new MenuEntry("Walk in the hay", -16);
    private static final MenuEntry[] array1_1 = {bullshit, stay, walk};
    private static final MenuList list1_1 = new MenuList("**POINTLESS**", array1_1);

    private static final MenuEntry nautilus = new MenuEntry("Go underwater", EV3Menu.GO_UNDERWATER);
    private static final MenuEntry dry = new MenuEntry("Stay dry", EV3Menu.STAY_DRY);
    private static final MenuEntry fly = new MenuEntry("Fly away", EV3Menu.FLY_AWAY);
    private static final MenuEntry[] array1_2 = {nautilus, dry, fly};
    private static final MenuList list1_2 = new MenuList("**REDACTED**", array1_2);

    //Lage hovedmeny
    private static final MenuEntry speedMenu = new MenuEntry("Set my speed", EV3Menu.SPEED_MENU);
    private static final MenuEntry climbCalf = new MenuEntry("Climb the calf", EV3Menu.CLIMB_KALVSKINNET);
    private static final MenuEntry climbDefault = new MenuEntry("Climb anywhere", EV3Menu.CLIMB_DYNAMIC);
    private static final MenuEntry startClimb = new MenuEntry("Start climbing", EV3Menu.START_CLIMBING);
    private static final MenuEntry pointer1_1 = new MenuEntry("Witness the truth", list1_1);
    private static final MenuEntry pointer1_2 = new MenuEntry("Witness the juice", list1_2);
    private static final MenuEntry[] array0 = {climbCalf, climbDefault, startClimb, speedMenu, pointer1_1, pointer1_2};
    private static final MenuList list0 = new MenuList("**MENU IN MAIN**", array0);

    private static final EV3Menu menu = new EV3Menu(list0);

    public static void main(String[] args) {
        menu.showMenu();
        while (menu.buttonResponse()) {
            menu.showMenu();
        }
        menu.sayGoodbye();
    }
}