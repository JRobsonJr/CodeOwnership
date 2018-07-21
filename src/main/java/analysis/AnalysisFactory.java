package analysis;

import artifact.ArtifactRepository;
import student.StudentRepository;

public class AnalysisFactory {

    public static AbstractAnalysis getAnalysis(AnalysisType analysisType, StudentRepository studentRepository, ArtifactRepository artifactRepository) {
        if (analysisType.equals(AnalysisType.CREATION)) {
            return new CreationAnalysis(studentRepository, artifactRepository);
        } else if (analysisType.equals(AnalysisType.LOC)) {
            return new LOCAnalysis(studentRepository, artifactRepository);
        } else if (analysisType.equals(AnalysisType.CO_AUTHORSHIP)) {
            return new LOCPercentAnalysis(studentRepository, artifactRepository);
        } else {
            return null;
        }
    }

}
