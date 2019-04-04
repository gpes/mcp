package ifpb.gpes;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 06/07/2017, 19:04:36
 */
public interface ProjectType {

    public void addSources(String source);

    public String sources();

    public void addClasspath(String path);

    public String classpath();
}
