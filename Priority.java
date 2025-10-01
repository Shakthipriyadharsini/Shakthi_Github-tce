public enum Priority {
    HIGH, MEDIUM, LOW;

    public static Priority fromString(String s) {
        if (s == null) throw new IllegalArgumentException("Priority is null");
        switch (s.trim().toLowerCase()) {
            case "high": return HIGH;
            case "medium": return MEDIUM;
            case "low": return LOW;
            default: throw new IllegalArgumentException("Unknown priority: " + s);
        }
    }
}