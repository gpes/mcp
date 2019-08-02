package ifpb.gpes.domain;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 26/05/2017, 21:32:40
 */
public class SampleObject {
    private HasJCFObject a = new HasJCFObject();
    public void teste() {
        a.getElements().add(new HasJCFObject());
        a.getElements().remove(0);
        a.getElements().set(0, null);
    }

    public HasJCFObject m1() {
        return this.a;
    }
}
