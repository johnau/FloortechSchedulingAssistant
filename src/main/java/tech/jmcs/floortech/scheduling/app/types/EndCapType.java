package tech.jmcs.floortech.scheduling.app.types;

public enum EndCapType {
    TYPE_A ("Type A"),
    TYPE_B ("Type B"),
    TYPE_C ("Type C"),
    TYPE_D ("Type D"),
    TYPE_E ("Type E"),
    TYPE_F ("Type F"),
    TYPE_G ("Type G"),
    TYPE_H ("Type H"),
    TYPE_I ("Type I"),
    TYPE_J ("Type J"),
    TYPE_K ("Type K"),
    SPECIAL ("Special"),
    ADJUSTABLE ("Adjustable"),
    STANDARD ("Standard");

    private final String name;

    private EndCapType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static EndCapType fromName(String name) {
        for (EndCapType value : EndCapType.values()) {
            String n = value.getName();
            if (name.toLowerCase().equals(n.toLowerCase())) {
                return value;
            }
        }
        return null;
    }
}
