package expertise;

public enum Expertise {

    INTERFACE("implements"),
    INHERITANCE("extends"),
    TESTS("@Test"),
    EXCEPTION_HIERARCHY("extends Exception", "extends RuntimeException"),
    EXCEPTIONS("Exception", "RuntimeException", "throws"),
    EXCEPTION_HANDLING("try", "catch"),
    PERSISTENCE ("IOException", "File"),
    ABSTRACT_CLASSES("abstract");

    private String[] keywords;

    Expertise(String... keywords) {
        this.keywords = keywords;
    }

    public boolean containsKeyword(String query) {
        for (String keyword : this.keywords) {
            if (keyword.equals(query)) {
                return true;
            }
        }

        return false;
    }

}
