package analysis;

public enum AnalysisType {

    CREATION(1, "creation"),
    LOC(2, "LOC"),
    CO_AUTHORSHIP(3, "co-authorship");

    private int index;
    private String strRep;

    AnalysisType(int index, String strRep) {
        this.index = index;
        this.strRep = strRep;
    }

    public int getIndex() {
        return this.index;
    }

    @Override
    public String toString() {
        return this.strRep;
    }

}
